package com.lifeix.cbs.contest.service.spark.live;

import java.util.List;

import com.lifeix.cbs.contest.bean.bb.BbPlayerStatisticsResponse;

public interface BbPlayerStatisticsDubbo {

    /**
     * 批量保存技术统计
     * 
     * @param list
     * @return
     */
    public boolean saveStatistics(List<BbPlayerStatisticsResponse> list);

}
