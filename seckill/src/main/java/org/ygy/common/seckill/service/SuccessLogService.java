package org.ygy.common.seckill.service;

import java.util.List;

import org.ygy.common.seckill.entity.SuccessLogEntity;

public interface SuccessLogService {

	void addSuccessLogBatch(List<SuccessLogEntity> logEntityList);

}
