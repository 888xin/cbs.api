package com.lifeix.cbs.contest.dao.bb.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbPlayerStatisticsDao;
import com.lifeix.cbs.contest.dto.bb.BbPlayerStatistics;

@Service("bbPlayerStatisticsDao")
public class BbPlayerStatisticsDaoImpl extends ContestDaoSupport implements BbPlayerStatisticsDao {

    @Override
    public List<BbPlayerStatistics> selectByContestId(Long contestId) {

	List<BbPlayerStatistics> contentMains = null;
	String cacheKey = getCustomCache("selectByContestId", contestId);
	contentMains = memcacheService.get(cacheKey);

	if (contentMains == null) {
	    contentMains = sqlSession.selectList("BbPlayerStatisticsMapper.selectByContestId", contestId);
	    if (contentMains != null) {
		memcacheService.set(cacheKey, contentMains);
	    }
	}

	return contentMains;
    }

    @Override
    public boolean saveStatistics(List<BbPlayerStatistics> list) {
	if (list == null || list.size() == 0)
	    return false;
	int res = sqlSession.insert("BbPlayerStatisticsMapper.saveStatistics", list);
	if (res > 0) {
	    Set<Long> contestIdSet = new HashSet<Long>(list.size());
	    for (BbPlayerStatistics statistics : list) {
		contestIdSet.add(statistics.getContestId());
	    }
	    for (Long contestId : contestIdSet) {
		deleteListCache(contestId);
	    }
	}
	return res > 0;
    }

    /**
     * 列表缓存
     * 
     * @param contest
     */
    public void deleteListCache(Long contestId) {
	// 清除赛事球员实时技术统计缓存
	String cacheKey2 = getCustomCache("selectByContestId", contestId);
	memcacheService.delete(cacheKey2);
    }
}
