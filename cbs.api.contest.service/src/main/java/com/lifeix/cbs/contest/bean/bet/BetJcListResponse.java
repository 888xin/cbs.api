package com.lifeix.cbs.contest.bean.bet;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class BetJcListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 3184586455882182903L;

    /**
     * bet bd list
     */
    private List<BetJcResponse> jc_bets;

    public List<BetJcResponse> getJc_bets() {
	return jc_bets;
    }

    public void setJc_bets(List<BetJcResponse> jc_bets) {
	this.jc_bets = jc_bets;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
