package com.lifeix.cbs.contest.bean.odds;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * 让球胜平负赔率历史记录
 * 
 * @author Peter
 * 
 */
public class OddsJcHistListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -4154715292969054893L;

    /**
     * op_hist list
     */
    private List<OddsJcHistResponse> odds_jc_hists;

    public List<OddsJcHistResponse> getOdds_jc_hists() {
	return odds_jc_hists;
    }

    public void setOdds_jc_hists(List<OddsJcHistResponse> odds_jc_hists) {
	this.odds_jc_hists = odds_jc_hists;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
