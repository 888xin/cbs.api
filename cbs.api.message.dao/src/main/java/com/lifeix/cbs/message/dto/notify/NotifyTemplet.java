package com.lifeix.cbs.message.dto.notify;

import java.io.Serializable;

public class NotifyTemplet implements Serializable {
	
	private static final long serialVersionUID = -4548733345207444289L;

	private Long templetId;
	
	private int type;
	
	private String template;

	public NotifyTemplet() {
		super();
	}

	public NotifyTemplet(Long templetId, int type, String template) {
		super();
		this.templetId = templetId;
		this.type = type;
		this.template = template;
	}

	public Long getTempletId() {
		return templetId;
	}

	public void setTempletId(Long templetId) {
		this.templetId = templetId;
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

}
