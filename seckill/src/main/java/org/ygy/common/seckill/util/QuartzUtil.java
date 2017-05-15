package org.ygy.common.seckill.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	

	public static void main(String[] args) {  
	    try {  
	    	add(MyJob.class, "job1", "group1", 
	    			new Date(System.currentTimeMillis()+10*1000));
	    	
	    	Thread.sleep(5*1000);
	    	
//	    	pause("job1", "group1");
	    	
	    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	Date date = dateFormat.parse("2017-5-15 22:30:10");
	    	update("job1", "group1",date);
	    	
	    	
	        scheduler.start();               
	    } catch(Exception e){  
	    	e.printStackTrace();  
	    }  
	         
	}  
}
