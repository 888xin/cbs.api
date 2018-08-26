package com.lifeix.cbs.api.dto.user;

import java.io.Serializable;
import java.util.Date;

public class CbsUserLogin implements Serializable{

    
    private static final long serialVersionUID = 5084058706139751388L;
    
    private Long userId;
    
    private String loginToken;
    
    private String machineCode;
    
    private String client;
    
    private String ipaddress;
    
    private Date loginTime;
    
    private int continuLogin;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public int getContinuLogin() {
        return continuLogin;
    }

    public void setContinuLogin(int continuLogin) {
        this.continuLogin = continuLogin;
    }
    
}
