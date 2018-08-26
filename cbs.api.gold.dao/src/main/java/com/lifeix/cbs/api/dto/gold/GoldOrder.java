package com.lifeix.cbs.api.dto.gold;

import java.io.Serializable;
import java.util.Date;

import com.lifeix.cbs.api.common.util.GoldConstants.PaymentType;

/**
 * 龙币筹码订单
 * 
 * @author jacky
 *
 */
public class GoldOrder implements Serializable {

    private static final long serialVersionUID = -7810460057718795538L;

    /**
     * 充值id
     */
    private Long orderId;

    /**
     * 充值用户
     */
    private Long userId;

    /**
     * 被充值用户
     */
    private Long targetId;

    /**
     * 充值卡类型
     */
    private Long cardId;

    /**
     * 花费数
     */
    private double amount;

    /**
     * 获得数
     */
    private double obtain;

    /**
     * 订单唯一序列号
     */
    private String orderNO;

    /**
     * 第三方支付流水号
     */
    private String notifyId;

    /**
     * 下单时间
     */
    private Date addedTime;

    /**
     * 支付时间
     */
    private Date paidTime;

    /**
     * 0 等待确认 1 等待完成 2 交易完成 10 超时关闭 11 用户取消
     */
    private int statu;

    /**
     * @link PaymentType 支付方式
     */
    private Integer paymentId;

    /**
     * 订单描述
     */
    private String content;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 客户端
     */
    private String client;

    public GoldOrder() {
	super();
    }

    public GoldOrder(Long orderId, Long userId, Long targetId, Long cardId, double amount, double obtain, String orderNO,
	    String notifyId, Date addedTime, Date paidTime, int statu, Integer paymentId, String content, String ipAddress,
	    String client) {
	super();
	this.orderId = orderId;
	this.userId = userId;
	this.targetId = targetId;
	this.cardId = cardId;
	this.amount = amount;
	this.obtain = obtain;
	this.orderNO = orderNO;
	this.notifyId = notifyId;
	this.addedTime = addedTime;
	this.paidTime = paidTime;
	this.statu = statu;
	this.paymentId = paymentId;
	this.content = content;
	this.ipAddress = ipAddress;
	this.client = client;
    }

    public String getPaymentType() {
	if (paymentId == PaymentType.ALIPAY || paymentId == PaymentType.ALIPAY_WAP || paymentId == PaymentType.ALIPAY_FAST) {
	    return "支付宝";
	} else if (paymentId == PaymentType.KUAIBIll) {
	    return "快钱";
	} else if (paymentId == PaymentType.MOBILE) {
	    return "手机支付";
	} else if (paymentId == PaymentType.UNIONPAY) {
	    return "银联支付";
	} else if (paymentId == PaymentType.STORED_BOC) {
	    return "中国银行";
	} else if (paymentId == PaymentType.STORED_ICBC) {
	    return "工商银行";
	} else if (paymentId == PaymentType.STORED_ABC) {
	    return "农业银行";
	} else if (paymentId == PaymentType.STORED_CCB) {
	    return "建设银行";
	} else if (paymentId == PaymentType.STORED_CMB) {
	    return "招商银行";
	} else if (paymentId == PaymentType.STORED_COMM) {
	    return "交通银行";
	} else if (paymentId == PaymentType.STORED_CGB) {
	    return "广发银行";
	} else if (paymentId == PaymentType.STORED_SPDB) {
	    return "浦发银行";
	} else if (paymentId == PaymentType.STORED_CIB) {
	    return "兴业银行";
	} else {
	    return "";
	}
    }

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

    public Long getTargetId() {
	return targetId;
    }

    public void setTargetId(Long targetId) {
	this.targetId = targetId;
    }

    public Long getCardId() {
	return cardId;
    }

    public void setCardId(Long cardId) {
	this.cardId = cardId;
    }

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public double getObtain() {
	return obtain;
    }

    public void setObtain(double obtain) {
	this.obtain = obtain;
    }

    public String getOrderNO() {
	return orderNO;
    }

    public void setOrderNO(String orderNO) {
	this.orderNO = orderNO;
    }

    public String getNotifyId() {
	return notifyId;
    }

    public void setNotifyId(String notifyId) {
	this.notifyId = notifyId;
    }

    public Date getAddedTime() {
	return addedTime;
    }

    public void setAddedTime(Date addedTime) {
	this.addedTime = addedTime;
    }

    public Date getPaidTime() {
	return paidTime;
    }

    public void setPaidTime(Date paidTime) {
	this.paidTime = paidTime;
    }

    public int getStatu() {
	return statu;
    }

    public void setStatu(int statu) {
	this.statu = statu;
    }

    public Integer getPaymentId() {
	return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
	this.paymentId = paymentId;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public String getIpAddress() {
	return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
    }

    public String getClient() {
	return client;
    }

    public void setClient(String client) {
	this.client = client;
    }

    @Override
    public String toString() {
	return "GoldOrder [orderId=" + orderId + ", userId=" + userId + ", targetId=" + targetId + ", cardId=" + cardId
	        + ", amount=" + amount + ", obtain=" + obtain + ", orderNO=" + orderNO + ", notifyId=" + notifyId
	        + ", addedTime=" + addedTime + ", paidTime=" + paidTime + ", statu=" + statu + ", paymentId=" + paymentId
	        + ", content=" + content + ", ipAddress=" + ipAddress + ", client=" + client + "]";
    }
    
}
