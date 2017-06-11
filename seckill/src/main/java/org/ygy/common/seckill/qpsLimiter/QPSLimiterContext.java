package org.ygy.common.seckill.qpsLimiter;

import org.ygy.common.seckill.util.AtomicIntegerExt;


public class QPSLimiterContext {
	
	/**
	 *  每秒限制的最大请求数
	 */
	private static int qpsln;
	
	/**
	 * 多少个毫秒增加一次token数
	 */
	private static long intervalInMillis;

	/**
	 *  当前令牌数
	 */
	private static final AtomicIntegerExt tokenNum = new AtomicIntegerExt(0);

	/**
	 * 获取每秒限制的最大请求数
	 * @return
	 */
	public static int getQpsln() {
		return qpsln;
	}

	/**
	 * 设置每秒限制的最大请求数，不能小于100
	 * @param qpsln
	 */
	public static void setQpsln(int qpsln) {
		if (qpsln < 100) {
			QPSLimiterContext.qpsln = 100;
		} else {
			QPSLimiterContext.qpsln = qpsln;
		}
	}
	
	/**
	 * 增加指定token数
	 */
	public static void addToken(int delta) {
		tokenNum.addToMaxValueAtMost(delta, qpsln);
	}
	
	/**
	 * 是否有token，如果有同时减一
	 * @return
	 */
	public static boolean hasToken() {	
		if (tokenNum.getAndDecrementWhenGzero() > 0) {
			return true;
		}
		return false;
	}

	public static long getIntervalInMillis() {
		return intervalInMillis;
	}

	/**
	 * 设置增加token数的时间间隔
	 * @param intervalInMillis   值为小于等于1000的正整数
	 */
	public static void setIntervalInMillis(long intervalInMillis) {
		if (intervalInMillis <= 0 || intervalInMillis > 1000) {
			intervalInMillis = 10;
		}
		QPSLimiterContext.intervalInMillis = intervalInMillis;
	}

}
