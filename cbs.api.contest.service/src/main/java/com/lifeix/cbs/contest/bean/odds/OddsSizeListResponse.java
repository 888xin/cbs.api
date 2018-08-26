package com.lifeix.cbs.contest.bean.odds;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class OddsSizeListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -967139573452052971L;

    /**
     * size list
     */
    private List<OddsJcResponse> odds_sizes;

    @Override
    public String getObjectName() {
	return null;
    }

    public List<OddsJcResponse> getOdds_sizes() {
	return odds_sizes;
    }

    public void setOdds_sizes(List<OddsJcResponse> odds_sizes) {
	this.odds_sizes = odds_sizes;
    }
}
