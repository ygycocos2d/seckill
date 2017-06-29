package org.ygy.common.seckill.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 说明：应用实例是否dump机检测，频率为默认1秒，总开关开启后且为集群部署启动该定时任务
 * 策略:
 * 	1、活着；当前应用实例通过每秒在缓存（redis）中更新时间，表示自己还活着，通过获取其他应用实例的redis中的该部分数据，可以判断其他实例是否活着。
 * 	2、当前实例over了，那没办法了。
 * 	3、其他实例over了（redis上的时间比当前时间少了3秒，我们认为相应应用实例dump），
 * 该部分数据在缓存中的格式：
 * 	
 * @author ygy
 *
 */
public class KeepAliveJob implements Job{

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		
	}

}
