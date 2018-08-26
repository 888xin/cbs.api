package com.lifeix.cbs.contest.dto.odds;

import java.io.Serializable;
import java.util.Date;

/**
 * 赔率预警数据
 * 
 * @author lifeix
 * 
 */
public class OddsWarn implements Serializable {

    private static final long serialVersionUID = 3968931691407330682L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 赛事id
     */
    private Long contestId;

    /**
     * 赔率类型
     */
    private Integer playType;

    /**
     * 赔率Id
     */
    private Long oddsId;

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
     * 状态 0 初始 1 关闭 2 忽略
     */
    private Integer status;

    /**
     * 赔率锁定
     */
    private Date createTime;

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

    public Integer getPlayType() {
	return playType;
    }

    public void setPlayType(Integer playType) {
	this.playType = playType;
    }

    public Long getOddsId() {
	return oddsId;
    }

    public void setOddsId(Long oddsId) {
	this.oddsId = oddsId;
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

    public Double getInitHandicap() {
	return initHandicap;
    }

    public void setInitHandicap(Double initHandicap) {
	this.initHandicap = initHandicap;
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

    public Integer getCompany() {
	return company;
    }

    public void setCompany(Integer company) {
	this.company = company;
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

}
