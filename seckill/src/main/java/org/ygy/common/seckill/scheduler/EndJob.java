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
import org.ygy.common.seckill.entity.SuccessLogEntity;
import org.ygy.common.seckill.util.ActivityQueue;
import org.ygy.common.seckill.util.StringUtil;

public class EndJob implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		ActivityInfo tempInfo = SchedulerContext.getCurActivityInfo();
		
		
		ActivityEntity entity = SchedulerContext.getActivityQueue().getHeader();
		if (null != entity) {
			ActivityInfo nextInfo = new ActivityInfo();
			
			//entity-->nextInfo
			
			SchedulerContext.setCurActivityInfo(nextInfo);
			
			// 调度下一个秒杀活动的开始任务
			String name = nextInfo.getActivityId() + "_start";
			String group = nextInfo.getActivityGid() + "_start";
			SchedulerContext.getQuartzUtil().add(StartJob.class, name, group, new Date());
		} else {
			
		}
		
		/**
		 *  进行当前秒杀活动结束后的一些操作（tempInfo），统计实际抢了多少，有多少被多抢了，生成订单、还库存等
		 */
		
		Map<String, Integer> killSucLog = SchedulerContext.getSucLog().getAll();
		SchedulerContext.getSucLog().clearAll();
		
		
		// 构建秒杀成功记录list
		List<SuccessLogEntity> logEntityList = new ArrayList<SuccessLogEntity>();
		int invalidSeckillCount = 0;
		
		// 生成订单
//		List<OrderEntity> orderList = new ArrayList<OrderEntity>();
		
		Set<Entry<String, Integer>> set = killSucLog.entrySet();
		Iterator<Entry<String, Integer>> iterator = set.iterator();
		Date curDate = new Date();
		while (iterator.hasNext()) {
			Entry<String, Integer> en = iterator.next();
			
			// 统计多抢了超过限制的商品数，用于还库
			if (tempInfo.getNumLimit() < en.getValue()) {
				invalidSeckillCount += en.getValue() - tempInfo.getNumLimit();
			}

			// 秒杀记录
			SuccessLogEntity logEntity = new SuccessLogEntity();
			logEntity.setSucclogId(StringUtil.getUUID());
			logEntity.setActivityId(tempInfo.getActivityId());
			logEntity.setUserId(en.getKey());
			logEntity.setGoodsNumber(en.getValue());
			logEntity.setCreateTime(curDate);
			
			logEntityList.add(logEntity);
		}
		
		// 还库存
		int toStock = tempInfo.getGoodsNum().intValue() + invalidSeckillCount;
		if (toStock > 0) {
			// 调用还库存接口
		}
	}

}
