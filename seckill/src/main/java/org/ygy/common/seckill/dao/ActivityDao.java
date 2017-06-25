package org.ygy.common.seckill.dao;

import java.util.List;
import java.util.Map;

import org.ygy.common.seckill.entity.ActivityEntity;

public interface ActivityDao {
    int deleteByPrimaryKey(String activityId);

    int insert(ActivityEntity record);

    int insertSelective(ActivityEntity record);

    ActivityEntity selectByPrimaryKey(String activityId);

    int updateByPrimaryKeySelective(ActivityEntity record);

    int updateByPrimaryKey(ActivityEntity record);

	ActivityEntity getNotDelActivityById(String id);

	List<ActivityEntity> getAllEffectiveActivity();

	ActivityEntity getByTime(Map<String, Object> param);
}