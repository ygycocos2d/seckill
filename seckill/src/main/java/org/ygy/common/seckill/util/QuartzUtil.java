package org.ygy.common.seckill.util;

import java.util.Date;

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

/**
 * 动态操作Quartz任务调度的工具类
 * @author ygy
 *
 */
public class QuartzUtil {
	
	private Scheduler scheduler;

	public QuartzUtil(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	/**
	 * 关闭任务调度器、释放掉所有相关资源且不可恢复
	 * @param waitForJobsToComplete true-等当前所有正在执行的任务完成后再关闭，false-强制直接关闭
	 */
	public void shutdown(boolean waitForJobsToComplete) {
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
	 * @param date  
	 * @return
	 */
	public boolean add(Class<? extends Job> jobClass, String name, String group, Date date) {
		System.out.println(name+"---start");
        try {
        	JobDetail job = JobBuilder.newJob(jobClass).withIdentity(name, group).build();  
        	Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(date).build();      
            //把作业和触发器注册到任务调度中  
    		Date resDate = scheduler.scheduleJob(job, trigger);
    		if (null != resDate) {
    			System.out.println(name+"---end");
    			return true;
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
	public boolean update(String name, String group, Date date) {
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
	public boolean pause(String name, String group) {
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
	public boolean resume(String name, String group) {
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
	public boolean delete(String name, String group) {
		try {
			JobKey jobKey = JobKey.jobKey(name, group);
			return scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void start() {
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
	 * @param date
	 * @param intervalInMillis
	 */
	public void add(Class<? extends Job> jobClass, String name, String group, Date date, long intervalInMillis) {
    	try {
    		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, group).build();
        	Trigger trigger = TriggerBuilder.newTrigger()
        			.withIdentity(name, group).withSchedule(SimpleScheduleBuilder.simpleSchedule()
        					.withIntervalInMilliseconds(intervalInMillis)
        	    	        .repeatForever()).startAt(date).build();  
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
