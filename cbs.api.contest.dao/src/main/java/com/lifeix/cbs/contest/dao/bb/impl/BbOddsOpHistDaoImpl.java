package com.lifeix.cbs.contest.dao.bb.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbOddsOpHistDao;
import com.lifeix.cbs.contest.dto.odds.OddsOpHist;

@Repository("bbOddsOpHistDao")
public class BbOddsOpHistDaoImpl extends ContestDaoSupport implements BbOddsOpHistDao {

    public List<OddsOpHist> selectListByContestId(Long contestId, Long startId, Long endId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("startId", startId);
	params.put("endId", endId);
	params.put("num", limit);
	return sqlSession.selectList("BbOddsOpHistMapper.selectListByContestId", params);
    }

    public boolean saveOddsHist(List<OddsOpHist> list) {
	if (list == null || list.size() == 0) {
	    return false;
	}
	int res = sqlSession.insert("BbOddsOpHistMapper.saveOddsHist", list);
	return res > 0;
    }
}
