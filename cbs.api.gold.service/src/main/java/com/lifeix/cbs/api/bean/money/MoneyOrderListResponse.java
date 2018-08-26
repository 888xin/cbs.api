package com.lifeix.cbs.api.bean.money;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class MoneyOrderListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 1586192288816871220L;

    private List<MoneyOrderResponse> orders;

    public List<MoneyOrderResponse> getOrders() {
	return orders;
    }

    public void setOrders(List<MoneyOrderResponse> orders) {
	this.orders = orders;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
