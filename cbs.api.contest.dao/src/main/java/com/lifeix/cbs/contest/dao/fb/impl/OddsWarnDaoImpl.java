package com.lifeix.cbs.contest.dao.fb.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.fb.OddsWarnDao;
import com.lifeix.cbs.contest.dto.odds.OddsWarn;

/**
 * 异常赔率预警记录
 * 
 * @author lifeix
 * 
 */
@Repository("oddsWarnDao")
public class OddsWarnDaoImpl extends ContestDaoSupport implements OddsWarnDao {

    @Override
    public OddsWarn selectById(long id) {
	return sqlSession.selectOne("OddsWarnMapper.selectById", id);
    }

    @Override
    public OddsWarn selectByOddsId(Integer playType, Long oddsId) {
	Map<String, Object> params = new HashMap<>();
	params.put("playType", playType);
	params.put("oddsId", oddsId);
	return sqlSession.selectOne("OddsWarnMapper.selectByOddsId", params);
    }

    @Override
    public boolean insert(OddsWarn entity) {
	int ret = sqlSession.insert("OddsWarnMapper.insert", entity);
	return ret > 0;
    }

    @Override
    public boolean update(OddsWarn entity) {
	return sqlSession.update("OddsWarnMapper.update", entity) > 0;
    }

    @Override
    public List<OddsWarn> selectByStatus(Integer status, Long startId, int limit) {
	Map<String, Object> params = new HashMap<>();
	if (status != null) {
	    params.put("status", status);
	}
	if (status != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	return sqlSession.selectList("OddsWarnMapper.selectByStatus", params);
    }
}
