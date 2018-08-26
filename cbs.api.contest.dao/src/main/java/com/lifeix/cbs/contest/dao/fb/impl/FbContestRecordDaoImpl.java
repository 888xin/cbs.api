package com.lifeix.cbs.contest.dao.fb.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.fb.FbContestRecordDao;
import com.lifeix.cbs.contest.dto.fb.FbContestRecord;

@Service("fbContestRecordDao")
public class FbContestRecordDaoImpl extends ContestDaoSupport implements FbContestRecordDao {

    @Override
    public List<FbContestRecord> selectHeadToHeadRecord(Long homeTeam, Long awayTeam, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("homeTeam", homeTeam);
	params.put("awayTeam", awayTeam);
	params.put("num", limit);
	List<FbContestRecord> list = sqlSession.selectList("FbContestRecordMapper.selectHeadToHeadRecord", params);
	return list;
    }

    @Override
    public List<FbContestRecord> selectTeamRecord(Long teamId, Integer limit) {
	List<FbContestRecord> list = null;
	String cacheKey = getCustomCache("selectTeamRecord", teamId);
	list = memcacheService.get(cacheKey);
	if (list == null) {
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("teamId", teamId);
	    params.put("num", limit);
	    list = sqlSession.selectList("FbContestRecordMapper.selectTeamRecord", params);
	}
	return list;
    }

    @Override
    public boolean saveContestRecord(List<FbContestRecord> list) {
	if (list == null || list.size() == 0)
	    return false;
	int res = sqlSession.insert("FbContestRecordMapper.saveContestRecord", list);
	return res > 0;
    }

    @Override
    public List<FbContestRecord> selectAnalysisNeeded(Long maxId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("maxId", maxId);
	params.put("num", limit);
	List<FbContestRecord> list = sqlSession.selectList("FbContestRecordMapper.selectAnalysisNeeded", params);
	return list;
    }

    @Override
    public boolean updateWinTypes(List<FbContestRecord> list) {
	if (list == null || list.size() == 0)
	    return false;
	boolean flag = sqlSession.insert("FbContestRecordMapper.updateWinTypes", list) > 0;
	if (flag) {
	    Set<Long> teamIds = new HashSet<Long>(list.size());
	    for (FbContestRecord record : list) {
		teamIds.add(record.getHomeTeam());
		teamIds.add(record.getAwayTeam());
	    }
	    for (Long teamId : teamIds) {
		deleteCustomCache(teamId);
	    }
	}
	return flag;
    }

    private void deleteCustomCache(Long teamId) {
	String cacheKey = getCustomCache("selectTeamRecord", teamId);
	memcacheService.delete(cacheKey);
    }
}
