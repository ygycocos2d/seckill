package org.ygy.common.seckill.serviceImpl;

import javax.annotation.Resource;

import org.ygy.common.seckill.dao.ActivityGoodsInventoryLogDao;
import org.ygy.common.seckill.entity.ActivityGoodsInventoryLogEntity;
import org.ygy.common.seckill.service.ActivityGoodsInventoryLogService;

public class ActivityGoodsInventoryLogServiceImpl implements ActivityGoodsInventoryLogService{
	
	@Resource
	private ActivityGoodsInventoryLogDao inventoryLogDao;

	@Override
	public void add(ActivityGoodsInventoryLogEntity inventoryLog) {
		if (null != inventoryLog) {
			this.inventoryLogDao.insertSelective(inventoryLog);
		}
	}

}
