package org.ygy.common.seckill.dao;

import org.ygy.common.seckill.entity.GoodsEntity;

public interface GoodsDao {
    int deleteByPrimaryKey(String goodsId);

    int insert(GoodsEntity record);

    int insertSelective(GoodsEntity record);

    GoodsEntity selectByPrimaryKey(String goodsId);

    int updateByPrimaryKeySelective(GoodsEntity record);

    int updateByPrimaryKey(GoodsEntity record);
}