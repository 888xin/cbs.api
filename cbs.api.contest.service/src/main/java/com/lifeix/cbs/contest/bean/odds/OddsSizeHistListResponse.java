package com.lifeix.cbs.contest.bean.odds;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * 大小球赔率历史记录
 */
public class OddsSizeHistListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 7529541618890371291L;

    /**
     * size_hist list
     */
    private List<OddsJcHistResponse> odds_size_hists;

    @Override
    public String getObjectName() {
	return null;
    }

    public List<OddsJcHistResponse> getOdds_size_hists() {
	return odds_size_hists;
    }

    public void setOdds_size_hists(List<OddsJcHistResponse> odds_size_hists) {
	this.odds_size_hists = odds_size_hists;
    }

}
