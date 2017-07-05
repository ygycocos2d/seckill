package org.ygy.common.seckill.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.ygy.common.seckill.util.Constant;
import org.ygy.common.seckill.util.RedisUtil;

/**
 * 我们的任务是--活着！哦，死了无所谓！
 * @author ygy
 *
 */
public class KeepAliveJob implements Job{

	@Override
	public void execute(JobExecutionContext context)throws JobExecutionException {
		try {
			// 往缓存中置值，告诉其他应用实例当前应用实例还活着
			String aliveKey = Constant.KEEP_ALIVE+SchedulerContext.getAppno();
			RedisUtil.setEx(aliveKey,"1",SchedulerContext.getDumptime()); 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
