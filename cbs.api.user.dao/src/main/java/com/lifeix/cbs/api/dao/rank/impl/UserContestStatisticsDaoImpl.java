package com.lifeix.cbs.api.dao.rank.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.dao.ContentDaoSupport;
import com.lifeix.cbs.api.dao.rank.UserContestStatisticsDao;
import com.lifeix.cbs.api.dto.rank.UserContestStatistics;
import com.lifeix.framework.memcache.MultiCacheResult;

@Service("userContestStatisticsDao")
public class UserContestStatisticsDaoImpl extends ContentDaoSupport implements UserContestStatisticsDao {

    @Override
    public UserContestStatistics getUserContestStatistics(Long userId) {

	String cacheKey = getCacheId(userId);

	UserContestStatistics userStatistics = memcacheService.get(cacheKey);

	if (userStatistics == null) {
	    userStatistics = sqlSession.selectOne("UserContestStatisticsMapper.getUserContestStatistics", userId);
	    if (userStatistics != null) {
		memcacheService.set(cacheKey, userStatistics);
	    }
	}
	return userStatistics;

    }

    @Override
    public UserContestStatistics getUserContestStatisticsByNoCache(Long userId) {
	String cacheKey = getCacheId(userId);
	UserContestStatistics userStatistics = sqlSession.selectOne("UserContestStatisticsMapper.getUserContestStatistics",
	        userId);
	if (userStatistics != null) {
	    memcacheService.set(cacheKey, userStatistics);
	}
	return userStatistics;
    }

    @Override
    public boolean insert(UserContestStatistics statistics) {
	int res = sqlSession.insert("UserContestStatisticsMapper.insert", statistics);
	if (res > 0) {
	    String cacheKey = getCacheId(statistics.getUserId());
	    memcacheService.set(cacheKey, statistics);
	    return true;
	}
	return false;
    }

    @Override
    public boolean updateWithLockerId(UserContestStatistics statistics) {
	int res = sqlSession.update("UserContestStatisticsMapper.updateWithLockerId", statistics);
	if (res > 0) {
	    String cacheKey = getCacheId(statistics.getUserId());
	    memcacheService.set(cacheKey, statistics);
	    return true;
	}
	return false;
    }

    @Override
    public Map<Long, UserContestStatistics> findUserContestStatisticsByIds(List<Long> ids) {
	Map<Long, UserContestStatistics> ret = new HashMap<Long, UserContestStatistics>();
	if (ids == null || ids.size() == 0) {
	    return ret;
	}
	MultiCacheResult cacheResult = memcacheService.getMulti(getMultiCacheId(ids));
	Collection<String> noCached = cacheResult.getMissIds();
	Map<String, Object> cacheDatas = cacheResult.getCacheData();
	Iterator<String> it = cacheDatas.keySet().iterator();
	while (it.hasNext()) {
	    String key = it.next();
	    Object obj = cacheDatas.get(key);
	    if (obj != null) {
		try {
		    ret.put(Long.valueOf(revertCacheId(key)), (UserContestStatistics) obj);
		} catch (Exception e) {
		    LOG.warn(String.format("UserContestStatisticsDaoImpl get multi cache fail %s - %s", key, e.getMessage()));
		}
	    }
	}
	if (noCached.size() > 0) {
	    List<UserContestStatistics> statisticss = sqlSession.selectList("UserContestStatisticsMapper.selectByIds",
		    revertMultiCacheId(noCached));
	    for (UserContestStatistics statistics : statisticss) {
		ret.put(statistics.getUserId(), statistics);
		memcacheService.set(getCacheId(statistics.getUserId()), statistics);
	    }
	}
	return ret;
    }

    @Override
    public Map<Long, Double> findUserContestStatisticsMap() {
	Map<Long, Double> ret = new HashMap<Long, Double>();

	List<UserContestStatistics> list = sqlSession.selectList("UserContestStatisticsMapper.findUserContestStatisticsMap");
	for (UserContestStatistics u : list) {
	    ret.put(u.getUserId(), Double.parseDouble(u.getScore().toString()));
	}
	return ret;
    }

    /**
     * 投资回报率排行
     */
    @Override
    public Map<String, Double> findUserContestStatisticsRoiMap() {
	Map<String, Double> ret = new HashMap<String, Double>();
	List<UserContestStatistics> list = sqlSession
	        .selectList("UserContestStatisticsMapper.findUserContestStatisticsRoiMap");
	for (UserContestStatistics u : list) {
	    ret.put(Long.toString(u.getUserId()), u.getRoi().doubleValue());
	}
	return ret;
    }

    /**
     * 胜率排行
     */
    @Override
    public Map<String, Double> findUserContestStatisticsWinningMap() {
	Map<String, Double> ret = new HashMap<String, Double>();
	List<UserContestStatistics> list = sqlSession
	        .selectList("UserContestStatisticsMapper.findUserContestStatisticsWinningMap");
	for (UserContestStatistics u : list) {
	    ret.put(Long.toString(u.getUserId()), u.getWinning());
	}
	return ret;
    }

}
