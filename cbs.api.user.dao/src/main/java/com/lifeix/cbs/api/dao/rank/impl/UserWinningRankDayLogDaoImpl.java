package com.lifeix.cbs.api.dao.rank.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.dao.ContentDaoSupport;
import com.lifeix.cbs.api.dao.rank.UserWinningRankDayLogDao;
import com.lifeix.cbs.api.dto.rank.UserWinningRankDayLog;

@Service("userWinningRankDayLogDao")
public class UserWinningRankDayLogDaoImpl extends ContentDaoSupport implements UserWinningRankDayLogDao {
    private static final String TABLE = "cbs_user_winning_rank_day_log";
    private static final String TABLE_LONGBI = "cbs_user_winning_rank_day_log_longbi";

    @Override
    public UserWinningRankDayLog findRankByYearAndDay(Integer year, Integer day, Boolean isLongbi) {

	String cacheKey = getCacheId(year + "-" + day + isLongbi);

	UserWinningRankDayLog userWinningRankLog = memcacheService.get(cacheKey);

	if (userWinningRankLog == null) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("year", year);
	    map.put("day", day);
	    if (isLongbi) {
		map.put("table", TABLE_LONGBI);
	    } else {
		map.put("table", TABLE);
	    }
	    userWinningRankLog = sqlSession.selectOne("UserWinningRankDayLogMapper.findRankByYearAndDay", map);
	    if (userWinningRankLog != null) {
		memcacheService.set(cacheKey, userWinningRankLog);
	    }
	}
	return userWinningRankLog;

    }

    @Override
    public boolean insert(UserWinningRankDayLog rankLog) {
	boolean isLongbi = rankLog.getIsLongbi();
	if (isLongbi) {
	    rankLog.setTable(TABLE_LONGBI);
	}
	return sqlSession.insert("UserWinningRankDayLogMapper.insert", rankLog) >= 1;
    }

    @Override
    public boolean update(UserWinningRankDayLog rankLog) {
	String cacheKey = getCacheId(rankLog.getYear() + "-" + rankLog.getDay()+rankLog.getIsLongbi());
	memcacheService.delete(cacheKey);
	return sqlSession.insert("UserWinningRankDayLogMapper.update", rankLog) >= 1;
    }

}
