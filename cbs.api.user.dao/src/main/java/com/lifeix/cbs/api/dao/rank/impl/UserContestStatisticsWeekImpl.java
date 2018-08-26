package com.lifeix.cbs.api.dao.rank.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.dao.ContentDaoSupport;
import com.lifeix.cbs.api.dao.rank.UserContestStatisticsWeekDao;
import com.lifeix.cbs.api.dto.rank.UserContestStatisticsWeek;

@Service("userContestStatisticsWeekDao")
public class UserContestStatisticsWeekImpl extends ContentDaoSupport implements UserContestStatisticsWeekDao {

    @Override
    public UserContestStatisticsWeek findById(Long userId, Integer year, Integer week, boolean cacheFlag) {

	String cacheKey = getCustomCache("findById", userId, year, week);

	UserContestStatisticsWeek userStatistics = memcacheService.get(cacheKey);

	if (userStatistics == null || !cacheFlag) {

	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("year", year);
	    map.put("week", week);
	    map.put("userId", userId);
	    userStatistics = sqlSession.selectOne("UserContestStatisticsWeekMapper.findById", map);
	    if (userStatistics != null) {
		memcacheService.set(cacheKey, userStatistics);
	    }
	}
	return userStatistics;
    }

    @Override
    public Map<Long, UserContestStatisticsWeek> findByIds(List<Long> userIds, Integer year, Integer week) {

	Map<Long, UserContestStatisticsWeek> ret = new HashMap<Long, UserContestStatisticsWeek>();

	// 优先从缓存中查询 并计算中没有缓存的id
	List<Long> missIds = new ArrayList<Long>();
	for (Long userId : userIds) {
	    String cacheKey = getCustomCache("findById", userId, year, week);
	    UserContestStatisticsWeek userStatistics = memcacheService.get(cacheKey);
	    if (userStatistics == null) {
		missIds.add(userId);
	    } else {
		ret.put(userId, userStatistics);
	    }
	}
	if (missIds.size() > 0) {

	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("year", year);
	    map.put("week", week);
	    map.put("userIds", missIds);
	    List<UserContestStatisticsWeek> weeks = sqlSession.selectList("UserContestStatisticsWeekMapper.findByIds", map);
	    for (UserContestStatisticsWeek statistics : weeks) {
		ret.put(statistics.getUserId(), statistics);
		String cacheKey = getCustomCache("findById", statistics.getUserId(), year, week);
		memcacheService.set(cacheKey, statistics);
	    }
	}

	return ret;

    }

    @Override
    public boolean insert(UserContestStatisticsWeek statistics) {
	int res = sqlSession.insert("UserContestStatisticsWeekMapper.insert", statistics);
	if (res > 0) {
	    String cacheKey = getCustomCache("findById", statistics.getUserId(), statistics.getYear(), statistics.getWeek());
	    memcacheService.delete(cacheKey);
	    return true;
	}
	return false;
    }

    @Override
    public boolean updateWithLockerId(UserContestStatisticsWeek statistics) {
	int res = sqlSession.update("UserContestStatisticsWeekMapper.updateWithLockerId", statistics);
	if (res > 0) {
	    String cacheKey = getCustomCache("findById", statistics.getUserId(), statistics.getYear(), statistics.getWeek());
	    memcacheService.delete(cacheKey);
	    return true;
	}
	return false;
    }

    @Override
    public List<UserContestStatisticsWeek> findByUser(Long userId, Integer startYear, Integer startWeek, Integer endYear,
	    Integer endWeek, Integer limit) {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("userId", userId);
	map.put("startYear", startYear);
	map.put("startWeek", startWeek);
	map.put("endYear", endYear);
	map.put("endWeek", endWeek);
	map.put("limit", limit);
	return sqlSession.selectList("UserContestStatisticsWeekMapper.findByUser", map);
    }

    @Override
    public List<UserContestStatisticsWeek> findByTime(Integer year, Integer week, Integer start, Integer limit) {
	return null;
    }

}
