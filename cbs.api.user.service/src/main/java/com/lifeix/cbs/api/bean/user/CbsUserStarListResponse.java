package com.lifeix.cbs.api.bean.user;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * 推荐用户列表
 * 
 * @author lifeix
 * 
 */
public class CbsUserStarListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 7882558489367118281L;

    private List<CbsUserStarResponse> user_stars;

    public List<CbsUserStarResponse> getUser_stars() {
	return user_stars;
    }

    public void setUser_stars(List<CbsUserStarResponse> user_stars) {
	this.user_stars = user_stars;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
