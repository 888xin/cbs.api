package com.lifeix.cbs.contest.dto.fb;

import java.io.Serializable;

/**
 * 足球球队
 * 
 * @author peter
 * 
 */
public class FbTeam implements Serializable {

    private static final long serialVersionUID = 6049677361443603242L;

    /**
     * 主键
     */
    private Long ftId;

    /**
     * 第三方id
     */
    private Long targetId;

    /**
     * 联赛id
     */
    private Long cupId;

    /**
     * 名称
     */
    private String name;

    /**
     * 英文名称
     */
    private String nameEN;

    /**
     * 图标
     */
    private String logo;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 所属国家id
     */
    private Integer countryId;

    public Long getFtId() {
	return ftId;
    }

    public void setFtId(Long ftId) {
	this.ftId = ftId;
    }

    public Long getTargetId() {
	return targetId;
    }

    public void setTargetId(Long targetId) {
	this.targetId = targetId;
    }

    public Long getCupId() {
	return cupId;
    }

    public void setCupId(Long cupId) {
	this.cupId = cupId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getNameEN() {
	return nameEN;
    }

    public void setNameEN(String nameEN) {
	this.nameEN = nameEN;
    }

    public String getLogo() {
	return logo;
    }

    public void setLogo(String logo) {
	this.logo = logo;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public Integer getCountryId() {
	return countryId;
    }

    public void setCountryId(Integer countryId) {
	this.countryId = countryId;
    }
}
