package com.lifeix.cbs.contest.dto.odds;

import java.io.Serializable;
import java.util.Date;

/**
 * 大小球赔率历史
 * 
 * @author lifeix-sz
 * 
 */
public class OddsSizeHist implements Serializable {

    private static final long serialVersionUID = 427090732436167323L;

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
    private Double bigRoi;
    /**
     * 客胜赔率
     */
    private Double tinyRoi;
    /**
     * 赔率公司ID
     */
    private Integer company;
    /**
     * 创建时间
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

}
