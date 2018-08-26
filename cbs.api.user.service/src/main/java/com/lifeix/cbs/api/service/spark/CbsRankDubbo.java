package com.lifeix.cbs.api.service.spark;

import com.lifeix.cbs.api.bean.user.UserGraphListResponse;
import com.lifeix.cbs.api.bean.user.UserStatisticsListResponse;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

public interface CbsRankDubbo {
    /**
     * 胜率总榜
     * 
     * @param nowPage
     * @param limit
     * @param userId
     * @return
     */
    public UserStatisticsListResponse getWinningTop(Integer nowPage, int limit, Long userId);

    /**
     * 胜率周榜
     * 
     * @param nowPage
     * @param limit
     * @param userId
     * @return
     */
    public UserStatisticsListResponse getWinningWeek(Integer year, Integer week, Integer nowPage, int limit, Long userId);

    /**
     * 回报率总榜
     * 
     * @param nowPage
     * @param limit
     * @param userId
     * @return
     */
    public UserStatisticsListResponse getRoiTop(Integer nowPage, int limit, Long userId);

    /**
     * 回报率周榜
     * 
     * @param nowPage
     * @param limit
     * @param userId
     * @return
     */
    public UserStatisticsListResponse getRoiWeek(Integer year, Integer week, Integer nowPage, int limit, Long userId);

    /**
     * 重置赢率和回报率总榜
     * 
     * @param type
     */
    public CustomResponse resetTop(Integer type);

    /**
     * 
     * 用户数据曲线图(按周)
     * 
     * @param userId
     * @param startTime
     * @param endTime
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    public UserGraphListResponse userBetGraph(Long userId, String startTime, String endTime, Integer limit)
	    throws L99IllegalParamsException;
}
