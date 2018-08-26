package com.lifeix.cbs.message.dto.push;

import java.util.Date;

/**
 * Push队列
 * @author Peter
 *
 */
public class CbsPushScore {

	private Long pushId;

	/**
	 * 推送类型
	 */
	private Integer pushType;
	
	/**
	 * 1 IM 2 外网
	 */
	private Integer target;
	/**
	 * 赛事id
	 */
	private Long contestId;
	/**
	 * 赛事类型
	 */
	private Integer contestType;
	
	/**
	 * Push参数
	 */
	private String params;
	
	private Date createTime;
	
	private Date sentTime;
	
	private Boolean sent;

	public CbsPushScore() {
		super();
	}

	public CbsPushScore(Long pushId, Integer pushType,
			Integer target, Long contestId, Integer contestType, String params,
			Date createTime, Date sentTime, Boolean sent) {
		super();
		this.pushId = pushId;
		this.pushType = pushType;
		this.target = target;
		this.contestId = contestId;
		this.contestType = contestType;
		this.params = params;
		this.createTime = createTime;
		this.sentTime = sentTime;
		this.sent = sent;
	}

	public Long getContestId() {
		return contestId;
	}

	public void setContestId(Long contestId) {
		this.contestId = contestId;
	}

	public Integer getContestType() {
		return contestType;
	}

	public void setContestType(Integer contestType) {
		this.contestType = contestType;
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
