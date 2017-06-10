package org.ygy.common.seckill.service;

import java.util.Date;
import java.util.List;

import org.ygy.common.seckill.entity.ActivityEntity;
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
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	ActivityEntity getByTime(String activityId, Date starttime, Date endtime);

	void add(ActivityEntity entity);

	void update(ActivityEntity entity);

	/**
	 * 更新活动信息同时更新商品库存
	 * @param activity
	 * @param goods
	 */
	void updateActivityAndGoods(ActivityEntity activity, GoodsEntity goods);

	/**
	 * 增加活动同时更新商品库存
	 * @param activity
	 * @param goods
	 */
	void addActivityAndUpdateGoods(ActivityEntity activity, GoodsEntity goods);

}
