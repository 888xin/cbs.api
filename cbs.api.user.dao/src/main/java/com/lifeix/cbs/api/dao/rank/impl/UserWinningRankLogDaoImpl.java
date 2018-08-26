package com.lifeix.cbs.api.dao.rank.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.dao.ContentDaoSupport;
import com.lifeix.cbs.api.dao.rank.UserWinningRankLogDao;
import com.lifeix.cbs.api.dto.rank.UserWinningRankLog;

@Service("userWinningRankLogDao")
public class UserWinningRankLogDaoImpl extends ContentDaoSupport implements UserWinningRankLogDao {
    private static final String TABLE = "cbs_user_winning_rank_log";
    private static final String TABLE_LONGBI = "cbs_user_winning_rank_log_longbi";

    @Override
    public UserWinningRankLog findRankByYearAndWeek(Integer year, Integer week, Boolean isLongbi) {

	String cacheKey = getCacheId(year + "-" + week + isLongbi);

	UserWinningRankLog userWinningRankLog = memcacheService.get(cacheKey);

	if (userWinningRankLog == null) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("year", year);
	    map.put("week", week);
	    if (isLongbi) {
		map.put("table", TABLE_LONGBI);
	    } else {
		map.put("table", TABLE);
	    }
	    userWinningRankLog = sqlSession.selectOne("UserWinningRankLogMapper.findRankByYearAndWeek", map);
	    if (userWinningRankLog != null) {
		memcacheService.set(cacheKey, userWinningRankLog);
	    }
	}
	return userWinningRankLog;

    }
    

    @Override
    public boolean insert(UserWinningRankLog rankLog) {
	boolean isLongbi = rankLog.getIsLongbi();
	if (isLongbi) {
	    rankLog.setTable(TABLE_LONGBI);
	}
	return sqlSession.insert("UserWinningRankLogMapper.insert", rankLog) >= 1;
    }

    @Override
    public boolean update(UserWinningRankLog rankLog) {
	String cacheKey = getCacheId(rankLog.getYear() + "-" + rankLog.getWeek() + rankLog.getIsLongbi());
	memcacheService.delete(cacheKey);
	boolean isLongbi = rankLog.getIsLongbi();
	if (isLongbi) {
	    rankLog.setTable(TABLE_LONGBI);
	}
	return sqlSession.insert("UserWinningRankLogMapper.update", rankLog) >= 1;
    }

}
