package org.ygy.common.seckill.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.ygy.common.seckill.dao.ActivityDao;
import org.ygy.common.seckill.dao.ActivityGoodsInventoryLogDao;
import org.ygy.common.seckill.dao.GoodsDao;
import org.ygy.common.seckill.entity.ActivityEntity;
import org.ygy.common.seckill.entity.ActivityGoodsInventoryLogEntity;
import org.ygy.common.seckill.entity.GoodsEntity;
import org.ygy.common.seckill.service.ActivityService;
import org.ygy.common.seckill.util.StringUtil;

public class ActivityServiceImpl implements ActivityService {

	@Resource
	private ActivityDao activityDao;
	
	@Resource
	private GoodsDao goodsDao;
	
	@Resource
	private ActivityGoodsInventoryLogDao inventoryLogDao;
	
	@Override
	public ActivityEntity getNotDelActivityById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		return this.activityDao.getNotDelActivityById(id);
	}

	@Override
	public ActivityEntity getEntityById(String activityId) {
		if (StringUtil.isEmpty(activityId)) {
			return null;
		}
		return this.activityDao.selectByPrimaryKey(activityId);
	}

	@Override
	public boolean updateWhenUnchanged(ActivityEntity entity,
			int oldLeftGoodsNum) {
		return false;
	}

	@Override
	public List<ActivityEntity> getAllEffectiveActivity() {
		List<ActivityEntity> entitys = this.activityDao.getAllEffectiveActivity();
		if (null == entitys) {
			entitys = new ArrayList<ActivityEntity>();
		}
		return entitys;
	}

	@Override
	public ActivityEntity getByTime(String activityId, Date startTime, Date endTime) {
		if (null == startTime || null == endTime) {
			return null;
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("activityId", activityId);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		return this.activityDao.getByTime(param);
	}

	@Override
	public void add(ActivityEntity entity) {
		if (null != entity) {
			this.activityDao.insertSelective(entity);
		}
	}

	@Override
	public void update(ActivityEntity entity) {
		if (null != entity) {
			this.activityDao.updateByPrimaryKeySelective(entity);
		}
	}

	//记得加事务
	@Override
	public void updateActivityAndGoods(ActivityEntity activity, GoodsEntity goods,ActivityGoodsInventoryLogEntity inventoryLog) {
		if (null != activity && null != goods && null != inventoryLog) {
			this.activityDao.updateByPrimaryKeySelective(activity);
			this.goodsDao.updateByPrimaryKeySelective(goods);
			this.inventoryLogDao.insertSelective(inventoryLog);
		}
	}

	//记得加事务
	@Override
	public void addActivityAndUpdateGoods(ActivityEntity activity, GoodsEntity goods,ActivityGoodsInventoryLogEntity inventoryLog) {
		if (null != activity && null != goods && null != inventoryLog) {
			this.activityDao.insertSelective(activity);
			this.goodsDao.updateByPrimaryKeySelective(goods);
			this.inventoryLogDao.insertSelective(inventoryLog);
		}
	}

}
