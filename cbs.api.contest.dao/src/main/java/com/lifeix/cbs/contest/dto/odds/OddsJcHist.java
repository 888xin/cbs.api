package com.lifeix.cbs.contest.dto.odds;

import java.util.Date;

/**
 * 让球胜平负赔率历史
 * 
 * @author lifeix-sz
 * 
 */
public class OddsJcHist {
    /**
     * 主键
     */
    private Long id;
    /**
     * 赛事id
     */
    private Long contestId;
    /**
     * 让球
     */
    private Double handicap;
    /**
     * 主胜赔率
     */
    private Double homeRoi;
    /**
     * 平局赔率
     */
    private Double drawRoi;
    /**
     * 客胜赔率
     */
    private Double awayRoi;
    /**
     * 赔率公司ID
     */
    private Integer company;
    /**
     * 创建时间
     */
    private Date createTime;

    public Long getContestId() {
	return contestId;
    }

    public void setContestId(Long contestId) {
	this.contestId = contestId;
    }

    public Double getHandicap() {
	return handicap;
    }

    public void setHandicap(Double handicap) {
	this.handicap = handicap;
    }

    public Double getHomeRoi() {
	return homeRoi;
    }

    public void setHomeRoi(Double homeRoi) {
	this.homeRoi = homeRoi;
    }

    public Double getDrawRoi() {
	return drawRoi;
    }

    public void setDrawRoi(Double drawRoi) {
	this.drawRoi = drawRoi;
    }

    public Double getAwayRoi() {
	return awayRoi;
    }

    public void setAwayRoi(Double awayRoi) {
	this.awayRoi = awayRoi;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public Integer getCompany() {
	return company;
    }

    public void setCompany(Integer company) {
	this.company = company;
    }

}
