package com.lifeix.cbs.api.bean.user;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class CbsUserWxListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -3684606920787867493L;
    private List<CbsUserWxResponse> userWxs;

    public CbsUserWxListResponse() {
	super();
    }

    public List<CbsUserWxResponse> getUserWxs() {
	return userWxs;
    }

    public void setUserWxs(List<CbsUserWxResponse> userWxs) {
	this.userWxs = userWxs;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
