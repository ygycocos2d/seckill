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
}
