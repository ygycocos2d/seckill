package org.ygy.common.seckill.service;

import java.util.List;
import java.util.Map;

import org.ygy.common.seckill.entity.OrderEntity;

public interface OrderService {

	void batchAddOrder(List<OrderEntity> orderList);

	List<OrderEntity> getByOrderIdListAndStatus(Map<String, Object> param);

	void autoCancelOrderList(List<OrderEntity> orderList);

}
