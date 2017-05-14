package org.ygy.common.seckill.dao;

import org.ygy.common.seckill.entity.TaskEntity;

public interface TaskDao {
    int deleteByPrimaryKey(String taskId);

    int insert(TaskEntity record);

    int insertSelective(TaskEntity record);

    TaskEntity selectByPrimaryKey(String taskId);

    int updateByPrimaryKeySelective(TaskEntity record);

    int updateByPrimaryKey(TaskEntity record);
}