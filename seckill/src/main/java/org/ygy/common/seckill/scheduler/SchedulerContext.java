package org.ygy.common.seckill.scheduler;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Queue;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.BeanUtils;
import org.ygy.common.seckill.entity.ActivityEntity;
import org.ygy.common.seckill.successlog.ISuccessLog;
import org.ygy.common.seckill.util.FileUtil;
import org.ygy.common.seckill.util.MyJob;
import org.ygy.common.seckill.util.StringUtil;

public class SchedulerContext {
	
	private static boolean masterSwitch = false;//秒杀活动总开关
	
	private static ActivityInfo curActivityInfo;
	
	private static Scheduler scheduler; 
	
	private static Map<String, Integer> curAppHandleGoodsNum = new HashMap<String, Integer>();
	
	private static ISuccessLog sucLog;
	
	public static void setCurAppHandleGoodsNum(String activityId, Integer number) {
		curAppHandleGoodsNum.put(activityId, number);
	}
	
	public static Integer getCurAppHandleGoodsNum(String activityId) {
		return curAppHandleGoodsNum.get(activityId);
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
				setSucLog((ISuccessLog) clazz.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			scheduler = new StdSchedulerFactory().getScheduler();
			SchedulerContext.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}  
	}
	
	/**
	 * 关闭任务调度器、释放掉所有相关资源且不可恢复
	 * @param waitForJobsToComplete true-等当前所有正在执行的任务完成后再关闭，false-强制直接关闭
	 */
	public static void shutdown(boolean waitForJobsToComplete) {
		try {
			scheduler.shutdown(waitForJobsToComplete);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增任务和触发器并注册到调度中
	 * @param jobClass
	 * @param name
	 * @param group
	 * @param date  执行时间不能为过去时间
	 * @return
	 */
	public static boolean add(Class<? extends Job> jobClass, String name, String group, Date date) {
        try {
        	if (date.getTime() >= System.currentTimeMillis()) {
        		JobDetail job = JobBuilder.newJob(jobClass).withIdentity(name, group).build();  
        		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(date).build();      
            	//把作业和触发器注册到任务调度中  
    			Date resDate = scheduler.scheduleJob(job, trigger);
    			if (null != resDate) {
    				return true;
    			}
        	}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 更新指定任务的执行时间（替换掉就得Trigger）
	 * @param name
	 * @param group
	 * @param date  更新的时间不能为过去时间
	 * @return
	 */
	public static boolean update(String name, String group, Date date) {
		try {
			if (date.getTime() >= System.currentTimeMillis()) {
				TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
				Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(date).build();
				Date resDate = scheduler.rescheduleJob(triggerKey, trigger);//按新的trigger重新设置job执行
				if (null != resDate) {
					return true;
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 暂停指定任务
	 * @param name
	 * @param group
	 * @return
	 */
	public static boolean pause(String name, String group) {
		try {
			JobKey jobKey = JobKey.jobKey(name, group);
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 恢复被暂停的任务
	 * @param name
	 * @param group
	 * @return
	 */
	public static boolean resume(String name, String group) {
		try {
			JobKey jobKey = JobKey.jobKey(name, group);
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 删除任务后，所对应的trigger也将被删除
	 * @param name
	 * @param group
	 * @return
	 */
	public static boolean delete(String name, String group) {
		try {
			JobKey jobKey = JobKey.jobKey(name, group);
			return scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void start() {
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 
	 * @param jobClass
	 * @param name
	 * @param group
	 * @param intervalInMillis
	 */
	public static void scheduleAddTokenNumJob(Class<? extends Job> jobClass, String name, String group, long intervalInMillis) {
    	try {
    		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, group).build();
        	Trigger trigger = TriggerBuilder.newTrigger()
        			.withIdentity(name, group).withSchedule(SimpleScheduleBuilder.simpleSchedule()
        					.withIntervalInMilliseconds(intervalInMillis)
        	    	        .repeatForever()).startAt(new Date()).build();  
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {  
	    try {  
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
	    	
	    	JobDetail jobDetail = JobBuilder.newJob(MyJob.class).withIdentity("111", "1123").build();
	    	
	    	Trigger trigger = TriggerBuilder.newTrigger()
	    			.withIdentity("sfds", "dsfds").withSchedule(SimpleScheduleBuilder.simpleSchedule()
	    					.withIntervalInMilliseconds(1)
//	    	    	        .withIntervalInSeconds(1) 
	    	    	        .repeatForever()).startAt(new Date()).build();  
	    	
	    	scheduler.scheduleJob(jobDetail, trigger);
	        start();  
//	        start();
//	        start();
	    } catch(Exception e){  
	    	e.printStackTrace();  
	    }  
//		List<String> usualBuyGoodsIdList = new ArrayList<String>();
//		List<String> goodsIdList = new ArrayList<String>();
//		
//		usualBuyGoodsIdList.add("1");
//		usualBuyGoodsIdList.add("2");
//		usualBuyGoodsIdList.add("3");
//		usualBuyGoodsIdList.add("4");
//		usualBuyGoodsIdList.add("5");
//		usualBuyGoodsIdList.add("6");
//		usualBuyGoodsIdList.add("7");
//		usualBuyGoodsIdList.add("8");
//		usualBuyGoodsIdList.add("9");
//		usualBuyGoodsIdList.add("10");
//		
//		goodsIdList.add("1");
//		goodsIdList.add("6");
////		goodsIdList.add("2");
////		goodsIdList.add("3");
////		goodsIdList.add("8");
//		goodsIdList.add("4");
//		goodsIdList.add("5");
//		goodsIdList.add("10");
////		goodsIdList.add("7");
////		goodsIdList.add("9");
//		
//		System.out.println(usualBuyGoodsIdList);
//		System.out.println(goodsIdList);
//		
//		usualBuyGoodsIdList.retainAll(goodsIdList);
//		
//		System.out.println(usualBuyGoodsIdList);
	         
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
		ActivityEntity entity = ActivityQueue.getHeader();
		ActivityInfo curActivity = new ActivityInfo();
		
		//这里还得好好写
//		BeanUtils.copyProperties(entity, curActivity);
		SchedulerContext.curActivityInfo = curActivity;
		
		String name = curActivity.getActivityId() + "_start";
		String group = curActivity.getActivityGid() + "_start";
		add(StartJob.class, name, group, new Date());
		
	}

	public static ISuccessLog getSucLog() {
		return sucLog;
	}

	public static void setSucLog(ISuccessLog sucLog) {
		SchedulerContext.sucLog = sucLog;
	}  
}
