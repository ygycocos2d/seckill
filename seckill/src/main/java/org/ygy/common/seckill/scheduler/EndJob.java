package org.ygy.common.seckill.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.ygy.common.seckill.entity.ActivityEntity;
import org.ygy.common.seckill.entity.ActivityGoodsInventoryLogEntity;
import org.ygy.common.seckill.entity.GoodsEntity;
import org.ygy.common.seckill.entity.OrderEntity;
import org.ygy.common.seckill.entity.SuccessLogEntity;
import org.ygy.common.seckill.service.ActivityGoodsInventoryLogService;
import org.ygy.common.seckill.service.GoodsService;
import org.ygy.common.seckill.service.OrderService;
import org.ygy.common.seckill.service.SuccessLogService;
import org.ygy.common.seckill.util.SpringContextUtil;
import org.ygy.common.seckill.util.StringUtil;

public class EndJob implements Job {
	
	private GoodsService goodsService;
	
	private SuccessLogService successLogService;
	
	private OrderService orderService;
	
	private ActivityGoodsInventoryLogService inventoryLogService;
	
	public EndJob() {
		try {
			goodsService = (GoodsService) SpringContextUtil.getBeanByClass(GoodsService.class);
			successLogService = (SuccessLogService) SpringContextUtil.getBeanByClass(SuccessLogService.class);
			orderService = (OrderService) SpringContextUtil.getBeanByClass(OrderService.class);
			inventoryLogService = (ActivityGoodsInventoryLogService) SpringContextUtil.getBeanByClass(ActivityGoodsInventoryLogService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("endjob start---------------------");
		// 将当前结束的秒杀活动备份，用于活动结束后的相关处理
		ActivityInfo tempInfo = SchedulerContext.getCurActivityInfo();
		SchedulerContext.setCurActivityInfo(null);
		// 如果有下一个秒杀活动，则进行任务调度
		ActivityEntity entity = SchedulerContext.getActivityQueue().getHeaderNotDel();
		if (null != entity) {
			String name = entity.getActivityId() + "_start";
			String group = entity.getGroupId() + "_start";
			SchedulerContext.getQuartzUtil().add(StartJob.class, name, group, new Date());
		} 
		/**
		 *  进行当前秒杀活动结束后的一些操作（tempInfo），统计实际抢了多少，有多少被多抢了，生成订单、还库存等
		 */
		// 该次秒杀活动秒杀记录获取后，清除缓存，以备下一个秒杀活动使用
		Map<String, Integer> killSucLog = SchedulerContext.getSucLog().getAll();
		SchedulerContext.getSucLog().clearAll();
		// 构建秒杀成功记录list
		List<SuccessLogEntity> logEntityList = new ArrayList<SuccessLogEntity>();
		List<OrderEntity> orderList = new ArrayList<OrderEntity>();//订单
		int invalidSeckillTotalCount = 0;//无效商品总数，秒杀活动中即多抢了的商品数
		Set<Entry<String, Integer>> set = killSucLog.entrySet();
		Iterator<Entry<String, Integer>> iterator = set.iterator();
		Date curDate = new Date();
		while (iterator.hasNext()) {
			Entry<String, Integer> en = iterator.next();
			// 统计多抢了超过限制的商品数，用于还库(按道理不允许出现这种情况，出现那就是系统漏洞)
			int validSeckillCount = en.getValue();//有效秒杀商品数，用于生成订单
			if (en.getValue() > tempInfo.getNumLimit()) {
				validSeckillCount = tempInfo.getNumLimit();
				invalidSeckillTotalCount += en.getValue() - tempInfo.getNumLimit();
			}
			// 秒杀记录
			SuccessLogEntity logEntity = new SuccessLogEntity();
			logEntity.setSucclogId(StringUtil.getUUID());
			logEntity.setActivityId(tempInfo.getActivityId());
			logEntity.setUserId(en.getKey());
			logEntity.setGoodsNumber(en.getValue());//如果秒杀数不正常，可以通过记录查看到
			logEntity.setCreateTime(curDate);
			logEntityList.add(logEntity);
			// 生成订单
			OrderEntity order = new OrderEntity();
			order.setOrderId(StringUtil.getUUID());
			order.setGoodsId(tempInfo.getGoodsId());
			order.setGoodsNumber(validSeckillCount);
			order.setUserId(en.getKey());
			order.setStatus("0");//创建状态
			orderList.add(order);
		}
		// 秒杀记录列表存库、订单列表存库
		this.successLogService.batchAddSuccessLog(logEntityList);
		this.orderService.batchAddOrder(orderList);
		// 商品还库存
		int toStock = tempInfo.getGoodsNum().intValue() + invalidSeckillTotalCount;
		if (toStock > 0) {
			GoodsEntity goods = this.goodsService.getGoodsById(tempInfo.getGoodsId());
			goods.setGoodsNumber(goods.getGoodsNumber() + toStock);
			this.goodsService.update(goods);
			ActivityGoodsInventoryLogEntity inventoryLog = new ActivityGoodsInventoryLogEntity();
			inventoryLog.setId(StringUtil.getUUID());
			inventoryLog.setActivityId(tempInfo.getActivityId());
			inventoryLog.setGoodsId(goods.getGoodsId());
			inventoryLog.setGoodsInventory(toStock);
			inventoryLog.setDescribt("活动-->商品，活动结束还库存，活动剩余="+tempInfo.getGoodsNum().intValue()+",用户多抢="+invalidSeckillTotalCount);
			this.inventoryLogService.add(inventoryLog);
		}
	}

}
