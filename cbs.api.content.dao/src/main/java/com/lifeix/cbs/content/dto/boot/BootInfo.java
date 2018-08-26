package com.lifeix.cbs.content.dto.boot;

import java.io.Serializable;
import java.util.Date;

/**
 * 开机信息
 * 
 */
public class BootInfo implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = 8933920983707026381L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * key值
     */
    private String infoKey;

    /**
     * 启用标识
     */
    private Boolean enableFlag;

    /**
     * 发表时间
     */
    private Date createTime;

    /**
     * 跳转类型
     */
    private Integer type;

    /**
     * 跳转链接
     */
    private String dataLink;

    /**
     * 广告时间
     */
    private Integer advTime;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getInfoKey() {
	return infoKey;
    }

    public void setInfoKey(String infoKey) {
	this.infoKey = infoKey;
    }

    public Boolean getEnableFlag() {
	return enableFlag;
    }

    public void setEnableFlag(Boolean enableFlag) {
	this.enableFlag = enableFlag;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public String getDataLink() {
	return dataLink;
    }

    public void setDataLink(String dataLink) {
	this.dataLink = dataLink;
    }

    public Integer getAdvTime() {
	return advTime;
    }

    public void setAdvTime(Integer advTime) {
	this.advTime = advTime;
    }

}
