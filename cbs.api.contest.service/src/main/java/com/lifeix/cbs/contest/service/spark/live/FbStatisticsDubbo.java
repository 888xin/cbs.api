package com.lifeix.cbs.contest.service.spark.live;

import java.util.List;

import com.lifeix.cbs.contest.bean.fb.FbStatisticsResponse;
import com.lifeix.cbs.contest.bean.fb.ext.StatisticsResponse;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface FbStatisticsDubbo {

    /**
     * 批量保存技术统计
     * 
     * @param list
     * @return
     */
    public boolean saveStatistics(List<StatisticsResponse> list);

    /**
     * 根据赛事id获取技术统计
     * 
     * @param contestId
     * @return
     * @throws L99IllegalParamsException
     */
    public FbStatisticsResponse findStatisticsByContestId(Long contestId) throws L99IllegalParamsException;

}
