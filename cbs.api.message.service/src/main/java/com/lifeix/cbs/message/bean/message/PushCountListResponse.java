package com.lifeix.cbs.message.bean.message;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class PushCountListResponse extends ListResponse implements Response {
    private static final long serialVersionUID = 1L;
    private List<PushCountResponse> pushCounts;

    public List<PushCountResponse> getPushCounts() {
        return pushCounts;
    }

    public void setPushCounts(List<PushCountResponse> pushCounts) {
        this.pushCounts = pushCounts;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
