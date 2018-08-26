package com.lifeix.cbs.api.bean.market;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * 渠道统计列表
 * 
 * @author yis
 *
 */
public class CbsMarketStatListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -524750839002585222L;

    private List<CbsMarketStatisticResponse> marketstats;

    public CbsMarketStatListResponse() {
	super();
    }

    public List<CbsMarketStatisticResponse> getMarketstats() {
	return marketstats;
    }

    public void setMarketstats(List<CbsMarketStatisticResponse> marketstats) {
	this.marketstats = marketstats;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
