package org.ygy.common.seckill.qpsLimiter;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 以每十个毫秒均匀流畅的增加令牌数--定时任务job
 * @author ygy
 *
 */
public class TokenAdderJob implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		int qpsln = QPSLimiterContext.getQpsln();
		int intervalInMillis = new Long(QPSLimiterContext.getIntervalInMillis()).intValue();
		QPSLimiterContext.addToken(qpsln / (1000/intervalInMillis) );
	}
	
}
