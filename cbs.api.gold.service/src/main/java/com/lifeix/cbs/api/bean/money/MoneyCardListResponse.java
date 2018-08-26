package com.lifeix.cbs.api.bean.money;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class MoneyCardListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -5322619637106494918L;

    private List<MoneyCardResponse> money_cards;

    private Double money;

    private Integer actStatus;

    private Boolean actFlag;

    public List<MoneyCardResponse> getMoney_cards() {
	return money_cards;
    }

    public Double getMoney() {
	return money;
    }

    public void setMoney(Double money) {
	this.money = money;
    }

    public void setMoney_cards(List<MoneyCardResponse> money_cards) {
	this.money_cards = money_cards;
    }

    public Integer getActStatus() {
	return actStatus;
    }

    public void setActStatus(Integer actStatus) {
	this.actStatus = actStatus;
    }

    public Boolean getActFlag() {
	return actFlag;
    }

    public void setActFlag(Boolean actFlag) {
	this.actFlag = actFlag;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
