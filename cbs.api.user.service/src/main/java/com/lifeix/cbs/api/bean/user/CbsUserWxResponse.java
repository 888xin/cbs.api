package com.lifeix.cbs.api.bean.user;

import com.lifeix.user.beans.Response;

public class CbsUserWxResponse implements Response {

    /**
     * 
     */
    private static final long serialVersionUID = -8820477720562029178L;

    /**
     * 用户id
     */
    private Long user_id;

    private String open_id;

    private String app_id;

    private String source;

    private String create_time;

    @Override
    public String getObjectName() {
	return null;
    }

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public String getOpen_id() {
	return open_id;
    }

    public void setOpen_id(String open_id) {
	this.open_id = open_id;
    }

    public String getApp_id() {
	return app_id;
    }

    public void setApp_id(String app_id) {
	this.app_id = app_id;
    }

    public String getSource() {
	return source;
    }

    public void setSource(String source) {
	this.source = source;
    }

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

}
