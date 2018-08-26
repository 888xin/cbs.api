package com.lifeix.cbs.contest.dto.odds;

import java.io.Serializable;

/**
 * 大小球当前赔率
 * 
 * @author lifeix-sz
 * 
 */
public class OddsSize implements Serializable {

    private static final long serialVersionUID = -836018613331816886L;

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
     * 大球赔率
     */
    private Double bigRoi;

    /**
     * 小球赔率
     */
    private Double tinyRoi;

    /**
     * 初始盘口
     */
    private Double initHandicap;

    /**
     * 初始大球赔率
     */
    private Double initBigRoi;

    /**
     * 初始小球赔率
     */
    private Double initTinyRoi;

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

    public Double getBigRoi() {
	return bigRoi;
    }

    public void setBigRoi(Double bigRoi) {
	this.bigRoi = bigRoi;
    }

    public Double getTinyRoi() {
	return tinyRoi;
    }

    public void setTinyRoi(Double tinyRoi) {
	this.tinyRoi = tinyRoi;
    }

    public Double getInitHandicap() {
	return initHandicap;
    }

    public void setInitHandicap(Double initHandicap) {
	this.initHandicap = initHandicap;
    }

    public Double getInitBigRoi() {
	return initBigRoi;
    }

    public void setInitBigRoi(Double initBigRoi) {
	this.initBigRoi = initBigRoi;
    }

    public Double getInitTinyRoi() {
	return initTinyRoi;
    }

    public void setInitTinyRoi(Double initTinyRoi) {
	this.initTinyRoi = initTinyRoi;
    }

    public Integer getCompany() {
	return company;
    }

    public void setCompany(Integer company) {
	this.company = company;
    }

    public Boolean getCloseFlag() {
	return closeFlag;
    }

    public void setCloseFlag(Boolean closeFlag) {
	this.closeFlag = closeFlag;
    }

    public Boolean getLockFlag() {
	return lockFlag;
    }

    public void setLockFlag(Boolean lockFlag) {
	this.lockFlag = lockFlag;
    }

    public Boolean getBelongFive() {
	return belongFive;
    }

    public void setBelongFive(Boolean belongFive) {
	this.belongFive = belongFive;
    }

}
