package com.lifeix.cbs.api.bean.mission;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

import java.util.List;

public class MissionUserListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 8138200851019916831L;

    private List<MissionUserResponse> mission_user;

    public List<MissionUserResponse> getMission_user() {
        return mission_user;
    }

    public void setMission_user(List<MissionUserResponse> mission_user) {
        this.mission_user = mission_user;
    }

    @Override
    public String getObjectName() {
	return null;
    }

   

}
