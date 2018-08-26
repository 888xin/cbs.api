package com.lifeix.cbs.contest.bean.bet;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class BetOpListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 3989550509057397049L;

    /**
     * bet op list
     */
    private List<BetOpResponse> op_bets;

    public List<BetOpResponse> getOp_bets() {
	return op_bets;
    }

    public void setOp_bets(List<BetOpResponse> op_bets) {
	this.op_bets = op_bets;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
