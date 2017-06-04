package org.ygy.common.seckill.entity;

import java.util.Date;

public class SuccessLogEntity {
    private String succlogId;

    private String userId;

    private String activityId;

    private Integer goodsNumber;

    private Date createTime;

    public String getSucclogId() {
        return succlogId;
    }

    public void setSucclogId(String succlogId) {
        this.succlogId = succlogId == null ? null : succlogId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId == null ? null : activityId.trim();
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}