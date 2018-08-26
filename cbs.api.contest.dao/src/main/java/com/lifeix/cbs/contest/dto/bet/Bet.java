package com.lifeix.cbs.contest.dto.bet;

import java.io.Serializable;
import java.util.Date;

/**
 * 比赛下单
 * 
 * @author lifeix-sz
 * 
 */
public abstract class Bet implements Serializable {

    private static final long serialVersionUID = 4281277166532471170L;

    /**
     * 主键
     */
    private Long bId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 比赛id
     */
    private Long contestId;

    /**
     * true 龙币 false 龙筹
     */
    private boolean isLongbi;
    /**
     * 支持方
     */
    private Integer support;

    /**
     * 下单总金额
     */
    private Double bet;

    /**
     * 龙筹金额
     */
    private Integer coupon;

    /**
     * 返还金额（包含本金）
     */
    private Double back;

    /**
     * 下单结果（初始|赢|输|走）
     */
    private Integer status;

    /**
     * 提供赔率数据的公司
     */
    private Integer company;

    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 内容id
     */
    private Long contentId;

    private String ipaddress;

    /**
     * 客户端来源
     */
    private String client;

    public Long getbId() {
	return bId;
    }

    public void setbId(Long bId) {
	this.bId = bId;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Long getContestId() {
	return contestId;
    }

    public void setContestId(Long contestId) {
	this.contestId = contestId;
    }

    public Integer getSupport() {
	return support;
    }

    public void setSupport(Integer support) {
	this.support = support;
    }

    public boolean isLongbi() {
	return isLongbi;
    }

    public void setLongbi(boolean isLongbi) {
	this.isLongbi = isLongbi;
    }

    public Double getBet() {
	return bet;
    }

    public void setBet(Double bet) {
	this.bet = bet;
    }

    public Double getBack() {
	return back;
    }

    public void setBack(Double back) {
	this.back = back;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public Integer getCompany() {
	return company;
    }

    public void setCompany(Integer company) {
	this.company = company;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public Long getContentId() {
	return contentId;
    }

    public void setContentId(Long contentId) {
	this.contentId = contentId;
    }

    public String getIpaddress() {
	return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
	this.ipaddress = ipaddress;
    }

    public Integer getCoupon() {
	return coupon;
    }

    public void setCoupon(Integer coupon) {
	this.coupon = coupon;
    }

    public String getClient() {
	return client;
    }

    public void setClient(String client) {
	this.client = client;
    }

}
