package org.ygy.common.seckill.successlog;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingleAppSuccessLog implements ISuccessLog {

	private ConcurrentHashMap<String,Integer> successLog = new ConcurrentHashMap<String,Integer>();
	
	@Override
	public void set(String userId, int goodsNum) {
		if (null == userId || "".equals(userId.trim()) || goodsNum <= 0) {
			return;
		}
		successLog.put(userId, goodsNum);
	}
	
	@Override
	public int get(String userId) {
		int goodsNum = 0;
		if (null == userId || "".equals(userId.trim())) {
			goodsNum = -1;
		} else {
			Integer num = successLog.get(userId);
			if (null != num) {
				goodsNum = num.intValue();
			}
		}
		return goodsNum;
	}

	@Override
	public void clearAll() {
//		successLog.clear();
		successLog = new ConcurrentHashMap<String,Integer>();
	}

	@Override
	public Map<String, Integer> getAll() {
		return successLog;
	}

	@Override
	public void setGoodsNumerOfUserInActivity(String activityId, String userId, int goodsNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getGoodsNumberOfUserInActivity(String activityId, String userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, Integer> getSuccLogInActivity(String activityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getHandledFlagFromActivitySuccLog(String activityId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setHandledFlagForActivitySuccLog(String activityId) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void setHandledFlagForActivitySuccLog(String activityId) {
//		// TODO Auto-generated method stub
//		
//	}
}
