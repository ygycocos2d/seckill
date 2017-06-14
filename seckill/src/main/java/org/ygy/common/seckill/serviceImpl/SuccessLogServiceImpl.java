package org.ygy.common.seckill.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.ygy.common.seckill.dao.SuccessLogDao;
import org.ygy.common.seckill.entity.SuccessLogEntity;
import org.ygy.common.seckill.service.SuccessLogService;

public class SuccessLogServiceImpl implements SuccessLogService{

	@Resource
	private SuccessLogDao successLogDao;
	
	@Override
	public void batchAddSuccessLog(List<SuccessLogEntity> logEntityList) {
		if (null != logEntityList && logEntityList.size() > 0) {
			
		}
		
	}

}
