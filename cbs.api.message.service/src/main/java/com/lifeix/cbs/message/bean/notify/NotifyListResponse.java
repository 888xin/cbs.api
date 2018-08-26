package com.lifeix.cbs.message.bean.notify;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class NotifyListResponse extends ListResponse implements Response {
    private static final long serialVersionUID = 1L;
    private List<NotifyResponse> notifies;

    public List<NotifyResponse> getNotifies() {
	return this.notifies;
    }

    public void setNotifies(List<NotifyResponse> notifies) {
	this.notifies = notifies;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
