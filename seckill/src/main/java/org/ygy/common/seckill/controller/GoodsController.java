package org.ygy.common.seckill.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ygy.common.seckill.entity.GoodsEntity;
import org.ygy.common.seckill.service.GoodsService;
import org.ygy.common.seckill.util.StringUtil;

@Controller
@RequestMapping("goods")
public class GoodsController {
	
	private Logger       logger = LoggerFactory.getLogger(GoodsController.class);
	
	@Resource
	private GoodsService goodsService;

	@RequestMapping("getGoodsById")
	@ResponseBody
	public Map<String,Object> getGoodsById(String goodsId) {
		Map<String,Object> result = new HashMap<String,Object>();
		if (StringUtil.isEmpty(goodsId)) {
			result.put("msg", "参数不合法");
		} else {
			try {
				GoodsEntity goods = this.goodsService.getGoodsById(goodsId);
				result.put("status", 0);
				result.put("data", goods);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("[GoodsController][getGoodsById][异常]", e);
				result.put("status", -1);
				result.put("msg", "系统异常");
			}
		}
		return result;
	}
}
