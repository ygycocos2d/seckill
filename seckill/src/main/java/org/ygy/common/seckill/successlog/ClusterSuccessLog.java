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
		RedisUtil.setHashMapValue(Constant.SUCC_LOG+activityId, userId, ""+goodsNum);
	}

	@Override
	public int getGoodsNumberOfUserInActivity(String activityId, String userId) {
		String num = RedisUtil.getHashMapValue(Constant.SUCC_LOG+activityId, userId);
		if (StringUtil.isEmpty(num)) {
			return 0;
		}
		return Integer.parseInt(num);
	}

	@Override
	public Map<String, Integer> getSuccLogInActivity(String activityId) {
		Map<String, Integer> succLog = new HashMap<String, Integer>();
		Set<Entry<String, String>> set = RedisUtil.getHashMap(Constant.SUCC_LOG+activityId).entrySet();
		for (Entry<String, String> en:set) {
			succLog.put(en.getKey(), Integer.parseInt(en.getValue()));
		}
		return succLog;
	}

}
