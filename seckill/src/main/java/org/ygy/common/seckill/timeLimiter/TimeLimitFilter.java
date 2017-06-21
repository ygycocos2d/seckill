package org.ygy.common.seckill.timeLimiter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.ygy.common.seckill.scheduler.ActivityInfo;
import org.ygy.common.seckill.scheduler.SchedulerContext;

public class TimeLimitFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// 未登录用户，秒杀接口不允许访问
		HttpServletRequest req = (HttpServletRequest)request;
		HttpSession session = req.getSession(false);
		if (null == session || null == session.getAttribute("user")) {
			request.getRequestDispatcher("/friendly/noLogin").forward(request, response);
			return;
		}
		// 秒杀未开始，秒杀接口不允许访问
		long currentTime = System.currentTimeMillis();
		ActivityInfo curActivityInfo = SchedulerContext.getCurActivityInfo();
		if (null == curActivityInfo || currentTime < curActivityInfo.getStartTime()
				|| currentTime > curActivityInfo.getEndTime()) {
			request.getRequestDispatcher("/friendly/timeLimit").forward(request, response);
			return;
		} 
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
	
	@Override
	public void destroy() {}

}
