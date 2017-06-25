package org.ygy.common.seckill.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.ygy.common.seckill.entity.OrderEntity;
import org.ygy.common.seckill.service.ActivityGoodsInventoryLogService;
import org.ygy.common.seckill.service.ActivityOrderRelationService;
import org.ygy.common.seckill.service.GoodsService;
import org.ygy.common.seckill.service.OrderService;
import org.ygy.common.seckill.service.SuccessLogService;
import org.ygy.common.seckill.util.Constant;
import org.ygy.common.seckill.util.SpringContextUtil;

public class OrderAutoCancelJob implements Job{
	
//private GoodsService goodsService;
	
	private OrderService orderService;
	
	private ActivityOrderRelationService relationService;
	
	public OrderAutoCancelJob() {
		try {
//			goodsService = (GoodsService) SpringContextUtil.getBeanByClass(GoodsService.class);
			orderService = (OrderService) SpringContextUtil.getBeanByClass(OrderService.class);
			relationService = (ActivityOrderRelationService) SpringContextUtil.getBeanByClass(ActivityOrderRelationService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		String jobName = context.getJobDetail().getKey().getName();
		String activityId = jobName.substring(0, jobName.indexOf("_"));
		for (;;) {
			try {
				List<String> orderIdList = relationService.getOrderIdListByActivityId(activityId);
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("orderIdList", orderIdList);
				param.put("status", Constant.ORDER_STATUS_PAYING);
				List<OrderEntity> orderList = orderService.getByOrderIdListAndStatus(param);
				orderService.autoCancelOrderList(orderList);
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
