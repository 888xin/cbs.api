package com.lifeix.cbs.api.bean.user;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class UserGraphListResponse extends ListResponse implements Response {
    private static final long serialVersionUID = -8500601696903328374L;

    /**
     * 用户信息
     */
    private CbsUserResponse user;

    private List<UserStatisticsResponse> ranks;

    public UserGraphListResponse() {
	super();
    }

    public CbsUserResponse getUser() {
	return user;
    }

    public void setUser(CbsUserResponse user) {
	this.user = user;
    }

    public List<UserStatisticsResponse> getRanks() {
	return ranks;
    }

    public void setRanks(List<UserStatisticsResponse> ranks) {
	this.ranks = ranks;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
