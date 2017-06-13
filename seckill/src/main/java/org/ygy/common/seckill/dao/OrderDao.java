package org.ygy.common.seckill.dao;

import org.ygy.common.seckill.entity.OrderEntity;

public interface OrderDao {
    int insert(OrderEntity record);

    int insertSelective(OrderEntity record);
}