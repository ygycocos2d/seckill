package org.ygy.common.seckill.dao;

import org.ygy.common.seckill.entity.ActivityGoodsInventoryLogEntity;

public interface ActivityGoodsInventoryLogDao {
    int deleteByPrimaryKey(String id);

    int insert(ActivityGoodsInventoryLogEntity record);

    int insertSelective(ActivityGoodsInventoryLogEntity record);

    ActivityGoodsInventoryLogEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ActivityGoodsInventoryLogEntity record);

    int updateByPrimaryKey(ActivityGoodsInventoryLogEntity record);
}