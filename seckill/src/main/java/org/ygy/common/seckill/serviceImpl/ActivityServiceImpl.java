package org.ygy.common.seckill.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.ygy.common.seckill.dao.ActivityDao;
import org.ygy.common.seckill.dao.GoodsDao;
import org.ygy.common.seckill.entity.ActivityEntity;
import org.ygy.common.seckill.entity.GoodsEntity;
import org.ygy.common.seckill.service.ActivityService;
import org.ygy.common.seckill.util.StringUtil;

public class ActivityServiceImpl implements ActivityService {

	@Resource
	private ActivityDao activityDao;
	
	@Resource
	private GoodsDao goodsDao;
	
	@Override
	public ActivityEntity getEffectiveActivityById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		return this.activityDao.getById(id);
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
		// TODO Auto-generated method stub
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
	public ActivityEntity getByTime(String activityId, Date starttime, Date endtime) {
		if (null == starttime || null == endtime) {
			return null;
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("activityId", activityId);
		param.put("starttime", starttime);
		param.put("endtime", endtime);
		return this.activityDao.getByTime(param);
	}

//	/**
//	 * 记得加事务
//	 */
	@Override
	public void add(ActivityEntity entity) {
		if (null != entity) {
			this.activityDao.insertSelective(entity);
			// 商品减库存
//			GoodsEntity goods = this.goodsDao.selectByPrimaryKey(entity.getGoodsId());
//			goods.setGoodsNumber(goods.getGoodsNumber() - entity.getGoodsNumber());
//			this.goodsDao.updateByPrimaryKey(goods);
		}
	}

	@Override
	public void update(ActivityEntity entity) {
		if (null != entity) {
			this.activityDao.updateByPrimaryKeySelective(entity);
		}
	}

}