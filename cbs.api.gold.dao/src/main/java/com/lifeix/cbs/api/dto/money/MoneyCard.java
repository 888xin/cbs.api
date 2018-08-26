package com.lifeix.cbs.api.dto.money;

import java.io.Serializable;
import java.util.Date;

/**
 * 龙币充值类型
 * 
 * @author lifeix
 * 
 */
public class MoneyCard implements Serializable {

    private static final long serialVersionUID = -6432252902343188622L;

    /**
     * id
     */
    private Long id;

    /**
     * 充值名称
     */
    private String name;

    /**
     * 充值描述
     */
    private String detail;

    /**
     * 价格
     */
    private Double price;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 赠送类型 0 龙筹 1 龙币
     */
    private Integer type;

    /**
     * 赠送金额
     */
    private Double handsel;

    /**
     * 日期
     */
    private Date createTime;

    private Integer deleteFlag;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDetail() {
	return detail;
    }

    public void setDetail(String detail) {
	this.detail = detail;
    }

    public Double getPrice() {
	return price;
    }

    public void setPrice(Double price) {
	this.price = price;
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Double getHandsel() {
	return handsel;
    }

    public void setHandsel(Double handsel) {
	this.handsel = handsel;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public Integer getDeleteFlag() {
	return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
	this.deleteFlag = deleteFlag;
    }
}