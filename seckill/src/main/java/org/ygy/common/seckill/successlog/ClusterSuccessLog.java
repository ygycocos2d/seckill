package org.ygy.common.seckill.successlog;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ygy.common.seckill.util.RedisUtil;

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

}
