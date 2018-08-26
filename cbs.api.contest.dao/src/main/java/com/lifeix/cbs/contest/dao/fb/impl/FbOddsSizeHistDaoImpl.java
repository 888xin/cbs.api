package com.lifeix.cbs.contest.dao.fb.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.fb.FbOddsSizeHistDao;
import com.lifeix.cbs.contest.dto.odds.OddsSizeHist;

@Repository("fbOddsSizeHistDao")
public class FbOddsSizeHistDaoImpl extends ContestDaoSupport implements FbOddsSizeHistDao {

    @Override
    public List<OddsSizeHist> selectListByContestId(Long contestId, Long startId, Long endId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("startId", startId);
	params.put("endId", endId);
	params.put("num", limit);
	return sqlSession.selectList("FbOddsSizeHistMapper.selectListByContestId", params);
    }

    @Override
    public boolean saveOddsHist(List<OddsSizeHist> list) {
	if (list == null || list.size() == 0) {
	    return false;
	}
	int res = sqlSession.insert("FbOddsSizeHistMapper.saveOddsHist", list);
	return res > 0;
    }
}
