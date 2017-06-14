package org.ygy.common.seckill.service;

import java.util.Date;
import java.util.List;

import org.ygy.common.seckill.entity.ActivityEntity;
import org.ygy.common.seckill.entity.ActivityGoodsInventoryLogEntity;
import org.ygy.common.seckill.entity.GoodsEntity;

public interface ActivityService {

	/**
	 * 未开始、未删除的秒杀活动
	 * @param id
	 * @return
	 */
	ActivityEntity getEffectiveActivityById(String id);

	ActivityEntity getEntityById(String activityId);

	boolean updateWhenUnchanged(ActivityEntity entity, int oldLeftGoodsNum);

	List<ActivityEntity> getAllEffectiveActivity();

	/**
	 * 查询starttime或endtime在已有活动中的秒杀活动，只查一个
	 * @param activityId  当为null时，查全部，非null时，排除自身
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	ActivityEntity getByTime(String activityId, Date startTime, Date endTime);

	void add(ActivityEntity entity);

	void update(ActivityEntity entity);

	/**
	 * 更新活动信息同时更新商品库存
	 * @param activity
	 * @param goods
	 * @param inventoryLog 
	 */
	void updateActivityAndGoods(ActivityEntity activity, GoodsEntity goods, ActivityGoodsInventoryLogEntity inventoryLog);

	/**
	 * 增加活动同时更新商品库存
	 * @param activity
	 * @param goods
	 * @param inventoryLog 
	 */
	void addActivityAndUpdateGoods(ActivityEntity activity, GoodsEntity goods, ActivityGoodsInventoryLogEntity inventoryLog);

}
