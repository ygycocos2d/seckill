package org.ygy.common.seckill.scheduler;

import java.util.List;

import org.ygy.common.seckill.entity.ImgEntity;
import org.ygy.common.seckill.util.AtomicIntegerExt;

public class ActivityInfo {
	
	//------------------
	private List<ImgEntity> imgList;
	
	//-------------------

	private AtomicIntegerExt goodsNum;
	
	private int numLimit;
	
	private String activityId;

    private String activityGid;

    private long startTime;

    private long endTime;

    private String goodsId;

    //当前部署应用的任务状态，不代表集群中所有任务的状态（即数据库中任务的状态）
    private String status;

    private Integer goodsPrice;
    
    private Integer payDelay;

    private String describt;

	public AtomicIntegerExt getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(AtomicIntegerExt goodsNum) {
		this.goodsNum = goodsNum;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Integer goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public int getNumLimit() {
		return numLimit;
	}

	/**
	 * 至少为1
	 * @param numLimit
	 */
	public void setNumLimit(int numLimit) {
		if (numLimit <= 0)
			numLimit = 1;
		this.numLimit = numLimit;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityGid() {
		return activityGid;
	}

	public void setActivityGid(String activityGid) {
		this.activityGid = activityGid;
	}

	public List<ImgEntity> getImgList() {
		return imgList;
	}

	public void setImgList(List<ImgEntity> imgList) {
		this.imgList = imgList;
	}

	public String getDescribt() {
		return describt;
	}

	public void setDescribt(String describt) {
		this.describt = describt;
	}

	public Integer getPayDelay() {
		return payDelay;
	}

	public void setPayDelay(Integer payDelay) {
		this.payDelay = payDelay;
	}

}
