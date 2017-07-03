package org.ygy.common.seckill.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.ygy.common.seckill.entity.ActivityEntity;
import org.ygy.common.seckill.successlog.ISuccessLog;
import org.ygy.common.seckill.util.ActivityQueue;
import org.ygy.common.seckill.util.FileUtil;
import org.ygy.common.seckill.util.QuartzUtil;
import org.ygy.common.seckill.util.StringUtil;

public class SchedulerContext {
	
	/*秒杀活动总开关*/
	private static boolean masterSwitch = false;
	/*当前进行中的秒杀活动信息*/
	private static ActivityInfo curActivityInfo;//
	/*当前应用各活动要处理的商品秒杀数，Map<活动ID, 商品秒杀数>*/
	private static Map<String, Integer> curAppHandleGoodsNum = new HashMap<String, Integer>();
	/*当前秒杀活动用户秒杀成功记录*/
	private static ISuccessLog sucLog;
	/*Quartz任务调度器管理工具*/
	private static QuartzUtil quartzUtil;
	/*秒杀活动队列*/
	private static ActivityQueue activityQueue = new ActivityQueue();
	/*心跳频率，单位秒，默认为1*/
	private static int heartbeat = 1;
	/*宕机检查时长，单位秒，默认为3*/
	private static int dumptime = 3;
	/*是否集群,默认false*/
	private static boolean cluster = false;
	/*当前应用实例号*/
	private static String appno;

	static {
		try {
			Properties pro = FileUtil.loadMultiProperties("conf/seckill.properties");
			// 是否集群
			String cluster = pro.getProperty("cluster");
			if (null != cluster && "yes".equals(cluster.trim())) {
				SchedulerContext.setCluster(true);;
			} 
			// 创建秒杀成功的记录的对象
			String classPath = "org.ygy.common.seckill.successlog.SingleAppSuccessLog";
			if (SchedulerContext.isCluster()) {
				classPath = "org.ygy.common.seckill.successlog.ClusterSuccessLog";
			} 
			try {
				Class<?> clazz = Class.forName(classPath);
				sucLog = (ISuccessLog) clazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 集群应用心跳检测相关设置参数获取
			if (SchedulerContext.isCluster()) {
				String appno = pro.getProperty("appno");
				String heartbeat = pro.getProperty("heartbeat");
				String dumptime = pro.getProperty("dumptime");
				if (StringUtil.isEmpty(appno)) {//appno必须有，没设置只能随机一个了
					appno = StringUtil.getUUID();
				}
				SchedulerContext.setAppno(appno);
				if (StringUtil.isEmpty(heartbeat)) {
					int  heartbeatInt = 0;
					try {
						heartbeatInt = Integer.parseInt(heartbeat);
						SchedulerContext.setHeartbeat(heartbeatInt);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (StringUtil.isEmpty(dumptime)) {
					int dumptimeInt = 0;
					try {
						dumptimeInt = Integer.parseInt(dumptime);
						SchedulerContext.setHeartbeat(dumptimeInt);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} 
			
			// 获取当前应用实例处理指定秒杀活动的商品数，这里只是模拟
			Properties pro2 = FileUtil.loadMultiProperties("conf/activity_goodsnumber.properties");
			Iterator<Entry<Object, Object>> it = pro2.entrySet().iterator();  
	        while (it.hasNext()) {  
	            Entry<Object, Object> entry = it.next();  
	            Object key = entry.getKey();  
	            Object value = entry.getValue(); 
	            curAppHandleGoodsNum.put(key.toString(), Integer.valueOf(value.toString()));
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
	
	public static void setCurAppHandleGoodsNum(String activityId, Integer number) {
		curAppHandleGoodsNum.put(activityId, number);
	}
	
	public static Integer getCurAppHandleGoodsNum(String activityId) {
		return curAppHandleGoodsNum.get(activityId);
	}

	public static ActivityInfo getCurActivityInfo() {
		return curActivityInfo;
	}

	public static void setCurActivityInfo(ActivityInfo curActivityInfo) {
		SchedulerContext.curActivityInfo = curActivityInfo;
	}

	public static boolean getMasterSwitch() {
		return masterSwitch;
	}

	public static void setMasterSwitch(boolean masterSwitch) {
		SchedulerContext.masterSwitch = masterSwitch;
	}

	public static void scheduleChainStart() {
		if (null == curActivityInfo) {
			if (!activityQueue.isEmpty()) {
				ActivityEntity entity = activityQueue.getHeaderNotDel();
				String name = entity.getActivityId() + "_start";
				String group = entity.getGroupId() + "_start";
				quartzUtil.add(StartJob.class, name, group, new Date());
			}
		}
	}

	public static ISuccessLog getSucLog() {
		return sucLog;
	}

	public static void setSucLog(ISuccessLog sucLog) {
		SchedulerContext.sucLog = sucLog;
	}  
	
	public static ActivityQueue getActivityQueue() {
		return activityQueue;
	}
	public static void setQuartzUtil(QuartzUtil quartzUtil) {
		SchedulerContext.quartzUtil = quartzUtil;
	}
	public static QuartzUtil getQuartzUtil() {
		return quartzUtil;
	}

	public static int getHeartbeat() {
		return heartbeat;
	}

	public static void setHeartbeat(int heartbeat) {
		SchedulerContext.heartbeat = heartbeat;
	}

	public static int getDumptime() {
		return dumptime;
	}

	public static void setDumptime(int dumptime) {
		SchedulerContext.dumptime = dumptime;
	}

	public static boolean isCluster() {
		return cluster;
	}

	public static void setCluster(boolean cluster) {
		SchedulerContext.cluster = cluster;
	}

	public static String getAppno() {
		return appno;
	}

	public static void setAppno(String appno) {
		SchedulerContext.appno = appno;
	}
}
