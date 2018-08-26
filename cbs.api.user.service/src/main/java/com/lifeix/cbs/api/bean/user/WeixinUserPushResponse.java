package com.lifeix.cbs.api.bean.user;

import lifeix.framwork.util.JsonUtils;

import com.lifeix.user.beans.Response;

public class WeixinUserPushResponse  implements Response{
    private static final long serialVersionUID = 1L;

    private String touser;
    private String template_id;
    private String url;
    private WeixinUserDataResponse data;
    @Override
    public String getObjectName() {
	return null;
    }
    public String getTouser() {
        return touser;
    }
    public void setTouser(String touser) {
        this.touser = touser;
    }
    public String getTemplate_id() {
        return template_id;
    }
    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public WeixinUserDataResponse getData() {
        return data;
    }
    public void setData(WeixinUserDataResponse data) {
        this.data = data;
    }
    public static void main(String[] args) {
	WeixinUserPushResponse r = new WeixinUserPushResponse();
	r.setTemplate_id("tempid111");
	r.setTouser("touser14343");
	r.setUrl("http://abidu.com");
	WeixinUserDataResponse d = new WeixinUserDataResponse();
	d.setFirst(d.new Data("v1","c1"));
	d.setKeynote1(d.new Data("v2","c2"));
	d.setRemark(d.new Data("r2","r2"));
	r.setData(d);
	
	System.out.println(JsonUtils.toJsonString(r));
    }

}
