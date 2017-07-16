package org.ygy.common.seckill.dto;

import org.ygy.common.seckill.entity.OrderEntity;

public class OrderDTO extends OrderEntity{
	
	private String goodsName;
	
	private String imgUrl;
	
	private Integer originaPrice;//原价
	
	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getOriginaPrice() {
		return originaPrice;
	}

	public void setOriginaPrice(Integer originaPrice) {
		this.originaPrice = originaPrice;
	}

}
