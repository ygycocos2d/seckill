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
	public void batchAdd(List<ActivityOrderRelationEntity> relationList) {
		// TODO Auto-generated method stub
		
	}

}
