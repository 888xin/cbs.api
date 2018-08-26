package com.lifeix.cbs.contest.bean.bet;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

import java.util.List;

public class BetLogListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -1580350080317941078L;
    /**
     * bet list
     */
    private List<BetLogResponse> bets;

    public List<BetLogResponse> getBets() {
        return bets;
    }

    public void setBets(List<BetLogResponse> bets) {
        this.bets = bets;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
