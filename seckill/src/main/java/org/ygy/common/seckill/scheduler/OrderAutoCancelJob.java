package org.ygy.common.seckill.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ygy.common.seckill.entity.OrderEntity;
import org.ygy.common.seckill.service.ActivityOrderRelationService;
import org.ygy.common.seckill.service.OrderService;
import org.ygy.common.seckill.util.Constant;
import org.ygy.common.seckill.util.SpringContextUtil;

public class OrderAutoCancelJob implements Job{
	
	private Logger       logger = LoggerFactory.getLogger(OrderAutoCancelJob.class);
	
	private OrderService orderService;
	
	private ActivityOrderRelationService relationService;
	
	public OrderAutoCancelJob() {
		try {
			orderService = (OrderService) SpringContextUtil.getBeanByClass(OrderService.class);
			relationService = (ActivityOrderRelationService) SpringContextUtil.getBeanByClass(ActivityOrderRelationService.class);
		} catch (Exception e) {
			logger.error("OrderAutoCancelJob init exception...",e);
		}
	}
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("OrderAutoCancelJob execute start...");
		String jobName = context.getJobDetail().getKey().getName();
		String activityId = jobName.substring(0, jobName.indexOf("_"));
		for (int count=3;count>0;count--) {
			try {
				List<String> orderIdList = relationService.getOrderIdListByActivityId(activityId);
				if (null != orderIdList && !orderIdList.isEmpty()) {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("orderIdList", orderIdList);
					param.put("status", Constant.ORDER_STATUS_PAYING);
					List<OrderEntity> orderList = orderService.getByOrderIdListAndStatus(param);
					if (null != orderList && !orderList.isEmpty()) {
						orderService.cancelOrderListAuto(orderList,activityId);
					}
				}
				break;
			} catch (Exception e) {
				logger.error("OrderAutoCancelJob execute exception...",e);
			}
		}
	}

}
