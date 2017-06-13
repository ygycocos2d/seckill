package org.ygy.common.seckill.service;

import java.util.List;

import org.ygy.common.seckill.entity.ImgEntity;

public interface ImgService {

	List<ImgEntity> getImgListByActivityId(String activityId);

}
