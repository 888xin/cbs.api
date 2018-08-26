package com.lifeix.cbs.contest.bean.settle;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class UserSettleLogListResponse extends ListResponse implements Response{
    private static final long serialVersionUID = 1L;
    List<UserSettleLogResponse> logs;

    public List<UserSettleLogResponse> getLogs() {
        return logs;
    }

    public void setLogs(List<UserSettleLogResponse> logs) {
        this.logs = logs;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
