package com.lifeix.cbs.api.bean.relationship;

import java.util.List;

import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class RelationShipListResponse extends ListResponse implements Response{
    private static final long serialVersionUID = 740978285801341467L;

    private List<CbsUserResponse> users;
    
    /**
     * 我关注的人数量
     */
    private Integer following_count;
    
    /**
     * 关注我的人数量
     */
    private Integer follower_count;
    
    public Integer getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(Integer following_count) {
        this.following_count = following_count;
    }

    public Integer getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(Integer follower_count) {
        this.follower_count = follower_count;
    }

    public List<CbsUserResponse> getUsers() {
        return users;
    }

    public void setUsers(List<CbsUserResponse> users) {
        this.users = users;
    }

    @Override
    public String getObjectName() {
	return null;
    }


}
