package org.ygy.common.seckill.dao;

import org.ygy.common.seckill.entity.SuccessLogEntity;

public interface SuccessLogDao {
    int deleteByPrimaryKey(String id);

    int insert(SuccessLogEntity record);

    int insertSelective(SuccessLogEntity record);

    SuccessLogEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SuccessLogEntity record);

    int updateByPrimaryKey(SuccessLogEntity record);
}