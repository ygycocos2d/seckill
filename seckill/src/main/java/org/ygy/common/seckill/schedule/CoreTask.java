package org.ygy.common.seckill.schedule;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class CoreTask {

	private AtomicInteger goodsNum;
	
	private String taskId;

    private String taskGid;

    private Date taskStarttime;

    private Date taskEndtime;

    private String taskGoodsid;

//    private Integer taskGoodsnum;

    //当前部署应用的任务状态，不代表集群中所有任务的状态（即数据库中任务的状态）
    private String taskStatus;

    private Integer taskAppnum;

    private String taskNumInApp;

    private Integer taskGoodsprice;

    private String taskDescribt;

	public AtomicInteger getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(AtomicInteger goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskGid() {
		return taskGid;
	}

	public void setTaskGid(String taskGid) {
		this.taskGid = taskGid;
	}

	public Date getTaskStarttime() {
		return taskStarttime;
	}

	public void setTaskStarttime(Date taskStarttime) {
		this.taskStarttime = taskStarttime;
	}

	public Date getTaskEndtime() {
		return taskEndtime;
	}

	public void setTaskEndtime(Date taskEndtime) {
		this.taskEndtime = taskEndtime;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Integer getTaskGoodsprice() {
		return taskGoodsprice;
	}

	public void setTaskGoodsprice(Integer taskGoodsprice) {
		this.taskGoodsprice = taskGoodsprice;
	}

	public String getTaskNumInApp() {
		return taskNumInApp;
	}

	public void setTaskNumInApp(String taskNumInApp) {
		this.taskNumInApp = taskNumInApp;
	}

	public Integer getTaskAppnum() {
		return taskAppnum;
	}

	public void setTaskAppnum(Integer taskAppnum) {
		this.taskAppnum = taskAppnum;
	}

	public String getTaskDescribt() {
		return taskDescribt;
	}

	public void setTaskDescribt(String taskDescribt) {
		this.taskDescribt = taskDescribt;
	}

	public String getTaskGoodsid() {
		return taskGoodsid;
	}

	public void setTaskGoodsid(String taskGoodsid) {
		this.taskGoodsid = taskGoodsid;
	}
}
