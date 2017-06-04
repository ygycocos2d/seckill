package org.ygy.common.seckill.service;

import org.ygy.common.seckill.entity.UserEntity;

public interface UserService {

	void add(UserEntity user);

	UserEntity getByAccount(String userAccount);

}
