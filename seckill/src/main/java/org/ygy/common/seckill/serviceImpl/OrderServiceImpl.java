package org.ygy.common.seckill.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.ygy.common.seckill.dao.OrderDao;
import org.ygy.common.seckill.entity.OrderEntity;
import org.ygy.common.seckill.service.OrderService;

public class OrderServiceImpl implements OrderService {
	
	@Resource
	private OrderDao orderDao;

	@Override
	public void batchAddOrder(List<OrderEntity> orderList) {
		if (null != orderList && orderList.size() > 0) {
			
		}
	}

}
