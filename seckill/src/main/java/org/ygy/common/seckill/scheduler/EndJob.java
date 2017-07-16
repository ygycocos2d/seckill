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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ygy.common.seckill.entity.ActivityEntity;
import org.ygy.common.seckill.entity.ActivityGoodsInventoryLogEntity;
import org.ygy.common.seckill.entity.ActivityOrderRelationEntity;
import org.ygy.common.seckill.entity.GoodsEntity;
import org.ygy.common.seckill.entity.OrderEntity;
import org.ygy.common.seckill.entity.SuccessLogEntity;
import org.ygy.common.seckill.service.ActivityGoodsInventoryLogService;
import org.ygy.common.seckill.service.ActivityOrderRelationService;
import org.ygy.common.seckill.service.GoodsService;
import org.ygy.common.seckill.service.OrderService;
import org.ygy.common.seckill.service.SuccessLogService;
import org.ygy.common.seckill.util.Constant;
import org.ygy.common.seckill.util.RedisLock;
import org.ygy.common.seckill.util.RedisUtil;
import org.ygy.common.seckill.util.SpringContextUtil;
import org.ygy.common.seckill.util.StringUtil;

import com.alibaba.fastjson.JSONObject;

public class EndJob implements Job {
	
	private Logger       logger = LoggerFactory.getLogger(EndJob.class);
	
	private GoodsService goodsService;
	
	private SuccessLogService successLogService;
	
	private OrderService orderService;
	
	private ActivityGoodsInventoryLogService inventoryLogService;
	
	private ActivityOrderRelationService relationService;
	
	public EndJob() {
		try {
			goodsService = (GoodsService) SpringContextUtil.getBeanByClass(GoodsService.class);
			successLogService = (SuccessLogService) SpringContextUtil.getBeanByClass(SuccessLogService.class);
			orderService = (OrderService) SpringContextUtil.getBeanByClass(OrderService.class);
			inventoryLogService = (ActivityGoodsInventoryLogService) SpringContextUtil.getBeanByClass(ActivityGoodsInventoryLogService.class);
			relationService = (ActivityOrderRelationService) SpringContextUtil.getBeanByClass(ActivityOrderRelationService.class);
		} catch (Exception e) {
			logger.error("EndJob init exception...",e);
		}
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			// 将当前结束的秒杀活动备份，用于活动结束后的相关处理
			ActivityInfo tempInfo = SchedulerContext.getCurActivityInfo();
			logger.info("endjob start activityId="+tempInfo.getActivityId());
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
			String lockKey = Constant.Cache.LOCK+Constant.Cache.SUCC_HANDLED_FLAG+tempInfo.getActivityId();
			RedisLock lock = new RedisLock(lockKey,false);//一次锁
			if (lock.acquireLockWithTimeout(
					RedisUtil.getConnect(), 0L, 0L)) {
				Map<String, Integer> killSucLog = SchedulerContext.getSucLog().getSuccLogInActivity(tempInfo.getActivityId());
				// 构建秒杀成功记录list
				List<SuccessLogEntity> logEntityList = new ArrayList<SuccessLogEntity>();
				List<OrderEntity> orderList = new ArrayList<OrderEntity>();//订单
				List<ActivityOrderRelationEntity> relationList = new ArrayList<ActivityOrderRelationEntity>();//活动订单关联记录
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
					logEntity.setSucclogId(StringUtil.getClusterUUID());
					logEntity.setActivityId(tempInfo.getActivityId());
					logEntity.setUserId(en.getKey());
					logEntity.setGoodsNumber(en.getValue());//如果秒杀数不正常，可以通过记录查看到
					logEntity.setCreateTime(curDate);
					logEntityList.add(logEntity);
					// 生成订单
					OrderEntity order = new OrderEntity();
					order.setOrderId(StringUtil.getClusterUUID());
					order.setGoodsId(tempInfo.getGoodsId());
					order.setGoodsNumber(validSeckillCount);
					order.setGoodsPrice(tempInfo.getGoodsPrice());
					order.setOrderAmount(
							Long.valueOf(validSeckillCount*tempInfo.getGoodsPrice()));
					order.setUserId(en.getKey());
					order.setStatus("0");//创建状态
					order.setCreateTime(curDate);
					orderList.add(order);
					// 生成秒杀活动与订单的关联记录（其中可用于过期订单自动取消）
					ActivityOrderRelationEntity relation = new ActivityOrderRelationEntity();
					relation.setActivityId(tempInfo.getActivityId());
					relation.setOrderId(order.getOrderId());
					relationList.add(relation);
				}
				// 商品还库存,无效秒杀商品数+未秒杀完的商品数
				int seckillLeft = 0;
				Map<String, String> goodsMap = RedisUtil.getHashMap(Constant.Cache.GOODS_NUMBER+tempInfo.getActivityId());
				for (Entry<String, String> entry:goodsMap.entrySet()) {
					seckillLeft += Integer.parseInt(entry.getValue()); 
				}
				int toStock = invalidSeckillTotalCount + seckillLeft;
				
				GoodsEntity goods = null;
				ActivityGoodsInventoryLogEntity inventoryLog = null;
				if (toStock > 0) {
					goods = this.goodsService.getGoodsById(tempInfo.getGoodsId());
					goods.setGoodsInventory(goods.getGoodsInventory() + toStock);
					
					inventoryLog = new ActivityGoodsInventoryLogEntity();
					inventoryLog.setId(StringUtil.getClusterUUID());
					inventoryLog.setActivityId(tempInfo.getActivityId());
					inventoryLog.setGoodsId(goods.getGoodsId());
					inventoryLog.setGoodsInventory(toStock);
					inventoryLog.setCreateTime(new Date());
					inventoryLog.setDescribt("活动-->商品，活动结束还库存，活动剩余="+seckillLeft+",用户多抢="+invalidSeckillTotalCount);
				}
				
				boolean succLogFlag = false;
				boolean orderFlag = false;
				boolean goodsFlag = false;
				boolean inventoryLogFlag = false;
				boolean relarionFlag = false;
				for(int count=3;count>0;count--) {
					// 秒杀记录列表存库
					if (!succLogFlag) {
						try {
							this.successLogService.addSuccessLogBatch(logEntityList);
							succLogFlag = true;
						} catch (Exception e) {
							logger.error("EndJob execute[successLogService.addSuccessLogBatch] exception...",e);
							if (1==count) {//3次都失败，存日志吧，人工处理
								logger.info("[活动ID为="+tempInfo.getActivityId()+"]"+"[秒杀成功记录]=>"+JSONObject.toJSONString(logEntityList));
							}
						}
					}
					// 订单列表存库
					if (!orderFlag) {
						try {
							this.orderService.addOrderBatch(orderList);
							orderFlag = true;
						} catch (Exception e) {
							logger.error("EndJob execute[orderService.addOrderBatch] exception...",e);
							if (1==count) {
								logger.info("[活动ID为="+tempInfo.getActivityId()+"]"+"[秒杀成功订单]=>"+JSONObject.toJSONString(orderList));
							}
						}
					}
					// 活动订单关联存库
					if (!relarionFlag) {
						try {
							this.relationService.addBatch(relationList);
							relarionFlag = true;
						} catch (Exception e) {
							logger.error("EndJob execute[relationService.addBatch] exception...",e);
							if (1==count) {
								logger.info("[活动ID为="+tempInfo.getActivityId()+"]"+"[秒杀成功订单活动关联]=>"+JSONObject.toJSONString(relationList));
							}
						}
					}
					// 商品还库存
					if (!goodsFlag) {
						try {
							this.goodsService.update(goods);
							goodsFlag = true;
						} catch (Exception e) {
							logger.error("EndJob execute[goodsService.update] exception...",e);
							if (1==count) {
								logger.info("[活动ID为="+tempInfo.getActivityId()+"]"+"[商品还库存]=>"+JSONObject.toJSONString(goods));
							}
						}
					}
					// 商品库存记录去向
					if (!inventoryLogFlag) {
						try {
							this.inventoryLogService.add(inventoryLog);
							inventoryLogFlag = true;
						} catch (Exception e) {
							logger.error("EndJob execute[inventoryLogService.add] exception...",e);
							if (1==count) {
								logger.info("[活动ID为="+tempInfo.getActivityId()+"]"+"[商品库存记录去向]=>"+JSONObject.toJSONString(inventoryLog));
							}
						}
					}
					if (succLogFlag&&orderFlag&&goodsFlag&&inventoryLogFlag) {
						break;
					}
				}
			}
			
			// 秒杀生成的订单超时不支付自动取消--定时任务启动
			String name = tempInfo.getActivityId() + "_orderAutoCancel";
			String group = tempInfo.getActivityGid() + "_orderAutoCancel";
			Date date = new Date(System.currentTimeMillis() + 1000*tempInfo.getPayDelay());
			SchedulerContext.getQuartzUtil().add(OrderAutoCancelJob.class, name, group, date);
		} catch (Exception e) {
			logger.error("EndJob execute exception...",e);
		}
	}

}
