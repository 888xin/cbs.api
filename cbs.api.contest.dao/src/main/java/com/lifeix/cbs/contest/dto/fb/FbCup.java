package com.lifeix.cbs.contest.dto.fb;

import java.io.Serializable;

public class FbCup implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8191262114181678092L;

    /**
     * 主键
     */
    private Long cupId;

    /**
     * 第三方id
     */
    private Long targetId;

    /**
     * 联赛名称
     */
    private String name;

    /**
     * 名称全称
     */
    private String nameFull;

    /**
     * 英文名
     */
    private String nameEN;

    /**
     * 颜色值
     */
    private String color;

    /**
     * 所属国家id
     */
    private Integer countryId;

    /**
     * 所属国家
     */
    private String country;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 赛事种类，1联赛2杯赛
     */
    private Integer type;

    public FbCup() {
	super();
    }

    public FbCup(Long cupId, Long targetId, String name, String nameFull, String nameEN, String color, Integer countryId,
	    String country, Integer level, Integer type) {
	super();
	this.cupId = cupId;
	this.targetId = targetId;
	this.name = name;
	this.nameEN = nameEN;
	this.nameFull = nameFull;
	this.color = color;
	this.countryId = countryId;
	this.country = country;
	this.level = level;
	this.type = type;
    }

    public Long getCupId() {
	return cupId;
    }

    public void setCupId(Long cupId) {
	this.cupId = cupId;
    }

    public Long getTargetId() {
	return targetId;
    }

    public void setTargetId(Long targetId) {
	this.targetId = targetId;
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

    public String getNameFull() {
	return nameFull;
    }

    public void setNameFull(String nameFull) {
	this.nameFull = nameFull;
    }

    public String getColor() {
	return color;
    }

    public void setColor(String color) {
	this.color = color;
    }

    public Integer getCountryId() {
	return countryId;
    }

    public void setCountryId(Integer countryId) {
	this.countryId = countryId;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public Integer getLevel() {
	return level;
    }

    public void setLevel(Integer level) {
	this.level = level;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

}
