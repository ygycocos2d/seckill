package org.ygy.common.seckill.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.ygy.common.seckill.dao.ImgDao;
import org.ygy.common.seckill.entity.ImgEntity;
import org.ygy.common.seckill.service.ImgService;
import org.ygy.common.seckill.util.StringUtil;

public class ImgServiceImpl implements ImgService {
	
	@Resource
	private ImgDao imgDao;

	@Override
	public List<ImgEntity> getImgListByActivityId(String activityId) {
		if (StringUtil.isEmpty(activityId)) {
			return new ArrayList<ImgEntity>(0);
		}
		return this.imgDao.getImgListByActivityId(activityId);
	}

}
