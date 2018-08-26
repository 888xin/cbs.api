package com.lifeix.cbs.api.dto.gold;

import java.io.Serializable;

/**
 * 龙币筹码活动
 * 
 * @author jacky
 *
 */
public class GoldCard implements Serializable {

 
    private static final long serialVersionUID = -1911653031142763295L;

    /**
     * 充值卡id
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
     * 描述
     */
    private String content;

    /**
     * 库存
     */
    private int stock;

    /**
     * 是否上架
     */
    private boolean enable;

    public GoldCard() {
	super();
    }

    public GoldCard(Long cardId, double amount, double obtain, String content, int stock, boolean enable) {
	super();
	this.cardId = cardId;
	this.amount = amount;
	this.obtain = obtain;
	this.content = content;
	this.stock = stock;
	this.enable = enable;
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

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public int getStock() {
	return stock;
    }

    public void setStock(int stock) {
	this.stock = stock;
    }

    public boolean isEnable() {
	return enable;
    }

    public void setEnable(boolean enable) {
	this.enable = enable;
    }

    @Override
    public String toString() {
	return "GoldCard [cardId=" + cardId + ", amount=" + amount + ", obtain=" + obtain + ", content=" + content
	        + ", stock=" + stock + ", enable=" + enable + "]";
    }
    
    

}
