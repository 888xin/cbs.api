package com.lifeix.cbs.api.dto.user;

import java.util.Date;

/**
 * 基于投资回报率的用户排名记录
 * @author pengkw
 * 
 */
public class CbsUserRankLog {
	
	/**
	 * id
	 */
	private Integer logId;
	
	/**
	 * 用户排名
	 */
	private String rank;
	
	/**
	 * 记录更新时间
	 */
	private Date updateTime;
	
	public CbsUserRankLog(){
		super();
	}

	public CbsUserRankLog(Integer logId, String rank, Date updateTime) {
		super();
		this.logId = logId;
		this.rank = rank;
		this.updateTime = updateTime;
	}

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
