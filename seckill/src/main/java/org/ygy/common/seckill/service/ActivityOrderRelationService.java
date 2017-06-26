package org.ygy.common.seckill.service;

import java.util.List;

import org.ygy.common.seckill.entity.ActivityOrderRelationEntity;

public interface ActivityOrderRelationService {

	List<String> getOrderIdListByActivityId(String activityId);

	void batchAdd(List<ActivityOrderRelationEntity> relationList);

}
