package com.lifeix.cbs.contest.service.live;

import com.lifeix.cbs.contest.bean.fb.FbStatisticsResponse;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface FbStatisticsService {
    /**
     * 根据赛事id获取技术统计信息
     * @param contestId
     * @return
     * @throws L99IllegalParamsException 
     */
    public FbStatisticsResponse findStatisticsByContestId(Long contestId) throws L99IllegalParamsException;
}
