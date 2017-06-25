package org.ygy.common.seckill.dao;

import java.util.Map;

import org.ygy.common.seckill.entity.OrderEntity;

public interface OrderDao {
    int deleteByPrimaryKey(String orderId);

    int insert(OrderEntity record);

    int insertSelective(OrderEntity record);

    OrderEntity selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(OrderEntity record);

    int updateByPrimaryKey(OrderEntity record);

	int updateStatusByOrderIdList(Map<String, Object> param);
}