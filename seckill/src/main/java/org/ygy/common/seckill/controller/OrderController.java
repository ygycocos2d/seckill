package org.ygy.common.seckill.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ygy.common.seckill.dto.OrderDTO;
import org.ygy.common.seckill.entity.GoodsEntity;
import org.ygy.common.seckill.entity.OrderEntity;
import org.ygy.common.seckill.entity.UserEntity;
import org.ygy.common.seckill.service.GoodsService;
import org.ygy.common.seckill.service.OrderService;
import org.ygy.common.seckill.util.Constant;

@Controller
@RequestMapping("order")
public class OrderController {
	
	private Logger       logger = LoggerFactory.getLogger(OrderController.class);
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private GoodsService goodsService;

	/**
	 * 
	 * @param status 0-创建，1-已支付，2-过期
	 * @return
	 */
	@RequestMapping("getOrderListByStatus")
	@ResponseBody
	public Map<String,Object> getOrderListByStatus(HttpServletRequest request,String status) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
//			HttpSession session = request.getSession(false);
//			if (null != session && null != session.getAttribute("user")){
//				UserEntity user = (UserEntity)(session.getAttribute("user"));
			if(true) {
//				List<OrderEntity> orderList = this.orderService.getOrderListByUserIdAndStatus(user.getUserId(),status);
				
				List<OrderEntity> orderList = this.orderService.getOrderListByUserIdAndStatus("b1c7e5b00758419aa3816dab26059e0b",status);
				List<OrderDTO> dtos = new ArrayList<OrderDTO>();
				this.entity2Dto(orderList,dtos);
				result.put("status", 0);
				result.put("data", dtos);
			} else {
				result.put("status", 1);
				result.put("msg", "请登录");
			}
		} catch (Exception e) {
			logger.error("[OrderController][getOrderListByStatus][异常]", e);
			result.put("status", -1);
			result.put("msg", "系统异常");
		}
		return result;
	}
	
	private void entity2Dto(List<OrderEntity> orderList, List<OrderDTO> dtos) {
		if (null != orderList && null != dtos) {
			for (OrderEntity en:orderList) {
				OrderDTO dto = new OrderDTO();
				BeanUtils.copyProperties(en, dto);
				GoodsEntity goods = this.goodsService.getGoodsById(dto.getGoodsId());
				if (null != goods) {
					dto.setGoodsName(goods.getGoodsName());
					dto.setOriginaPrice(goods.getGoodsPrice());
				}
				//dto.setImgUrl("");
				dtos.add(dto);
			}
		}
		
	}

	/**
	 * 模拟支付（改订单状态）
	 * @param request
	 * @return status -1-系统异常，0-成功，1-该订单不存在
	 */
	@RequestMapping("pay")
	@ResponseBody
	public Map<String,Object> pay(HttpServletRequest request,String orderId) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", 0);
		try {
			String userId = "";
			UserEntity user = (UserEntity) request.getSession(true).getAttribute("user");
			if (null != user) {
				userId = user.getUserId();
			}
			OrderEntity order = this.orderService.getOrderByIdAndUserId(orderId, userId);
			if (null != order) {
				order.setStatus(Constant.ORDER_STATUS_PAYED);//模拟支付，只是改个状态
				this.orderService.update(order);
			} else {
				result.put("status", 1);
				result.put("msg", "该订单不存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", -1);
			result.put("msg", "系统异常");
		}
		return result;
	}
}
