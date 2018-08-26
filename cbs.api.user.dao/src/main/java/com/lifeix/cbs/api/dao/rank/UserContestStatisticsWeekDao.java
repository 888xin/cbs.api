package com.lifeix.cbs.api.dao.rank;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.api.dto.rank.UserContestStatisticsWeek;

/**
 * 周榜統計
 * 
 * @author lifeix
 * 
 */
public interface UserContestStatisticsWeekDao {

    /**
     * 获取用户单个周榜数据
     * 
     * @param userId
     * @param year
     * @param week
     * @return
     */
    public UserContestStatisticsWeek findById(Long userId, Integer year, Integer week, boolean cacheFlag);

    /**
     * 批量获取指定周榜的用户数据
     * 
     * @param ids
     * @return
     */
    public Map<Long, UserContestStatisticsWeek> findByIds(List<Long> userIds, Integer year, Integer week);

    /**
     * 获取用户下注曲线(按周)
     * 
     * @param userId
     * @param startYear
     * @param startWeek
     * @param endYear
     * @param endWeek
     * @param limit
     * @return
     */
    public List<UserContestStatisticsWeek> findByUser(Long userId, Integer startYear, Integer startWeek, Integer endYear,
	    Integer endWeek, Integer limit);

    /**
     * 获取周榜的用户数据 以投资回报率排序
     * 
     * @param year
     * @param week
     * @param start
     * @param limit
     * @return
     */
    public List<UserContestStatisticsWeek> findByTime(Integer year, Integer week, Integer start, Integer limit);

    public boolean updateWithLockerId(UserContestStatisticsWeek weekStatistics);

    public boolean insert(UserContestStatisticsWeek weekStatistics);

}
