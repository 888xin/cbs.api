package com.lifeix.cbs.api.bean.money;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class MoneyMissedListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -1664477643457869804L;

    private List<MoneyMissedResponse> money_misseds;

    public List<MoneyMissedResponse> getMoney_misseds() {
	return money_misseds;
    }

    public void setMoney_misseds(List<MoneyMissedResponse> money_misseds) {
	this.money_misseds = money_misseds;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
