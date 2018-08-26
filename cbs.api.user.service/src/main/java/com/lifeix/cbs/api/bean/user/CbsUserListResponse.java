package com.lifeix.cbs.api.bean.user;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * 用户统计相关信息
 * @author huiy
 *
 */
public class CbsUserListResponse  extends ListResponse implements Response{
	private static final long serialVersionUID = -8500601696903328374L;

	private List<CbsUserResponse> users;
	
	public CbsUserListResponse() {
		super();
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
