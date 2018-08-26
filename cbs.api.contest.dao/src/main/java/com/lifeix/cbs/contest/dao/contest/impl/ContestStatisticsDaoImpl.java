package com.lifeix.cbs.contest.dao.contest.impl;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.contest.ContestStatisticsDao;
import com.lifeix.cbs.contest.dto.contest.Contest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("contestStatisticsDao")
public class ContestStatisticsDaoImpl extends ContestDaoSupport implements ContestStatisticsDao {


	@Override
	public List<Contest> findContestsBySame(int type) {
		Map<String, Object> params = new HashMap<>();
		params.put("type", type);
		return sqlSession.selectList("ContestStatisticsMapper.findContestsBySame", params);
	}

	@Override
	public List<Contest> findContestsByStatus(int type, int status) {
		Map<String, Object> params = new HashMap<>();
		params.put("type", type);
		params.put("status", status);
		return sqlSession.selectList("ContestStatisticsMapper.findContestsByStatus", params);
	}

	@Override
	public Contest findContestsById(long contestId, int type) {
		Map<String, Object> params = new HashMap<>();
		params.put("type", type);
		params.put("contestId", contestId);
		return sqlSession.selectOne("ContestStatisticsMapper.findContestsById", params);
	}

	@Override
	public Double findBetMoney(int playType, long contestId, int support) {
		Map<String, Object> params = new HashMap<>();
		params.put("playType", playType);
		params.put("contestId", contestId);
		params.put("support", support);
		return sqlSession.selectOne("ContestStatisticsMapper.findBetMoney", params);
	}
}
