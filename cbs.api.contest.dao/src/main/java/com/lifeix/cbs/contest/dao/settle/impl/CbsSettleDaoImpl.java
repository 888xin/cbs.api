package com.lifeix.cbs.contest.dao.settle.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.settle.CbsSettleDao;
import com.lifeix.cbs.contest.dto.settle.CbsSettle;

@Service("cbsSettleDao")
public class CbsSettleDaoImpl extends ContestDaoSupport implements CbsSettleDao {

    @Override
    public Boolean insert(CbsSettle entity) {
	int res = sqlSession.insert("CbsSettleMapper.insert", entity);
	return res > 0;
    }

    @Override
    public Boolean update(CbsSettle entity) {
	int res = sqlSession.update("CbsSettleMapper.update", entity);
	return res > 0;
    }

    @Override
    public boolean closeContest(Long settleId, int status) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("settleId", settleId);
	params.put("closeFlag", status);
	int res = sqlSession.update("CbsSettleMapper.closeContest", params);
	return res > 0;
    }

    @Override
    public List<CbsSettle> findSettleTask(Long settleId, int limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("settleId", settleId);
	params.put("limit", limit);
	return sqlSession.selectList("CbsSettleMapper.findSettleTask", params);
    }

    @Override
    public List<CbsSettle> findCbsSettles(Long settleId, Integer type, int limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	if (settleId != null) {
	    params.put("settleId", settleId);
	}
	if (type != null) {
	    params.put("type", type);
	}
	params.put("limit", limit);
	return sqlSession.selectList("CbsSettleMapper.findCbsSettles", params);
    }

    @Override
    public CbsSettle findByContest(Integer type, Long contestId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("type", type);
	params.put("contestId", contestId);
	return sqlSession.selectOne("CbsSettleMapper.findByContest", params);
    }

}
