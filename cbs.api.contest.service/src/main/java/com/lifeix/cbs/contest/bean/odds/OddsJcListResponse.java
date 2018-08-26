package com.lifeix.cbs.contest.bean.odds;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class OddsJcListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 7603152535629307419L;

    /**
     * jc list
     */
    private List<OddsJcResponse> odds_jcs;

    public List<OddsJcResponse> getOdds_jcs() {
	return odds_jcs;
    }

    public void setOdds_jcs(List<OddsJcResponse> odds_jcs) {
	this.odds_jcs = odds_jcs;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
