package com.lifeix.cbs.api.bean.market;

import com.lifeix.user.beans.Response;

/**
 * 渠道统计
 * 
 * @author lifeix
 * 
 */
public class CbsMarketMainResponse implements Response {

    /**
     * 
     */
    private static final long serialVersionUID = 3010412620896048286L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 渠道
     */
    private String marketCode;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 状态：1 屏蔽 2 未验证 4 已验证
     */
    private Integer status;

    /**
     * token
     */
    private String token;

    @Override
    public String getObjectName() {
	return "";
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getMarketCode() {
	return marketCode;
    }

    public void setMarketCode(String marketCode) {
	this.marketCode = marketCode;
    }

    public String getNickName() {
	return nickName;
    }

    public void setNickName(String nickName) {
	this.nickName = nickName;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getToken() {
	return token;
    }

    public void setToken(String token) {
	this.token = token;
    }

}
