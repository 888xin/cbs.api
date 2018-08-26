package com.lifeix.cbs.api.dto.rank;

import java.util.Date;

public class MedalTask {
	
	/**
	 * 任务类型id 
	 */
	
    private Long taskId;
	/**
	 * 任务上次执行到的id
	 */
	private Long lastId;
	
	/**
	 * 状态 1 表示ok， 0 表示统计更新成功，但是内容更新失败 
	 */
	private Integer status = 1; 
	
	/**
	 * 上次更新时间
	 */
	private Date updateTime;
	
	public MedalTask() {
		super();
	}

	public MedalTask(Long taskId, Long lastId, Integer status, Date updateTime) {
		super();
		this.taskId = taskId;
		this.lastId = lastId;
		this.status = status;
		this.updateTime = updateTime;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getLastId() {
		return lastId;
	}

	public void setLastId(Long lastId) {
		this.lastId = lastId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
