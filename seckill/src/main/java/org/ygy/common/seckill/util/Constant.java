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
		final String KEEP_ALIVE = "keepAlive:";
		final String GOODS_NUMBER = "goodsNumber:";
		final String SUCC_LOG = "succLog:";
		final String SUCC_HANDLED_FLAG = "succHandled:";
		final String START_COMED = "startComed:";//活动开始，各应用实例平分商品数记录缓存前缀
		final String DUMP_COMED = "dumpComed:";//某实例应用dump机，其他活着的应用实例平分其商品数记录缓存前缀
		final String LOCK = "lock:";
		
		
		final String ALIVE_APP_MAP = "aliveAppMap";
	}
	
	//总开关类型
	public static final String SECKILL_SWITCH = "1";
}
