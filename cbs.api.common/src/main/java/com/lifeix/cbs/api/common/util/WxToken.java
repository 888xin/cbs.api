package com.lifeix.cbs.api.common.util;

public class WxToken {

    // 接口访问凭证
    private String accessToken;
    // 凭证有效期，单位：秒
    private int expiresIn;

    private long curTimeMilles;

    public long getCurTimeMilles() {
	return curTimeMilles;
    }

    public void setCurTimeMilles(long curTimeMilles) {
	this.curTimeMilles = curTimeMilles;
    }

    public String getAccessToken() {
	return accessToken;
    }

    public void setAccessToken(String accessToken) {
	this.accessToken = accessToken;
    }

    public int getExpiresIn() {
	return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
	this.expiresIn = expiresIn;
    }

}
