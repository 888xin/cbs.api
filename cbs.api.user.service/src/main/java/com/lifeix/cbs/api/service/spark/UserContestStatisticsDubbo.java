package com.lifeix.cbs.api.service.spark;

import com.lifeix.cbs.api.bean.user.UserContestStatisticsListResponse;
import com.lifeix.cbs.api.bean.user.UserContestStatisticsResponse;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface UserContestStatisticsDubbo {
    /**
     * 查看用户的比赛结算统计数据
     * 
     * @param userId
     * @return
     * @throws L99IllegalParamsException
     */
    public UserContestStatisticsResponse getUserContestStatistics(Long userId) throws L99IllegalParamsException;

    /**
     * 查看一堆用户的比赛结算统计数据
     * 
     * @param userId
     * @return
     * @throws L99IllegalParamsException
     */
    public UserContestStatisticsListResponse findMoreUserStatistics(String userIds) throws L99IllegalParamsException;

}
