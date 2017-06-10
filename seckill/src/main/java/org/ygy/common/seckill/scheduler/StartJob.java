package org.ygy.common.seckill.scheduler;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class StartJob implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		ActivityInfo info = SchedulerContext.getCurActivityInfo();
		
		// 获取图片等资源信息
		
		// 调度当前秒杀活动结束任务
		String name = info.getActivityId() + "_end";
		String group = info.getActivityGid() + "_end";
		Date date = new Date(info.getEndTime() + 1000);//活动结束一秒后执行
		SchedulerContext.getQuartzUtil().add(EndJob.class, name, group, date);
	}

}
