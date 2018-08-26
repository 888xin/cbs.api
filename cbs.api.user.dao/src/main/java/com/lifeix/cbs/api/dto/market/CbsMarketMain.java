package com.lifeix.cbs.api.dto.market;

import java.io.Serializable;

/**
 * 渠道
 * 
 * @author yis
 * 
 */
public class CbsMarketMain implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4280934950408092764L;

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
     * 密码
     */
    private String password;

    /**
     * 状态：1 屏蔽 2 未验证 4 已验证
     */
    private Integer status;

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

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }
}
