package com.lifeix.cbs.contest.dto.odds;

import java.io.Serializable;

/**
 * 让球胜平负当前赔率
 * 
 * @author lifeix-sz
 * 
 */
public class OddsJc implements Serializable {

    private static final long serialVersionUID = 1947642907860049627L;

    /**
     * 主键
     */
    private Long oddsId;

    /**
     * 赛事id
     */
    private Long contestId;

    /**
     * 盘口
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
     * 初始盘口
     */
    private Double initHandicap;

    /**
     * 初始主胜赔率
     */
    private Double initHomeRoi;

    /**
     * 初始平局赔率
     */
    private Double initDrawRoi;

    /**
     * 初始客胜赔率
     */
    private Double initAwayRoi;

    /**
     * 公司Id
     */
    private Integer company;

    /**
     * 关闭标识
     */
    private Boolean closeFlag;

    /**
     * 赔率锁定
     */
    private Boolean lockFlag;

    /**
     * 是否属于重要联赛
     */
    private Boolean belongFive;

    public Boolean getLockFlag() {
	return lockFlag;
    }

    public void setLockFlag(Boolean lockFlag) {
	this.lockFlag = lockFlag;
    }

    public Double getInitHomeRoi() {
	return initHomeRoi;
    }

    public void setInitHomeRoi(Double initHomeRoi) {
	this.initHomeRoi = initHomeRoi;
    }

    public Double getInitDrawRoi() {
	return initDrawRoi;
    }

    public void setInitDrawRoi(Double initDrawRoi) {
	this.initDrawRoi = initDrawRoi;
    }

    public Double getInitAwayRoi() {
	return initAwayRoi;
    }

    public void setInitAwayRoi(Double initAwayRoi) {
	this.initAwayRoi = initAwayRoi;
    }

    public Long getOddsId() {
	return oddsId;
    }

    public void setOddsId(Long oddsId) {
	this.oddsId = oddsId;
    }

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

    public Boolean getCloseFlag() {
	return closeFlag;
    }

    public void setCloseFlag(Boolean closeFlag) {
	this.closeFlag = closeFlag;
    }

    public Double getInitHandicap() {
	return initHandicap;
    }

    public void setInitHandicap(Double initHandicap) {
	this.initHandicap = initHandicap;
    }

    public Integer getCompany() {
	return company;
    }

    public void setCompany(Integer company) {
	this.company = company;
    }

    public Boolean getBelongFive() {
	return belongFive;
    }

    public void setBelongFive(Boolean belongFive) {
	this.belongFive = belongFive;
    }

}
