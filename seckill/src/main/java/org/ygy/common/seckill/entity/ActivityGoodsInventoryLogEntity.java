package org.ygy.common.seckill.entity;

public class ActivityGoodsInventoryLogEntity {
    private String id;

    private String activityId;

    private String goodsId;

    private Integer goodsInventory;

    private String describt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId == null ? null : activityId.trim();
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId == null ? null : goodsId.trim();
    }

    public Integer getGoodsInventory() {
        return goodsInventory;
    }

    public void setGoodsInventory(Integer goodsInventory) {
        this.goodsInventory = goodsInventory;
    }

    public String getDescribt() {
        return describt;
    }

    public void setDescribt(String describt) {
        this.describt = describt == null ? null : describt.trim();
    }
}