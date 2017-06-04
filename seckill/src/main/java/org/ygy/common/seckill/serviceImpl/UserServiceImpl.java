package org.ygy.common.seckill.serviceImpl;

import javax.annotation.Resource;

import org.ygy.common.seckill.dao.UserDao;
import org.ygy.common.seckill.entity.UserEntity;
import org.ygy.common.seckill.service.UserService;
import org.ygy.common.seckill.util.StringUtil;

public class UserServiceImpl implements UserService{
	
	@Resource
	private UserDao userDao;

	@Override
	public void add(UserEntity user) {
		if (null != user) {
			this.userDao.insertSelective(user);
		}
	}

	@Override
	public UserEntity getByAccount(String userAccount) {
		if (StringUtil.isEmpty(userAccount)) {
			return null;
		}
		return this.userDao.getByAccount(userAccount);
	}

}
