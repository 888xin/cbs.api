package com.lifeix.cbs.content.dto.shicai;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 时彩游戏结果
 * @author wenhuans
 * 2015年11月3日 下午5:33:06
 * 
 */
public class Shicai implements Serializable{  

    /**
     * @author wenhuans
     * 2015年11月3日下午5:34:31
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 时彩id(主键id)
     */
    private Long scId; 
    
    /**
     * 奖池一区开奖结果
     */
    private String num; 
    
    /**
     * 奖池单注
     */
    private String prize; 
    
    /**
     * 奖池数额
     */
    private String prizePool; 
    
    /**
     * 截止时间
     */
    private Date closeTime;
    
    /**
     * 处理状态（0未处理，1已处理）
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    public Shicai(){
	super();
    }
    
    public Shicai(Date closeTime, Date createTime){
	this.closeTime = closeTime;
	this.createTime = createTime;
    }
    
    public Shicai(String num, String prize, String prizePool, Date closeTime, Date createTime){
	this.num = num;
	this.prize = prize;
	this.prizePool = prizePool;
	this.closeTime = closeTime;
	this.createTime = createTime;
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

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getPrizePool() {
        return prizePool;
    }

    public void setPrizePool(String prizePool) {
        this.prizePool = prizePool;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

