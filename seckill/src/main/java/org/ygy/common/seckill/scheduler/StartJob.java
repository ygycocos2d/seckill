package org.ygy.common.seckill.scheduler;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.ygy.common.seckill.entity.ImgEntity;
import org.ygy.common.seckill.service.ImgService;

public class StartJob implements Job {
	
	@Resource
	private ImgService imgService;

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		ActivityInfo info = SchedulerContext.getCurActivityInfo();
		if (null != info) {
			// 获取图片等资源信息
			List<ImgEntity> imgList = this.imgService.getImgListByActivityId(info.getActivityId());
			info.setImgList(imgList);
			// 调度当前秒杀活动结束任务
			String name = info.getActivityId() + "_end";
			String group = info.getActivityGid() + "_end";
			Date date = new Date(info.getEndTime());
			SchedulerContext.getQuartzUtil().add(EndJob.class, name, group, date);
		}
	}

}
