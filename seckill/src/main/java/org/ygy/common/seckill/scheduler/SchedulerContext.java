package org.ygy.common.seckill.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.util.Assert;
import org.ygy.common.seckill.entity.ActivityEntity;
import org.ygy.common.seckill.successlog.ISuccessLog;
import org.ygy.common.seckill.util.ActivityQueue;
import org.ygy.common.seckill.util.AtomicIntegerExt;
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

	public static void main(String[] args) {  
//	    try {  
//	    	add(MyJob.class, "job1", "group1", 
//	    			new Date(System.currentTimeMillis()-10*1000));
//	    	System.out.println(new Date(System.currentTimeMillis()));
//	    	Thread.sleep(5*1000);
//	    	
////	    	pause("job1", "group1");
//	    	
//	    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	    	Date date = dateFormat.parse("2017-5-16 9:30:10");
//	    	System.out.println(update("job1", "group1",date));
//	    	scheduler.pauseAll();
	    	
	    	
//	    	SimpleTriggerImpl trigger=new SimpleTriggerImpl("trigger1","group1");
//	    	trigger.setStartTime(new Date());
//	    	trigger.setRepeatInterval(1);
//	    	trigger.setRepeatCount(-1);
	    	
//	    	JobDetail jobDetail = JobBuilder.newJob(MyJob.class).withIdentity("111", "1123").build();
//	    	
//	    	Trigger trigger = TriggerBuilder.newTrigger()
//	    			.withIdentity("sfds", "dsfds").withSchedule(SimpleScheduleBuilder.simpleSchedule()
//	    					.withIntervalInMilliseconds(1)
////	    	    	        .withIntervalInSeconds(1) 
//	    	    	        .repeatForever()).startAt(new Date()).build();  
//	    	
//	    	scheduler.scheduleJob(jobDetail, trigger);
//	        start();  
//
//	    } catch(Exception e){  
//	    	e.printStackTrace();  
//	    }  
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
			ActivityEntity entity = activityQueue.getHeader();
			if (null != entity) {
				ActivityInfo curActivity = new ActivityInfo();
				entity2Info(entity, curActivity);
				SchedulerContext.curActivityInfo = curActivity;
				String name = curActivity.getActivityId() + "_start";
				String group = curActivity.getActivityGid() + "_start";
				quartzUtil.add(StartJob.class, name, group, new Date());
			}
		}
	}

	private static void entity2Info(ActivityEntity entity, ActivityInfo curActivity) {
		Assert.notNull(entity, "");
		Assert.notNull(curActivity, "");
//		if () {
//			
//		}
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
		curActivity.setTaskDescribt(entity.getDescribt());
	}
	
	public static ISuccessLog getSucLog() {
		return sucLog;
	}

	public static void setSucLog(ISuccessLog sucLog) {
		SchedulerContext.sucLog = sucLog;
	}  
}
