package com.lifeix.cbs.contest.dto.yy;

import java.io.Serializable;
import java.util.Date;

/**
 * 押押下单
 * 
 * @author lifeix
 * 
 */
public class YyBet implements Serializable {

    private static final long serialVersionUID = 1070958181154423007L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 比赛id
     */
    private Long contestId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 支持方
     */
    private Integer support;

    /**
     * 赔率
     */
    private Double yyRoi;

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
     * 下单时间
     */
    private Date createTime;

    /**
     * 内容id
     */
    private Long contentId;

    /**
     * ip地址
     */
    private String ipaddress;

    /**
     * 龙币标识
     */
    private boolean isLongbi;

    private String client;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getContestId() {
	return contestId;
    }

    public void setContestId(Long contestId) {
	this.contestId = contestId;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Integer getSupport() {
	return support;
    }

    public void setSupport(Integer support) {
	this.support = support;
    }

    public Double getYyRoi() {
	return yyRoi;
    }

    public void setYyRoi(Double yyRoi) {
	this.yyRoi = yyRoi;
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

    public boolean isLongbi() {
	return isLongbi;
    }

    public void setLongbi(boolean isLongbi) {
	this.isLongbi = isLongbi;
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
