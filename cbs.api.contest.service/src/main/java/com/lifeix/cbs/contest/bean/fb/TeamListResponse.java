package com.lifeix.cbs.contest.bean.fb;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class TeamListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 5096151898924995345L;

    private List<TeamResponse> teams;

    public List<TeamResponse> getTeams() {
	return teams;
    }

    public void setTeams(List<TeamResponse> teams) {
	this.teams = teams;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
