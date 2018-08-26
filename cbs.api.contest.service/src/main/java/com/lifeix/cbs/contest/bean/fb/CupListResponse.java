package com.lifeix.cbs.contest.bean.fb;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class CupListResponse extends ListResponse implements Response {

    /**
     * 
     */
    private static final long serialVersionUID = 605675402339423617L;

    private List<CupResponse> cups;

    @Override
    public String getObjectName() {
	return null;
    }

    public List<CupResponse> getCups() {
	return cups;
    }

    public void setCups(List<CupResponse> cups) {
	this.cups = cups;
    }

}
