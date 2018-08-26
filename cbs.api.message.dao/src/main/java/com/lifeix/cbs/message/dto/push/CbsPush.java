package com.lifeix.cbs.message.dto.push;

import java.util.Date;

/**
 * Push队列
 * @author Peter
 *
 */
public class CbsPush {

	private Long pushId;

	/**
	 * 推送类型
	 */
	private Integer pushType;
	
	/**
	 * 类型ID
	 */
	private Long pushData;
	
	/**
	 * 1 IM 2 外网
	 */
	private Integer target;
	
	/**
	 * Push参数
	 */
	private String params;
	
	private Date createTime;
	
	private Date sentTime;
	
	private Boolean sent;

	public CbsPush() {
		super();
	}

	public CbsPush(Long pushId, Integer pushType, Long pushData,
			Integer target, String params, Date createTime, Date sentTime,
			Boolean sent) {
		super();
		this.pushId = pushId;
		this.pushType = pushType;
		this.pushData = pushData;
		this.target = target;
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

	public Integer getPushType() {
		return pushType;
	}

	public void setPushType(Integer pushType) {
		this.pushType = pushType;
	}

	public Long getPushData() {
		return pushData;
	}

	public void setPushData(Long pushData) {
		this.pushData = pushData;
	}

	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
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
