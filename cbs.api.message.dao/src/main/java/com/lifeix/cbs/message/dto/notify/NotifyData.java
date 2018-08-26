package com.lifeix.cbs.message.dto.notify;

import java.io.Serializable;
import java.util.Date;

public class NotifyData implements Serializable {
	
	private static final long serialVersionUID = 9054333782895865822L;

	private Long notifyId;

	private Long userId;

	private Long targetId;
	
	private int type;
	
	private Long templetId;
	
	private String template;
	
	private String templateData;

	private Date createTime;
	
	private boolean readFlag;

    private Long skipId ;

	public NotifyData() {
		super();
	}

	public NotifyData(Long notifyId, Long userId, Long targetId, int type,
			Long templetId, String template, String templateData, Date createTime,
			boolean readFlag) {
		super();
		this.notifyId = notifyId;
		this.userId = userId;
		this.targetId = targetId;
		this.type = type;
		this.templetId = templetId;
		this.template = template;
		this.templateData = templateData;
		this.createTime = createTime;
		this.readFlag = readFlag;
	}

    public Long getSkipId() {
        return skipId;
    }

    public void setSkipId(Long skipId) {
        this.skipId = skipId;
    }

    public Long getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(Long notifyId) {
		this.notifyId = notifyId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplateData() {
		return templateData;
	}

	public void setTemplateData(String templateData) {
		this.templateData = templateData;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public boolean isReadFlag() {
		return readFlag;
	}

	public void setReadFlag(boolean readFlag) {
		this.readFlag = readFlag;
	}

	public Long getTempletId() {
		return this.templetId;
	}

	public void setTempletId(Long templetId) {
		this.templetId = templetId;
	}

}
