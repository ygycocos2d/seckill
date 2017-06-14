package org.ygy.common.seckill.service;

import java.util.List;

import org.ygy.common.seckill.entity.OrderEntity;

public interface OrderService {

	void batchAddOrder(List<OrderEntity> orderList);

}
