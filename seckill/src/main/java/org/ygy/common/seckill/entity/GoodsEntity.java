package org.ygy.common.seckill.entity;

public class GoodsEntity {
    private String goodsId;

    private String goodsName;

    private Integer goodsPrice;

    private Integer goodsInventory;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId == null ? null : goodsId.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public Integer getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Integer goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getGoodsInventory() {
        return goodsInventory;
    }

    public void setGoodsInventory(Integer goodsInventory) {
        this.goodsInventory = goodsInventory;
    }
}