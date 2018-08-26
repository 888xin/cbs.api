package com.lifeix.cbs.api.bean.gold;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class GoldStatisticListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -4080733435245112593L;

    private List<GoldStatisticResponse> gold_statistic;
    
    private List<String> time_range;
    
    private double[] all_income;
    
    private double[] all_outlay;

    public List<GoldStatisticResponse> getGold_statistic() {
	return gold_statistic;
    }

    public void setGold_statistic(List<GoldStatisticResponse> gold_statistic) {
	this.gold_statistic = gold_statistic;
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
