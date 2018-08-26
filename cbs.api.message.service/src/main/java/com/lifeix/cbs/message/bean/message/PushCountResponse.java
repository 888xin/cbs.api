package com.lifeix.cbs.message.bean.message;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class PushCountResponse implements JsonSerializer<PushCountResponse>, Response {
    private static final long serialVersionUID = 1L;

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

	public PushCountResponse() {
		super();
	}

	public PushCountResponse(Long pushId, Long userId, Integer count,
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
	

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(PushCountResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }}
