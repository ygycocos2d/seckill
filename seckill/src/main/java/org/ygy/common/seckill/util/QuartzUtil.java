package org.ygy.common.seckill.util;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzUtil {
	
	private static Scheduler scheduler=null; 
	
	static {
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}  
	}
	
	public static boolean shutdown() {
		
		return true;
	}
	/**
	 * 新增任务和触发器并注册到调度中
	 * @param jobClass
	 * @param name
	 * @param group
	 * @param date
	 * @return
	 */
	public static boolean add(Class<? extends Job> jobClass, String name, String group, Date date) {
        try {
        	JobDetail job = JobBuilder.newJob(jobClass).withIdentity(name, group).build();  
    		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(date).build();      
        	//把作业和触发器注册到任务调度中  
			Date resDate = scheduler.scheduleJob(job, trigger);
			if (null == resDate) {
				return false;
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 更新指定任务的执行时间（替换掉就得Trigger）
	 * @param name
	 * @param group
	 * @param date
	 * @return
	 */
	public static boolean update(String name, String group, Date date) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(date).build();
			Date resDate = scheduler.rescheduleJob(triggerKey, trigger);//按新的trigger重新设置job执行
			if (null == resDate) {
				return false;
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 暂停指定任务
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public static boolean pause(String jobName, String jobGroup) {
		try {
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 恢复被暂停的任务
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public static boolean resume(String jobName, String jobGroup) {
		try {
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 删除任务后，所对应的trigger也将被删除
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public static boolean delete(String jobName, String jobGroup) {
		try {
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
			return scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	

	public static void main(String[] args) {  
	     
	     //通过schedulerFactory获取一个调度器  
	       SchedulerFactory schedulerfactory=new StdSchedulerFactory();  
	       Scheduler scheduler=null;  
	       try{  
//	      通过schedulerFactory获取一个调度器  
	           scheduler=schedulerfactory.getScheduler();  
	             
//	       创建jobDetail实例，绑定Job实现类  
//	       指明job的名称，所在组的名称，以及绑定job类  
	           JobDetail job=JobBuilder.newJob(MyJob.class).withIdentity("job1", "jgroup1").build();  
//	           
//	             
////	       定义调度触发规则  
//	             
////	      使用simpleTrigger规则  
//	           Trigger trigger=TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "triggerGroup")  
//	        		   .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withRepeatCount(8))  
//	        		   .startNow().build();  
	        Trigger trigger=TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "triggerGroup")  
	                        .startAt(new Date(System.currentTimeMillis() + 2000)).build();  
////	      使用cornTrigger规则  每天10点42分  
//	              Trigger trigger=TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "triggerGroup")  
//	              .withSchedule(CronScheduleBuilder.cronSchedule("0 42 10 * * ? *"))  
//	              .startNow().build();   
//	             
//	       把作业和触发器注册到任务调度中  
	           scheduler.scheduleJob(job, trigger);  
	             
//	       启动调度  
	           scheduler.start();  
//	             
//	             
	       }catch(Exception e){  
	           e.printStackTrace();  
	       }  
	         
	}  
}
