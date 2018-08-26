package com.lifeix.cbs.contest.bean.odds;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * 欧赔赔率历史记录
 * 
 * @author Peter
 * 
 */
public class OddsOpHistListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -3747597267868394551L;

    /**
     * op_hist list
     */
    private List<OddsOpHistResponse> odds_op_hists;

    public List<OddsOpHistResponse> getOdds_op_hists() {
	return odds_op_hists;
    }

    public void setOdds_op_hists(List<OddsOpHistResponse> odds_op_hists) {
	this.odds_op_hists = odds_op_hists;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
