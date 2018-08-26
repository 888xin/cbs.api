package com.lifeix.cbs.content.dto.shicai;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户下单记录
 * @author wenhuans
 * 2015年11月3日 下午5:33:47
 * 
 */
public class ShicaiPlay implements Serializable{

    /**
     * @author wenhuans
     * 2015年11月3日下午5:34:34
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id; 
    
    /**
     * 用户id
     */
    private Long accountId; 
    
    /**
     * 时彩id
     */
    private Long scId; 
    
    /**
     * 奖池一区相关记录
     */
    private String num;

    /**
     * 奖池二区相关记录
     */
    private String sumNum;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    public ShicaiPlay(){
    }
    
    public ShicaiPlay(Long accountId, Long scId, String num, String sumNum){
	this.accountId = accountId ;
	this.scId = scId;
	this.num = num;
	this.sumNum = sumNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getScId() {
        return scId;
    }

    public void setScId(Long scId) {
        this.scId = scId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSumNum() {
        return sumNum;
    }

    public void setSumNum(String sumNum) {
        this.sumNum = sumNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
}

