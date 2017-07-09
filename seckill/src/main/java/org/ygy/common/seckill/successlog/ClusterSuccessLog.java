package org.ygy.common.seckill.successlog;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ygy.common.seckill.util.Constant;
import org.ygy.common.seckill.util.RedisUtil;
import org.ygy.common.seckill.util.StringUtil;

public class ClusterSuccessLog implements ISuccessLog {

	@Override
	public void set(String userId, int goodsNum) {
		RedisUtil.set(userId, ""+goodsNum);
	}

	@Override
	public int get(String userId) {
		String valueStr = RedisUtil.get(userId);
		if (null != valueStr && "".equals(valueStr.trim())) {
			return Integer.parseInt(valueStr);
		}
		return 0;
	}

	@Override
	public void clearAll() {
		RedisUtil.clearAll();
	}

	@Override
	public Map<String, Integer> getAll() {
		Map<String, Integer> successLogMap = new HashMap<String, Integer>();
		Map<String, String> map = RedisUtil.getAll();
		Set<Entry<String, String>> set = map.entrySet();
		for (Entry<String, String> entry : set) {
			successLogMap.put(entry.getKey(), Integer.parseInt(entry.getValue()));
		}
		return successLogMap;
	}

	@Override
	public void setGoodsNumerOfUserInActivity(String activityId, String userId, int goodsNum) {
		RedisUtil.setHashMapValue(Constant.Cache.SUCC_LOG+activityId, userId, ""+goodsNum);
	}

	@Override
	public int getGoodsNumberOfUserInActivity(String activityId, String userId) {
		String num = RedisUtil.getHashMapValue(Constant.Cache.SUCC_LOG+activityId, userId);
		if (StringUtil.isEmpty(num)) {
			return 0;
		}
		return Integer.parseInt(num);
	}

	@Override
	public Map<String, Integer> getSuccLogInActivity(String activityId) {
		Map<String, Integer> succLog = new HashMap<String, Integer>();
		Set<Entry<String, String>> set = RedisUtil.getHashMap(Constant.Cache.SUCC_LOG+activityId).entrySet();
		for (Entry<String, String> en:set) {
			succLog.put(en.getKey(), Integer.parseInt(en.getValue()));
		}
		return succLog;
	}

	@Override
	public boolean getHandledFlagFromActivitySuccLog(String activityId) {
		String handled = RedisUtil.getAndSet(Constant.Cache.SUCC_HANDLED_FLAG+activityId, Constant.Cache.HANDLING);
		if (Constant.Cache.HANDLED.equals(handled)) {//旧值为已处理，重设为已处理
			RedisUtil.set(Constant.Cache.SUCC_HANDLED_FLAG+activityId, Constant.Cache.HANDLED);
			return true;
		}
		if (Constant.Cache.HANDLING.equals(handled)) {//旧值为处理中，说明正在被其他实例处理
			return true;
		}
		return false;
	}

//	@Override
//	public void setHandledFlagForActivitySuccLog(String activityId) {
//		RedisUtil.setHashMapValue(Constant.SUCC_HANDLED_FLAG, activityId, "1");
//	}

}
