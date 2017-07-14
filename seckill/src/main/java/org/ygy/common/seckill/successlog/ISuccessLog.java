package org.ygy.common.seckill.successlog;

import java.util.Map;

public interface ISuccessLog {

	/**
	 * 保存用户秒杀到的商品数
	 * @param userId
	 * @param goodsNum
	 */
	void set(String userId, int goodsNum);
	
	/**
	 * 获取用户秒杀到的商品数
	 * @param userId
	 * @return fanhui
	 */
	int get(String userId);
	
	/**
	 * 清空秒杀成功记录
	 */
	void clearAll();
	
	/**
	 * 获取所有秒杀成功记录
	 * @return
	 */
	Map<String, Integer> getAll();
	
	void setGoodsNumerOfUserInActivity(String activityId, String userId, int goodsNum);
	
	int getGoodsNumberOfUserInActivity(String activityId, String userId);
	
	Map<String, Integer> getSuccLogInActivity(String activityId);
	
	boolean getHandledFlagFromActivitySuccLog(String activityId);
	
	/**
	 * 设置为已处理
	 * @param activityId
	 */
	void setHandledFlagForActivitySuccLog(String activityId);
}
