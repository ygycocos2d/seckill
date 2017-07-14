package org.ygy.common.seckill.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ygy.common.seckill.util.Constant;
import org.ygy.common.seckill.util.RedisUtil;

/**
 * 我们的任务是--活着！哦，死了无所谓！
 * 两个缓存结构：
 * 1、keepAlive_+appno：1(设置过期时间)
 * 
 * 2、aliveAppMap：Map<appno,1>
 * 
 * @author ygy
 *
 */
public class KeepAliveJob implements Job{
	
	private static Logger       logger = LoggerFactory.getLogger(KeepAliveJob.class);

	@Override
	public void execute(JobExecutionContext context)throws JobExecutionException {
		try {
			// 往缓存中置值，告诉其他应用实例当前应用实例还活着
			String aliveKey = Constant.Cache.KEEP_ALIVE+SchedulerContext.getAppno();
			RedisUtil.setEx(aliveKey,"1",SchedulerContext.getDumptime()); 
			RedisUtil.setHashMapValue(Constant.Cache.ALIVE_APP_MAP, SchedulerContext.getAppno(), "1");
		}catch(Exception e) {
			logger.error("KeepAliveJob execute exception...", e);
		}
	}
	
	/**
	 * 获取集群中应用实例存活状态
	 * @return Map
	 * key-aliveMap:Map<appno,是否活着>
	 * key-aliveNum:活着的应用数
	 */
	public static Map<String,Object> getAliveApp() {
		Map<String,Object> result = new HashMap<String, Object>(); 
		Map<String, Boolean> aliveMap = new HashMap<String, Boolean>();//Map<appno,是否活着>
    	int aliveNum = 0;//活着的应用数
    	try {
    		Map<String, String> map = RedisUtil.getHashMap(Constant.Cache.ALIVE_APP_MAP);
    		List<String> keyList = new ArrayList<String>();
    		for(Entry<String, String> entry:map.entrySet()) {
    			keyList.add(Constant.Cache.KEEP_ALIVE+entry.getKey());
    		}
    		String[] keys = keyList.toArray(new String[keyList.size()]);
    		
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
		} catch (Exception e) {
			logger.error("KeepAliveJob getAliveApp exception...", e);
		}
    	result.put("aliveMap", aliveMap);
    	result.put("aliveNum", aliveNum);
    	return result;
	}
}
