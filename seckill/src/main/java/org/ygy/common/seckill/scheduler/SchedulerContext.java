package org.ygy.common.seckill.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.ygy.common.seckill.entity.ActivityEntity;
import org.ygy.common.seckill.successlog.ISuccessLog;
import org.ygy.common.seckill.util.ActivityQueue;
import org.ygy.common.seckill.util.FileUtil;
import org.ygy.common.seckill.util.QuartzUtil;

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
	
	public static ActivityQueue getActivityQueue() {
		return activityQueue;
	}
	public static QuartzUtil getQuartzUtil() {
		return quartzUtil;
	}

	static {
		try {
			Properties pro = FileUtil.loadMultiProperties("conf/seckill.properties");
			String cluster = pro.getProperty("cluster");
			String classPath = "org.ygy.common.seckill.successlog.SingleAppSuccessLog";
			if (null != cluster && "yes".equals(cluster.trim())) {
				classPath = "org.ygy.common.seckill.successlog.ClusterSuccessLog";
			} 
			try {
				Class<?> clazz = Class.forName(classPath);
				sucLog = (ISuccessLog) clazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 创建Quartz任务调度器，并启动
			quartzUtil = new QuartzUtil(new StdSchedulerFactory().getScheduler());
			quartzUtil.start();
		} catch (SchedulerException e) {
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
}
