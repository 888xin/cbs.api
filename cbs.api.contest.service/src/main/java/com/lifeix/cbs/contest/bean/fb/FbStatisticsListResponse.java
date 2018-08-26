package com.lifeix.cbs.contest.bean.fb;

import java.util.List;
import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class FbStatisticsListResponse extends ListResponse implements Response {
    /**
     * 
     */
    private static final long serialVersionUID = 2458679219009260364L;
    private List<FbStatisticsResponse> statistics;
    
    public List<FbStatisticsResponse> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<FbStatisticsResponse> statistics) {
        this.statistics = statistics;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
