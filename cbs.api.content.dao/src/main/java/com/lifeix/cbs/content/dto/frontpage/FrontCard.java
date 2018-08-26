package com.lifeix.cbs.content.dto.frontpage;

import java.io.Serializable;
import java.util.Date;

public class FrontCard implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    
    private String cardName;
    private String cardDetail;
    private Double price;
    private Integer cardType;
    private Date createTime;
    
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCardName() {
        return cardName;
    }
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    public String getCardDetail() {
        return cardDetail;
    }
    public void setCardDetail(String cardDetail) {
        this.cardDetail = cardDetail;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Integer getCardType() {
        return cardType;
    }
    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    
    
}
