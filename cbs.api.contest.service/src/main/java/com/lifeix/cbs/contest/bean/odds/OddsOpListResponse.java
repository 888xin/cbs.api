package com.lifeix.cbs.contest.bean.odds;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class OddsOpListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 5114314856457721444L;

    /**
     * op list
     */
    private List<OddsOpResponse> odds_ops;

    public List<OddsOpResponse> getOdds_ops() {
	return odds_ops;
    }

    public void setOdds_ops(List<OddsOpResponse> odds_ops) {
	this.odds_ops = odds_ops;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
