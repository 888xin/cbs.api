/**
 * 
 */
package com.lifeix.cbs.mall.dto.order;

import java.io.Serializable;
import java.util.Date;

/**
 * 商城商品订单
 * 
 * @author lifeix
 * 
 */
public class MallOrder implements Serializable {

    private static final long serialVersionUID = 6658006002224192040L;

    /**
     * 订单Id
     */
    private Long id;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     * 商品单价
     */
    private Double goodsPrice;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 邮费
     */
    private Double postage;

    /**
     * 订单总价
     */
    private Double amount;

    /**
     * 收货地址
     */
    private String orderAddress;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 订单属性
     */
    private String goodsPro;

    /**
     * 用户备注
     */
    private String userRemark;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 发货时间
     */
    private Date shopTime;

    /**
     * 完成时间
     */
    private Date doneTime;

    /**
     * 支付方式
     */
    private Long logId;

    /**
     * 更新版本
     */
    private int lockId;

    public Long getGoodsId() {
	return goodsId;
    }

    public void setGoodsId(Long goodsId) {
	this.goodsId = goodsId;
    }

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

    public Double getGoodsPrice() {
	return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
	this.goodsPrice = goodsPrice;
    }

    public Integer getGoodsNum() {
	return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
	this.goodsNum = goodsNum;
    }

    public Double getPostage() {
	return postage;
    }

    public void setPostage(Double postage) {
	this.postage = postage;
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public String getOrderAddress() {
	return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
	this.orderAddress = orderAddress;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getGoodsPro() {
	return goodsPro;
    }

    public void setGoodsPro(String goodsPro) {
	this.goodsPro = goodsPro;
    }

    public String getUserRemark() {
	return userRemark;
    }

    public void setUserRemark(String userRemark) {
	this.userRemark = userRemark;
    }

    public String getCancelReason() {
	return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
	this.cancelReason = cancelReason;
    }

    public Long getLogId() {
	return logId;
    }

    public void setLogId(Long logId) {
	this.logId = logId;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public Date getShopTime() {
	return shopTime;
    }

    public void setShopTime(Date shopTime) {
	this.shopTime = shopTime;
    }

    public Date getDoneTime() {
	return doneTime;
    }

    public void setDoneTime(Date doneTime) {
	this.doneTime = doneTime;
    }

    public int getLockId() {
	return lockId;
    }

    public void setLockId(int lockId) {
	this.lockId = lockId;
    }

}
