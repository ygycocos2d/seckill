package org.ygy.common.seckill.qpsLimiter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.ygy.common.seckill.scheduler.SchedulerContext;

/**
 * qps限流器
 * @author ygy
 *
 */
public class QPSLimiterFilter implements Filter{
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (QPSLimiterContext.hasToken()) {
			chain.doFilter(request, response);
		}
		request.getRequestDispatcher("friendly/accessLimit").forward(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 获取配置的最大访问数/每秒
        String qpslnStr = filterConfig.getInitParameter("qpsln");
        int qpsln = 600;//默认600
        if (null != qpslnStr && !"".equals(qpslnStr.trim())) {
        	qpsln = Integer.parseInt(qpslnStr);
        }
        String intervalInMillisStr = filterConfig.getInitParameter("qpsln");
        int intervalInMillis = 10;//每隔10个毫秒更新令牌数
        if (null != intervalInMillisStr && !"".equals(intervalInMillisStr.trim())) {
        	intervalInMillis = Integer.parseInt(intervalInMillisStr);
        }
        QPSLimiterContext.setQpsln(qpsln);
        QPSLimiterContext.setQpsln(intervalInMillis);
        
        /**
         * 增加token定时增加任务到调度器
         * 每intervalInMillis个毫秒执行一次
         */
//        SchedulerContext.scheduleAddTokenNumJob(TokenAdderJob.class, "addTokenJob", "addTokenGroup", intervalInMillis);
	}
	
	@Override
	public void destroy() {}

}
