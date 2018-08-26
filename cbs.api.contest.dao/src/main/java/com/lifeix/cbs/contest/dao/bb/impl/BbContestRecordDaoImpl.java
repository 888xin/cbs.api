package com.lifeix.cbs.contest.dao.bb.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbContestRecordDao;
import com.lifeix.cbs.contest.dto.bb.BbContestRecord;

@Service("bbContestRecordDao")
public class BbContestRecordDaoImpl extends ContestDaoSupport implements BbContestRecordDao {

    @Override
    public List<BbContestRecord> selectHeadToHeadRecord(Long homeTeam, Long awayTeam, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("homeTeam", homeTeam);
	params.put("awayTeam", awayTeam);
	params.put("num", limit);
	List<BbContestRecord> list = sqlSession.selectList("BbContestRecordMapper.selectHeadToHeadRecord", params);
	return list;
    }

    @Override
    public List<BbContestRecord> selectTeamRecord(Long teamId, Integer limit) {
	List<BbContestRecord> list = null;
	String cacheKey = getCustomCache("selectTeamRecord", teamId);
	list = memcacheService.get(cacheKey);
	if (list == null) {
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("teamId", teamId);
	    params.put("num", limit);
	    list = sqlSession.selectList("BbContestRecordMapper.selectTeamRecord", params);
	}
	return list;
    }

    @Override
    public boolean saveContestRecord(List<BbContestRecord> list) {
	if (list == null || list.size() == 0)
	    return false;
	int res = sqlSession.insert("BbContestRecordMapper.saveContestRecord", list);
	return res > 0;
    }

    @Override
    public List<BbContestRecord> selectAnalysisNeeded(Long maxId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("maxId", maxId);
	params.put("num", limit);
	List<BbContestRecord> list = sqlSession.selectList("BbContestRecordMapper.selectAnalysisNeeded", params);
	return list;
    }

    @Override
    public boolean updateWinTypes(List<BbContestRecord> list) {
	if (list == null || list.size() == 0)
	    return false;
	boolean flag = sqlSession.insert("BbContestRecordMapper.updateWinTypes", list) > 0;
	if (flag) {
	    Set<Long> teamIds = new HashSet<Long>(list.size());
	    for (BbContestRecord record : list) {
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
