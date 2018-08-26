package com.lifeix.cbs.contest.dao.bb.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbOddsJcHistDao;
import com.lifeix.cbs.contest.dto.odds.OddsJcHist;

@Repository("bbOddsJcHistDao")
public class BbOddsJcHistDaoImpl extends ContestDaoSupport implements BbOddsJcHistDao {

    public List<OddsJcHist> selectListByContestId(Long contestId, Long startId, Long endId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("startId", startId);
	params.put("endId", endId);
	params.put("num", limit);
	return sqlSession.selectList("BbOddsJcHistMapper.selectListByContestId", params);
    }

    public boolean saveOddsHist(List<OddsJcHist> list) {
	if (list == null || list.size() == 0) {
	    return false;
	}
	int res = sqlSession.insert("BbOddsJcHistMapper.saveOddsHist", list);
	return res > 0;
    }
}
