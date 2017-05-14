package org.ygy.common.seckill.dao;

import org.ygy.common.seckill.entity.ImgEntity;

public interface ImgDao {
    int deleteByPrimaryKey(String imgId);

    int insert(ImgEntity record);

    int insertSelective(ImgEntity record);

    ImgEntity selectByPrimaryKey(String imgId);

    int updateByPrimaryKeySelective(ImgEntity record);

    int updateByPrimaryKey(ImgEntity record);
}