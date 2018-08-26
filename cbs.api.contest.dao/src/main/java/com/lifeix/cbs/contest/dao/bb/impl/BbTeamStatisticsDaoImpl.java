package com.lifeix.cbs.contest.dao.bb.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbTeamStatisticsDao;
import com.lifeix.cbs.contest.dto.bb.BbTeamStatistics;

@Service("bbTeamStatisticsDao")
public class BbTeamStatisticsDaoImpl extends ContestDaoSupport implements BbTeamStatisticsDao {

    @Override
    public List<BbTeamStatistics> selectByContestId(Long contestId) {

	List<BbTeamStatistics> contentMains = null;
	String cacheKey = getCustomCache("selectByContestId", contestId);
	contentMains = memcacheService.get(cacheKey);

	if (contentMains == null) {
	    contentMains = sqlSession.selectList("BbTeamStatisticsMapper.selectByContestId", contestId);
	    if (contentMains != null) {
		memcacheService.set(cacheKey, contentMains);
	    }
	}

	return contentMains;
    }

    @Override
    public boolean saveStatistics(List<BbTeamStatistics> list) {
	if (list == null || list.size() == 0)
	    return false;
	int res = sqlSession.insert("BbTeamStatisticsMapper.saveStatistics", list);
	if (res > 0) {
	    Set<Long> contestIdSet = new HashSet<Long>(list.size());
	    for (BbTeamStatistics statistics : list) {
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
	// 清除赛事球队实时技术统计缓存
	String cacheKey2 = getCustomCache("selectByContestId", contestId);
	memcacheService.delete(cacheKey2);
    }
}
