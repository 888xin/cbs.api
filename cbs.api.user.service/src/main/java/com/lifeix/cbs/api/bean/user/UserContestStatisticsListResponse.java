package com.lifeix.cbs.api.bean.user;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class UserContestStatisticsListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -4353164622586480264L;

    private List<UserContestStatisticsResponse> statistics;

    public List<UserContestStatisticsResponse> getStatistics() {
	return statistics;
    }

    public void setStatistics(List<UserContestStatisticsResponse> statistics) {
	this.statistics = statistics;
    }

    @Override
    public String getObjectName() {
	return "statistics";
    }
}