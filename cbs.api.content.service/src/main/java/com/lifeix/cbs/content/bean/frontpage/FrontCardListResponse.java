package com.lifeix.cbs.content.bean.frontpage;

import com.lifeix.cbs.api.bean.coupon.CouponUserResponse;
import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

import java.util.List;

public class FrontCardListResponse extends ListResponse implements Response {
    private static final long serialVersionUID = 1L;

    private List<FrontCardResponse> cards;

    /**
     * 可用余额
     */
    private Double availableBalance;

    /**
     * 可用余额
     */
    private CouponUserResponse couponUserResponse;
    
    /**
     * 可用龙币
     */
    private Double availableMoney;

    public CouponUserResponse getCouponUserResponse() {
        return couponUserResponse;
    }

    public void setCouponUserResponse(CouponUserResponse couponUserResponse) {
        this.couponUserResponse = couponUserResponse;
    }

    public List<FrontCardResponse> getCards() {
        return cards;
    }

    public void setCards(List<FrontCardResponse> cards) {
        this.cards = cards;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Double getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(Double availableMoney) {
        this.availableMoney = availableMoney;
    }

}
