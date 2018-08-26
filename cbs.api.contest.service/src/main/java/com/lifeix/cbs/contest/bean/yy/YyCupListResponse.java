package com.lifeix.cbs.contest.bean.yy;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class YyCupListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -3895885410157956364L;

    /**
     * cup list
     */
    private List<YyCupResponse> cups;

    public List<YyCupResponse> getCups() {
	return cups;
    }

    public void setCups(List<YyCupResponse> cups) {
	this.cups = cups;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
