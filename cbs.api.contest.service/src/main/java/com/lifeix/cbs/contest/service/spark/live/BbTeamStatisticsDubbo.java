package com.lifeix.cbs.contest.service.spark.live;

import java.util.List;

import com.lifeix.cbs.contest.bean.bb.BbTeamStatisticsResponse;

public interface BbTeamStatisticsDubbo {

    /**
     * 批量保存技术统计
     * 
     * @param list
     * @return
     */
    public boolean saveStatistics(List<BbTeamStatisticsResponse> list);

}
