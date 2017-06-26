package org.ygy.common.seckill.service;

import java.util.List;
import java.util.Map;

import org.ygy.common.seckill.entity.OrderEntity;

public interface OrderService {

	void addOrderBatch(List<OrderEntity> orderList);

	void autoCancelOrderList(List<OrderEntity> orderList);

	OrderEntity getOrderByIdAndUserId(String orderId, String userId);

	void update(OrderEntity order);

	List<OrderEntity> getByOrderIdListAndStatus(Map<String, Object> param);

}
