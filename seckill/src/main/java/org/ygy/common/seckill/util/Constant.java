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
	
	// 缓存key前缀
	public static final String KEEP_ALIVE = "keepAlive_";
	public static final String GOODS_NUMBER = "goodsNumber_";
	public static final String SUCC_LOG = "succLog_";
	public static final String COMED = "comed_";
}
