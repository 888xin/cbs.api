package com.lifeix.cbs.api.bean.money;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class MoneyStatisticListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -4080733435245112593L;

    private List<MoneyStatisticResponse> money_statistic;
    
    private List<String> time_range;
    
    private double[] all_income;
    
    private double[] all_outlay;

    public List<MoneyStatisticResponse> getMoney_statistic() {
        return money_statistic;
    }

    public void setMoney_statistic(List<MoneyStatisticResponse> money_statistic) {
        this.money_statistic = money_statistic;
    }

    public List<String> getTime_range() {
        return time_range;
    }

    public void setTime_range(List<String> time_range) {
        this.time_range = time_range;
    }

    public double[] getAll_income() {
        return all_income;
    }

    public void setAll_income(double[] all_income) {
        this.all_income = all_income;
    }

    public double[] getAll_outlay() {
        return all_outlay;
    }

    public void setAll_outlay(double[] all_outlay) {
        this.all_outlay = all_outlay;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
