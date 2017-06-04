package org.ygy.common.seckill.entity;

import java.util.Date;

public class ActivityEntity {
    private String activityId;

    private String groupId;

    private Date startTime;

    private Date endTime;

    private String goodsId;

    private Integer goodsPrice;

    private Integer goodsNumber;

    private Integer leftGoodsNumber;

    private Integer limitNumber;

    private Integer payDelay;

    private String status;

    private String describt;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId == null ? null : activityId.trim();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId == null ? null : goodsId.trim();
    }

    public Integer getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Integer goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public Integer getLeftGoodsNumber() {
        return leftGoodsNumber;
    }

    public void setLeftGoodsNumber(Integer leftGoodsNumber) {
        this.leftGoodsNumber = leftGoodsNumber;
    }

    public Integer getLimitNumber() {
        return limitNumber;
    }

    public void setLimitNumber(Integer limitNumber) {
        this.limitNumber = limitNumber;
    }

    public Integer getPayDelay() {
        return payDelay;
    }

    public void setPayDelay(Integer payDelay) {
        this.payDelay = payDelay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getDescribt() {
        return describt;
    }

    public void setDescribt(String describt) {
        this.describt = describt == null ? null : describt.trim();
    }
}