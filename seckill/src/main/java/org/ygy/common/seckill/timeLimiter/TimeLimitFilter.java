package org.ygy.common.seckill.timeLimiter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.ygy.common.seckill.scheduler.ActivityInfo;
import org.ygy.common.seckill.scheduler.SchedulerContext;

public class TimeLimitFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		long currentTime = System.currentTimeMillis();
		ActivityInfo curActivityInfo = SchedulerContext.getCurActivityInfo();
		// 秒杀未开始，秒杀接口不允许访问
		if (null == curActivityInfo || currentTime < curActivityInfo.getStartTime()
				|| currentTime > curActivityInfo.getEndTime()) {
//			HttpServletRequest req = (HttpServletRequest)request;
//			String url = req.getServletPath();
//			System.out.println(url);
//			url = url.replaceAll("/{2,}", "/");
//			// 若为秒杀接口，提示用户秒杀未开始
//			if ("".equals(url)) {
//				request.getRequestDispatcher("").forward(request, response);
//				return;
//			}
			request.getRequestDispatcher("friendly/timeLimit").forward(request, response);
			return;
		} 
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
	
	@Override
	public void destroy() {}

}
