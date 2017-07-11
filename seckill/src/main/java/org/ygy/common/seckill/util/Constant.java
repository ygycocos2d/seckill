package org.ygy.common.seckill.util;

public class Constant {
	
	// 秒杀活动状态
	public static final String ACTIVITY_STATUS_START = "0";//启动
	public static final String ACTIVITY_STATUS_STOP = "1";//停止
	public static final String ACTIVITY_STATUS_DELETE = "2";//删除
	public static final String[] ACTIVITY_STATUS = {"0","1","2"};
	
	// 秒杀订单状态
	public static final String ORDER_STATUS_PAYING = "0";//待付款
	public static final String ORDER_STATUS_PAYED = "1";//已付款
	public static final String ORDER_STATUS_EXPIRE = "2";//已过期
	public static final String[] ORDER_STATUS = {"0","1","2"};
	
	public static interface Cache{
		// 缓存key前缀
		final String KEEP_ALIVE = "keepAlive_";
		final String GOODS_NUMBER = "goodsNumber_";
		final String SUCC_LOG = "succLog_";
		final String SUCC_HANDLED_FLAG = "succHandled_";
		final String COMED = "comed_";
		
		/**
		 *  秒杀succLog处理状态flag
		 *  1、保证只有一个应用实例处理succLog
		 *  2、当处理succLog的实例出现异常时（如宕机），可根据这些状态进行后续的人工处理（可以是一个管理应用）
		 */
		final String UNHANDLE = null;//未处理
		final String HANDLING = "1";//处理中
		final String HANDLED = "2";//已处理
	}
	
	//总开关类型
	public static final String SECKILL_SWITCH = "1";
}
