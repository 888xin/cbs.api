package com.lifeix.cbs.api.dao.rank;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.api.dto.rank.UserContestStatistics;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface UserContestStatisticsDao {
    /**
     * 查看用户的比赛结算统计数据
     * 
     * @param userId
     * @return
     * @throws L99IllegalParamsException
     */
    public UserContestStatistics getUserContestStatistics(Long userId);

    /**
     * 查询统计不查询缓存(用于生成统计)
     * 
     * @param userId
     * @return
     */
    public UserContestStatistics getUserContestStatisticsByNoCache(Long userId);

    public boolean insert(UserContestStatistics statistics);

    public boolean updateWithLockerId(UserContestStatistics statistics);

    /**
     * 批量查找列表
     * 
     * @param ids
     * @return
     */
    public Map<Long, UserContestStatistics> findUserContestStatisticsByIds(List<Long> ids);

    /**
     * 批量查找
     * 
     * @param ids
     * @return
     */
    public Map<Long, Double> findUserContestStatisticsMap();

    /**
     * 投资回报率排行
     * 
     * @return
     */
    public Map<String, Double> findUserContestStatisticsRoiMap();

    /**
     * 胜率排行
     * 
     * @return
     */
    public Map<String, Double> findUserContestStatisticsWinningMap();

}
