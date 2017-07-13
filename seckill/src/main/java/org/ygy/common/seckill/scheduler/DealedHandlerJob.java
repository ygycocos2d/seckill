package org.ygy.common.seckill.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ygy.common.seckill.util.Constant;
import org.ygy.common.seckill.util.RedisUtil;

/**
 * 我们的任务是--活着！哦，死了无所谓！
 * 说明：应用实例是否dump机检测，频率为默认1秒，总开关开启后且为集群部署启动该定时任务
 * 策略:
 * 	1、活着；当前应用实例通过每秒在缓存（redis）中更新值，表示自己还活着，通过获取其他应用实例的redis中的该部分数据，可以判断其他实例是否活着。
 * 	2、当前实例over了，那没办法了。
 * 	3、其他实例over了（redis上的时间比当前时间少了3秒，我们认为相应应用实例dump），
 * 该部分数据在缓存中的格式：
 * 
 *  1、goodsNumber_活动ID:Map<appno,商品数>
 *  作用：记录应用实例当前秒杀活动还有多少秒杀商品，同时当应用实例宕机后，其他应用实例可以将该宕机实例的未抢完的商品数平分
 *  说明：没心情，没啥可说的。
 *  	a、总开关启动后，当前应用实例获取自己要处理的当前活动的商品数，同时将该数值缓存
 *  	b、每秒杀成功一个商品，则商品数减1，同时缓存
 *  	c、当相应应用实例dump后，其他应用实例获取到该商品数，如果为0，则删除该挂掉实例相应缓存。非0则其他活着的应用平分该商品数
 *  
 * 	2、keepalive_+appno：任意值
 *  作用：应用实例是否活着检测
 * 	说明：如当前实例appno为1，则缓存为："keepalive_1:1",同时该缓存设置过期时间。
 * 		如过期时间为3秒，而定时任务每秒重新设置该缓存及过期时间，也就是说只要应用活着该缓存便不会过期而被清除。
 * 		其他应用实例通过获取该值判定应用是否活着，获取到值说明应用活着，否则说明该应用实例至少3秒没有更新该值，我们认定该应用实例dump了。
 * 		某应用实例dump了，其他应用实例可以根据dump了的实例的appno去获取其没处理完的秒杀商品数（根据策略去平分）
 * 
 *  3、succLog_活动ID:Map<userId,成功秒杀的商品数>
 * 	作用：记录用户某个秒杀活动中抢到的商品数，用于判断用户是否抢过该秒杀商品
 * 	说明：言简意赅，也没啥可说
 * 
 * 	4、comed_活动ID_appno:set集合<appno>
 * 	作用：记录当前活动dump的应用实例，dump后都有哪些其他实例来平分了其未处理完的商品数
 *  说明:睡觉，不说了！
 * 
 * @author ygy
 *
 */
public class DealedHandlerJob implements Job{
	
	private Logger       logger = LoggerFactory.getLogger(DealedHandlerJob.class);

	@Override
	public void execute(JobExecutionContext context)throws JobExecutionException {
		try {
			ActivityInfo info = SchedulerContext.getCurActivityInfo();
			if (null != info) {
				logger.info("DealedHandlerJob---activity--satrt");
				boolean noOk = true;
				while(noOk) {
					
					// 获取当前秒杀活动中所有应用实例处理的商品数:Map<appno,商品数>
					String goodsNumberKey = Constant.Cache.GOODS_NUMBER+info.getActivityId();
					Map<String, String> goodsNumberMap = RedisUtil.getHashMap(goodsNumberKey);
					
					// 获取所有应用实例是否活着
			    	List<String> keyList = new ArrayList<String>();
			    	for(Entry<String, String> en : goodsNumberMap.entrySet()) {
			    		keyList.add(Constant.Cache.KEEP_ALIVE + en.getKey());
			    	}
			    	String[] keys = keyList.toArray(new String[keyList.size()]);
			    	Map<String, Boolean> aliveMap = new HashMap<String, Boolean>();//Map<appno,是否活着>
			    	int aliveNum = 0;
			    	if (keys.length > 0) {
			    		List<String> appNoStatus = RedisUtil.getByKeys(keys);
			    		for (int i=0;i<appNoStatus.size();i++) {
			    			String key = keys[i].substring( (keys[i].indexOf("_")+1) );
				    		if (null != appNoStatus.get(i)) {
				    			aliveMap.put(key, true);
				    			aliveNum ++;
				    		} else {
				    			aliveMap.put(key, false);
				    		}
				    	}
			    	}
			    	
			    	// 如果有应用实例挂掉了,平分该应用未处理的商品数
			    	if (aliveNum < keys.length) {
			    		for(Entry<String, Boolean> en : aliveMap.entrySet()){
			    			if (!en.getValue()) {
			    				int goodsNumer = Integer.valueOf(goodsNumberMap.get(en.getKey()));
			    				if (goodsNumer <= 0) {//没有待处理商品数，挂就挂了不用管了
			    					RedisUtil.hdelete(goodsNumberKey,en.getKey());
			    					noOk = false;
			    					continue;
			    				}
			    				String comedKey = Constant.Cache.COMED+info.getActivityId()+"_"+en.getKey();
			    				Set<String> set = RedisUtil.smembers(comedKey);
			    				int num = 0;
			    				// 商品数非0但所有活着的应用实例都平分过该宕机实例的商品数了，说明又有应用挂了
			    				if (aliveNum == set.size()) {
			    					num = goodsNumer;//把剩下的商品直接处理了
			    				} else {
			    					// 如果当前实例已经平分过该挂掉的实例的待处理的商品数
				    				if (set.contains(SchedulerContext.getAppno())) {
				    					noOk = false;
				    					continue;
				    				}
				    				// 平分该宕机实例未处理的商品数
				    				if (aliveNum-set.size() != 0) {
				    					num = goodsNumer/(aliveNum-set.size());
				    				}
			    				}
			    				
			    				// 更新宕机应用实例商品数及被平分记录、当前应用实例商品数
			    				int oldGoodsNumber = Integer.valueOf(RedisUtil.getHashMapValue(goodsNumberKey, en.getKey()));
			    				if (oldGoodsNumber != goodsNumer) {//避免宕机实例的商品数不一致（其实还是有可能出现不一致，这里只是尽量减少）
			    					noOk = true;
			    					break;
			    				}
			    				RedisUtil.setHashMapValue(goodsNumberKey, en.getKey(), ""+(goodsNumer-num));
			    				RedisUtil.sadd(comedKey, SchedulerContext.getAppno());
			    				int newNum = info.getGoodsNum().addAndGet(num);
			    				RedisUtil.setHashMapValue(goodsNumberKey, SchedulerContext.getAppno(), ""+newNum);
			    				noOk = false;
			    			}
			    		}
			    	} else {
			    		noOk = false;
			    	}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("DealedHandlerJob execute exception...", e);
		}
	}

}

