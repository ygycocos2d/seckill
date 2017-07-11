package org.ygy.common.seckill.serviceImpl;

import javax.annotation.Resource;

import org.ygy.common.seckill.dao.SwitchDao;
import org.ygy.common.seckill.entity.SwitchEntity;
import org.ygy.common.seckill.service.SwitchService;

public class SwitchServiceImpl implements SwitchService{
	
	@Resource
	private SwitchDao switchDao;

	@Override
	public String getStatusById(String id) {
		SwitchEntity en =switchDao.selectByPrimaryKey(id);
		return en==null?null:en.getStatus();
	}

	@Override
	public String getStatusByType(String type) {
		SwitchEntity en =switchDao.selectByType(type);
		return en==null?null:en.getStatus();
	}
}
