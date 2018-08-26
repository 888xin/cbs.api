package com.lifeix.cbs.api.bean.gold;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class GoldPrizeListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -4080733435245112593L;

    private List<GoldPrizeResponse> gold_prizes;

    public List<GoldPrizeResponse> getGold_prizes() {
	return gold_prizes;
    }

    public void setGold_prizes(List<GoldPrizeResponse> gold_prizes) {
	this.gold_prizes = gold_prizes;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
