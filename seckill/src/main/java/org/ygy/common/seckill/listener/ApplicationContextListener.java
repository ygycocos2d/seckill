package org.ygy.common.seckill.listener;

import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ygy.common.seckill.scheduler.DealedHandlerJob;
import org.ygy.common.seckill.scheduler.KeepAliveJob;
import org.ygy.common.seckill.scheduler.MasterSwitchJob;
import org.ygy.common.seckill.scheduler.SchedulerContext;
import org.ygy.common.seckill.util.QuartzUtil;

public class ApplicationContextListener implements ServletContextListener {
	
	private Logger       logger = LoggerFactory.getLogger(ApplicationContextListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("The application start...");
		try {
			// 创建Quartz任务调度器，并启动
			SchedulerContext.setQuartzUtil(new QuartzUtil(new StdSchedulerFactory().getScheduler()));
			SchedulerContext.getQuartzUtil().start();
			logger.info("The quartz scheduler start...");
			
			// 启动集群中应用实例相互检测job
			SchedulerContext.getQuartzUtil().add(KeepAliveJob.class, "keepAliveJobId",
					"keepAliveJobIdGid", new Date(), 1000*SchedulerContext.getHeartbeat());
			SchedulerContext.getQuartzUtil().add(DealedHandlerJob.class, "dealedHandlerJobId",
					"dealedHandlerJobGid", new Date(), 1000*SchedulerContext.getHeartbeat());
			
			// 检测总开关是否开启job。(应该做成消息处理的形式--现在用循化定时任务模拟)
			SchedulerContext.getQuartzUtil().add(MasterSwitchJob.class, "masterSwitchJobId",
					"masterSwitchJobGid", new Date());
		} catch (SchedulerException e) {
			logger.error("The application start exception...", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("The application stop...");
		try {
			// Quartz shutdown
			SchedulerContext.getQuartzUtil().shutdown(false);;
			logger.info("The quartz scheduler stop...");
		} catch (Exception e) {
			logger.error("The application stop exception...", e);
		}
	}
}


