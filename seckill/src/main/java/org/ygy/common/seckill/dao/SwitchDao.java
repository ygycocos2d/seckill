package org.ygy.common.seckill.dao;

import org.ygy.common.seckill.entity.SwitchEntity;

public interface SwitchDao {

	SwitchEntity selectByPrimaryKey(String id);

	SwitchEntity selectByType(String type);
}
