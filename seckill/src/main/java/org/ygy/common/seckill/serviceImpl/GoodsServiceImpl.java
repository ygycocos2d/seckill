package org.ygy.common.seckill.serviceImpl;

import javax.annotation.Resource;

import org.ygy.common.seckill.dao.GoodsDao;
import org.ygy.common.seckill.entity.GoodsEntity;
import org.ygy.common.seckill.service.GoodsService;
import org.ygy.common.seckill.util.StringUtil;

public class GoodsServiceImpl implements GoodsService{
	
	@Resource
	private GoodsDao goodsDao;

	@Override
	public GoodsEntity getGoodsById(String goodsId) {
		if (StringUtil.isEmpty(goodsId)) {
			return null;
		}
		return this.goodsDao.selectByPrimaryKey(goodsId);
	}

	@Override
	public void update(GoodsEntity goods) {
		if (null != goods) {
			this.goodsDao.updateByPrimaryKeySelective(goods);
		}
	}

}
