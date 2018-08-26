package com.lifeix.cbs.api.dto.money;

import java.io.Serializable;
import java.util.Date;

/**
 * 龙币充值赠送记录
 * 
 * @author lifeix
 * 
 */
public class MoneyOrder implements Serializable {

    private static final long serialVersionUID = -4794432784421573009L;

    /**
     * 龙币订单Id
     */
    private Long orderId;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 充值卡Id
     */
    private Long cardId;

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
     * 状态 0 未处理 1 已处理
     */
    private Integer status;

    /**
     * 日期
     */
    private Date createTime;

    public Long getOrderId() {
	return orderId;
    }

    public void setOrderId(Long orderId) {
	this.orderId = orderId;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Long getCardId() {
	return cardId;
    }

    public void setCardId(Long cardId) {
	this.cardId = cardId;
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