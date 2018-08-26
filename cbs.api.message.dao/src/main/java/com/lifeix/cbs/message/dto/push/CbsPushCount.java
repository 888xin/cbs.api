package com.lifeix.cbs.message.dto.push;

import java.util.Date;

/**
 * Push统计队列
 * @author Peter
 *
 */
public class CbsPushCount {

	private Long pushId;

	/**
	 * 用户ID
	 */
	private Long userId;
	
	/**
	 * 数量统计
	 */
	private Integer count;
	
	/**
	 * Push参数
	 */
	private String params;
	
	private Date createTime;
	
	private Date sentTime;
	
	private Boolean sent;

	public CbsPushCount() {
		super();
	}

	public CbsPushCount(Long pushId, Long userId, Integer count,
			String params, Date createTime, Date sentTime, Boolean sent) {
		super();
		this.pushId = pushId;
		this.userId = userId;
		this.count = count;
		this.params = params;
		this.createTime = createTime;
		this.sentTime = sentTime;
		this.sent = sent;
	}

	public Long getPushId() {
		return pushId;
	}

	public void setPushId(Long pushId) {
		this.pushId = pushId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getSentTime() {
		return sentTime;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}

	public Boolean getSent() {
		return sent;
	}

	public void setSent(Boolean sent) {
		this.sent = sent;
	}
	
}
