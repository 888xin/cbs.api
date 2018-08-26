package com.lifeix.cbs.contest.bean.yy;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class YyBetListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 2345439138596704259L;

    /**
     * yy bet list
     */
    private List<YyBetResponse> bets;

    public List<YyBetResponse> getBets() {
	return bets;
    }

    public void setBets(List<YyBetResponse> bets) {
	this.bets = bets;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
