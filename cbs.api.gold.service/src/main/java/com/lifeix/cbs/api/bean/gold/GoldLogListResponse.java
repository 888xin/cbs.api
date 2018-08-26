package com.lifeix.cbs.api.bean.gold;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class GoldLogListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -4080733435245112593L;

    /**
     * 用户金币流水合计
     */
    private GoldResponse gold_total;

    private List<GoldLogResponse> gold_logs;

    public List<GoldLogResponse> getGold_logs() {
        return gold_logs;
    }

    public void setGold_logs(List<GoldLogResponse> gold_logs) {
        this.gold_logs = gold_logs;
    }

    public GoldResponse getGold_total() {
	return gold_total;
    }

    public void setGold_total(GoldResponse gold_total) {
	this.gold_total = gold_total;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
