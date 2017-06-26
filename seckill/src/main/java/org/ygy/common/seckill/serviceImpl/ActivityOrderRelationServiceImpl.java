package org.ygy.common.seckill.serviceImpl;

import java.util.List;

import org.ygy.common.seckill.dao.ActivityOrderRelationDao;
import org.ygy.common.seckill.entity.ActivityOrderRelationEntity;
import org.ygy.common.seckill.service.ActivityOrderRelationService;

public class ActivityOrderRelationServiceImpl implements ActivityOrderRelationService{

	private ActivityOrderRelationDao relationDao;
	@Override
	public List<String> getOrderIdListByActivityId(String activityId) {
		return relationDao.selectOrderIdListByActivityId(activityId);
	}
	@Override
	public void addBatch(List<ActivityOrderRelationEntity> relationList) {
		if (null != relationList && relationList.isEmpty()) {
			this.relationDao.insertBatch(relationList);
		}
	}

}
