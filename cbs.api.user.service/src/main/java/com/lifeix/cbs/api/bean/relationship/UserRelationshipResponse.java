package com.lifeix.cbs.api.bean.relationship;

import com.lifeix.user.beans.Response;

public class UserRelationshipResponse implements Response{
    private static final long serialVersionUID = 1L;

    
    /**
     * 我关注的人数量
     */
    private Integer following_count = 0;
    
    /**
     * 关注我的人数量
     */
    private Integer follower_count = 0;
    
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

    @Override
    public String getObjectName() {
	return null;
    }

}
