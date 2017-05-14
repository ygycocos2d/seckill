package org.ygy.common.seckill.entity;

import java.util.Date;

public class TaskEntity {
    private String taskId;

    private String taskGid;

    private Date taskStarttime;

    private Date taskEndtime;

    private String taskGoodsid;

    private Integer taskGoodsnum;

    private String taskStatus;

    private Integer taskAppnum;

    private String taskNuminapp;

    private Integer taskGoodsprice;

    private String taskDescribt;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public String getTaskGid() {
        return taskGid;
    }

    public void setTaskGid(String taskGid) {
        this.taskGid = taskGid == null ? null : taskGid.trim();
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

    public String getTaskGoodsid() {
        return taskGoodsid;
    }

    public void setTaskGoodsid(String taskGoodsid) {
        this.taskGoodsid = taskGoodsid == null ? null : taskGoodsid.trim();
    }

    public Integer getTaskGoodsnum() {
        return taskGoodsnum;
    }

    public void setTaskGoodsnum(Integer taskGoodsnum) {
        this.taskGoodsnum = taskGoodsnum;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus == null ? null : taskStatus.trim();
    }

    public Integer getTaskAppnum() {
        return taskAppnum;
    }

    public void setTaskAppnum(Integer taskAppnum) {
        this.taskAppnum = taskAppnum;
    }

    public String getTaskNuminapp() {
        return taskNuminapp;
    }

    public void setTaskNuminapp(String taskNuminapp) {
        this.taskNuminapp = taskNuminapp == null ? null : taskNuminapp.trim();
    }

    public Integer getTaskGoodsprice() {
        return taskGoodsprice;
    }

    public void setTaskGoodsprice(Integer taskGoodsprice) {
        this.taskGoodsprice = taskGoodsprice;
    }

    public String getTaskDescribt() {
        return taskDescribt;
    }

    public void setTaskDescribt(String taskDescribt) {
        this.taskDescribt = taskDescribt == null ? null : taskDescribt.trim();
    }
}