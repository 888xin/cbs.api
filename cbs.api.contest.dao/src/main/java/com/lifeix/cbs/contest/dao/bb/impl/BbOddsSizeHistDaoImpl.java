package com.lifeix.cbs.contest.dao.bb.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbOddsSizeHistDao;
import com.lifeix.cbs.contest.dto.odds.OddsSizeHist;

@Repository("bbOddsSizeHistDao")
public class BbOddsSizeHistDaoImpl extends ContestDaoSupport implements BbOddsSizeHistDao {

    public List<OddsSizeHist> selectListByContestId(Long contestId, Long startId, Long endId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("startId", startId);
	params.put("endId", endId);
	params.put("num", limit);
	return sqlSession.selectList("BbOddsSizeHistMapper.selectListByContestId", params);
    }

    public boolean saveOddsHist(List<OddsSizeHist> list) {
	if (list == null || list.size() == 0) {
	    return false;
	}
	int res = sqlSession.insert("BbOddsSizeHistMapper.saveOddsHist", list);
	return res > 0;
    }
}
