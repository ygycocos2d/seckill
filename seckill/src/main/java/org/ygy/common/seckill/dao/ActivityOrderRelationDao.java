package org.ygy.common.seckill.dao;

import java.util.List;

import org.ygy.common.seckill.entity.ActivityOrderRelationEntity;

public interface ActivityOrderRelationDao {
	
    int deleteByPrimaryKey(ActivityOrderRelationEntity key);

    int insert(ActivityOrderRelationEntity record);

    int insertSelective(ActivityOrderRelationEntity record);

	List<String> selectOrderIdListByActivityId(String activityId);
}