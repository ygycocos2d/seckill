package org.ygy.common.seckill.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;
import org.ygy.common.seckill.dao.ActivityGoodsInventoryLogDao;
import org.ygy.common.seckill.dao.GoodsDao;
import org.ygy.common.seckill.dao.OrderDao;
import org.ygy.common.seckill.entity.ActivityGoodsInventoryLogEntity;
import org.ygy.common.seckill.entity.GoodsEntity;
import org.ygy.common.seckill.entity.OrderEntity;
import org.ygy.common.seckill.service.OrderService;
import org.ygy.common.seckill.util.StringUtil;

public class OrderServiceImpl implements OrderService {
	
	@Resource
	private OrderDao orderDao;
	
	@Resource
	private GoodsDao goodsDao;
	
	@Resource
	private ActivityGoodsInventoryLogDao inventoryLogDao;

	@Override
	public void addOrderBatch(List<OrderEntity> orderList) {
		if (null != orderList && !orderList.isEmpty()) {
			this.orderDao.insertOrderBatch(orderList);
		}
	}

	@Override
	public List<OrderEntity> getByOrderIdListAndStatus(Map<String, Object> param) {
		return this.orderDao.selectByOrderIdListAndStatus(param);
	}

	@Override
	public void cancelOrderListAuto(List<OrderEntity> orderList,String activityId) {
		if (null != orderList && !orderList.isEmpty()) {
			int goodsNumber = 0;
			List<String> orderIdList = new ArrayList<String>();
			for (OrderEntity order:orderList) {
				goodsNumber += order.getGoodsNumber();
				orderIdList.add(order.getOrderId());
			}
//			Map<String, Object> param = new HashMap<String, Object>();
//			param.put("orderList", orderList);
			int count = this.orderDao.cancelOrderListAuto(orderIdList);
			if (count != orderIdList.size()) {
				throw new RuntimeException("自动取消秒杀活动相关订单数与期望不符");
			}
			String goodsId = orderList.get(0).getGoodsId();
			GoodsEntity goods = this.goodsDao.selectByPrimaryKey(goodsId);
			goods.setGoodsInventory(goods.getGoodsInventory() + goodsNumber);
			this.goodsDao.updateByPrimaryKey(goods);
			ActivityGoodsInventoryLogEntity inventoryLog = new ActivityGoodsInventoryLogEntity();
			inventoryLog.setId(StringUtil.getUUID());
			inventoryLog.setActivityId(activityId);
			inventoryLog.setGoodsId(goodsId);
			inventoryLog.setGoodsInventory(goodsNumber);
			inventoryLog.setDescribt("活动-->商品,活动订单超时未支付自动取消");
			inventoryLog.setCreateTime(new Date());
			this.inventoryLogDao.insertSelective(inventoryLog);
		}
	}

	@Override
	public OrderEntity getOrderByIdAndUserId(String orderId, String userId) {
		if (StringUtils.isEmpty(orderId) || StringUtils.isEmpty(userId)) {
			return null;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orderId", orderId);
		param.put("userId", userId);
		return this.orderDao.selectOrderByIdAndUserId(param);
	}

	@Override
	public void update(OrderEntity order) {
		this.orderDao.updateByPrimaryKeySelective(order);
	}

}
