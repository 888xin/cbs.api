package com.lifeix.cbs.api.dto.money;

import java.io.Serializable;
import java.util.Date;

/**
 * 龙币丢失的操作记录
 * 
 * @author lifeix
 * 
 */
public class MoneyMissed implements Serializable {

    private static final long serialVersionUID = -299700112351794467L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 操作类型
     */
    private Integer moneyType;

    /**
     * 操作的数据
     */
    private String moneyData;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 充值描述
     */
    private String detail;

    /**
     * 状态 0 未处理 1 已处理 2 放弃处理
     */
    private Integer status;

    /**
     * 日期
     */
    private Date createTime;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Integer getMoneyType() {
	return moneyType;
    }

    public void setMoneyType(Integer moneyType) {
	this.moneyType = moneyType;
    }

    public String getMoneyData() {
	return moneyData;
    }

    public void setMoneyData(String moneyData) {
	this.moneyData = moneyData;
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public String getDetail() {
	return detail;
    }

    public void setDetail(String detail) {
	this.detail = detail;
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