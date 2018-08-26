package com.lifeix.cbs.contest.bean.contest;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class ContestAdListResponse extends ListResponse implements Response {

    /**
     * 
     */
    private static final long serialVersionUID = -1867780708487086792L;
    /**
     * contest list
     */
    private List<ContestAdResponse> ads;

    public List<ContestAdResponse> getAds() {
	return ads;
    }

    public void setAds(List<ContestAdResponse> ads) {
	this.ads = ads;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
