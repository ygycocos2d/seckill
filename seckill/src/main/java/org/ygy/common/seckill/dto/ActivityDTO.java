package org.ygy.common.seckill.dto;

public class ActivityDTO {
	
	private String activityId;

    private String groupId;
    
    private Long startTime;

	private Long endTime;

    private String goodsId;

    private Integer goodsPrice;

    private Integer goodsNumber;

    private Integer limitNumber;

    private Integer payDelay;

    private String status;

    private String describt;

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
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
		this.status = status;
	}

	public String getDescribt() {
		return describt;
	}

	public void setDescribt(String describt) {
		this.describt = describt;
	}
    
}