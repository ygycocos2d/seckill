package org.ygy.common.seckill.scheduler;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.ygy.common.seckill.entity.ActivityEntity;
import org.ygy.common.seckill.service.ActivityService;
import org.ygy.common.seckill.service.SwitchService;
import org.ygy.common.seckill.util.Constant;
import org.ygy.common.seckill.util.SpringContextUtil;
import org.ygy.common.seckill.util.StringUtil;

public class MasterSwitchJob implements Job{
	
	private static String GID = "masterSwitch";
	
	private ActivityService activityService;
	private SwitchService switchService;
	
	public MasterSwitchJob() {
		try {
			activityService = (ActivityService) SpringContextUtil.getBeanByClass(ActivityService.class);
			switchService = (SwitchService) SpringContextUtil.getBeanByClass(SwitchService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println(new Date(System.currentTimeMillis()));
		try {
			// 获取总开关状态
			String status = switchService.getStatusByType(Constant.SECKILL_SWITCH);
			if ("1".equals(status)) {
				// 获取有效的秒杀活动
				List<ActivityEntity> activityList = this.activityService.getAllEffectiveActivity();
				if (null != activityList && !activityList.isEmpty()) {
					// 将有效的秒杀活动放入优先级队列
					SchedulerContext.getActivityQueue().addAll(activityList);
					// 秒杀活动定时调度链启动
					SchedulerContext.scheduleChainStart();
				} else {
					System.out.println("总开关已开启，但是没有任何有效的秒杀活动！");
				}
			} else {
				Date date = new Date(System.currentTimeMillis() + 3*1000);
				SchedulerContext.getQuartzUtil().add(MasterSwitchJob.class,
						StringUtil.getUUID(), GID, date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
