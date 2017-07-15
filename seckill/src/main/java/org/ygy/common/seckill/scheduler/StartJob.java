package org.ygy.common.seckill.scheduler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.ygy.common.seckill.entity.ActivityEntity;
import org.ygy.common.seckill.entity.ImgEntity;
import org.ygy.common.seckill.service.ImgService;
import org.ygy.common.seckill.util.AtomicIntegerExt;
import org.ygy.common.seckill.util.Constant;
import org.ygy.common.seckill.util.RedisLock;
import org.ygy.common.seckill.util.RedisUtil;
import org.ygy.common.seckill.util.SpringContextUtil;

/**
 * 秒杀活动启动资源加载，当前应用实例处理的当前秒杀活动商品数。默认策略，平分。
 * 缓存数据结构：
 * 
 * @author gy
 *
 */
public class StartJob implements Job {
	
	private Logger       logger = LoggerFactory.getLogger(StartJob.class);
	
	private ImgService imgService;
	
	public StartJob() {
		try {
			imgService = (ImgService) SpringContextUtil.getBeanByClass(ImgService.class);
		} catch (Exception e) {
			logger.error("StartJob init exception...",e);
		}
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		ActivityInfo curActivityInfo = SchedulerContext.getCurActivityInfo();
		if (null == curActivityInfo) {
			ActivityEntity entity = SchedulerContext.getActivityQueue().getHeaderAndDel();
			if (null != entity) {
				try {
					curActivityInfo = new ActivityInfo();
					this.entity2Info(entity, curActivityInfo);
					SchedulerContext.setCurActivityInfo(curActivityInfo);
					// 获取图片等资源信息
					List<ImgEntity> imgList = this.imgService.getImgListByActivityId(curActivityInfo.getActivityId());
					curActivityInfo.setImgList(imgList);
					// 获取当前秒杀活动在当前应用中处理的秒杀商品数
//					Integer handlerGoodNumber = SchedulerContext.getCurAppHandleGoodsNum(curActivityInfo.getActivityId());
//					if (null != handlerGoodNumber && handlerGoodNumber.compareTo(0) > 0) {
//						curActivityInfo.getGoodsNum().set(handlerGoodNumber);
//					}
					// 获取各应用实例存活情况，平分秒杀商品数
					Map<String, Object> resultMap = KeepAliveJob.getAliveApp();
//					Map<String,Boolean> aliveMap = (Map<String,Boolean>)resultMap.get("aliveMap");
					int num = (int) resultMap.get("aliveNum");
					String comedKey = Constant.Cache.START_COMED+curActivityInfo.getActivityId();
					String lockKey = Constant.Cache.LOCK + comedKey;
					RedisLock lock = new RedisLock(lockKey);//互斥锁
					if (lock.acquireLockWithTimeout(
							RedisUtil.getConnect(), 5000L, 3000L)) {
						Set<String> set = RedisUtil.smembers(comedKey);
						if (!set.contains(SchedulerContext.getAppno())) {//没平分过
							Map<String, String> goodsMap = RedisUtil.getHashMap(Constant.Cache.GOODS_NUMBER+curActivityInfo.getActivityId());
							int usedGoodsNum = 0;
							for (String appno:set) {
								usedGoodsNum += Integer.parseInt(goodsMap.get(appno));
							}
							int leftGoodsNum = curActivityInfo.getGoodsNum().get() - usedGoodsNum;
							int handlerGoodNumber = 0;
							if (num - set.size() > 0) {
								handlerGoodNumber = leftGoodsNum / (num - set.size());
							}
							curActivityInfo.getGoodsNum().set(handlerGoodNumber);
							// 缓存平分记录
							RedisUtil.sadd(comedKey, SchedulerContext.getAppno());
							// 讲当前应用实例处理的当前活动秒杀商品数存入缓存
							RedisUtil.setHashMapValue(Constant.Cache.GOODS_NUMBER+curActivityInfo.getActivityId(),
									SchedulerContext.getAppno(), ""+curActivityInfo.getGoodsNum().get());
						}
						lock.releaseLock(RedisUtil.getConnect());
					}
					// 调度当前秒杀活动结束任务
					String name = curActivityInfo.getActivityId() + "_end";
					String group = curActivityInfo.getActivityGid() + "_end";
					Date date = new Date(curActivityInfo.getEndTime());
					SchedulerContext.getQuartzUtil().add(EndJob.class, name, group, date);
				} catch (Exception e) {
					logger.error("StartJob execute exception...",e);
				}
			}
		}
	}
	
	private void entity2Info(ActivityEntity entity, ActivityInfo curActivity) {
		Assert.notNull(entity, "source entity requested not null!");
		Assert.notNull(curActivity, "target info requested not null!");
		try {
			curActivity.setActivityId(entity.getActivityId());
			curActivity.setActivityGid(entity.getGroupId());
			curActivity.setStartTime(entity.getStartTime().getTime());
			curActivity.setEndTime(entity.getEndTime().getTime());
			curActivity.setGoodsId(entity.getGoodsId());
			AtomicIntegerExt atomicGoodsNumber = new AtomicIntegerExt(entity.getGoodsNumber());
			curActivity.setGoodsNum(atomicGoodsNumber);
			curActivity.setGoodsPrice(entity.getGoodsPrice());
			curActivity.setNumLimit(entity.getLimitNumber());
			curActivity.setStatus(entity.getStatus());
			curActivity.setPayDelay(entity.getPayDelay());
			curActivity.setDescribt(entity.getDescribt());
		} catch (Exception e) {
			logger.error("StartJob execute[entity2Info] exception...",e);
		}
	}

}
