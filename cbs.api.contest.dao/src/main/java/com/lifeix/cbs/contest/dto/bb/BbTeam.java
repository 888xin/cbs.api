package com.lifeix.cbs.contest.dto.bb;

import java.io.Serializable;

/**
 * 篮球：球队信息
 * 
 */
public class BbTeam implements Serializable {

    private static final long serialVersionUID = 7795193282457739240L;

    /**
     * 主键
     */
    private Long btId;

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
     * 中文全称
     */
    private String nameFull;

    /**
     * 英文名称
     */
    private String nameEN;

    /**
     * 图标
     */
    private String logo;

    /**
     * 球队资料
     */
    private String data;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 所属国家id
     */
    private Integer countryId;

    public String getNameFull() {
	return nameFull;
    }

    public void setNameFull(String nameFull) {
	this.nameFull = nameFull;
    }

    public Long getBtId() {
	return btId;
    }

    public void setBtId(Long btId) {
	this.btId = btId;
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

    public String getData() {
	return data;
    }

    public void setData(String data) {
	this.data = data;
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
