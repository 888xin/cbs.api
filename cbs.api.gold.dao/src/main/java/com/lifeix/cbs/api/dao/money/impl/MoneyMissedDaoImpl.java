package com.lifeix.cbs.api.dao.money.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.api.dao.ContentGoldDaoSupport;
import com.lifeix.cbs.api.dao.money.MoneyMissedDao;
import com.lifeix.cbs.api.dto.money.MoneyMissed;

@Repository("moneyMissedDao")
public class MoneyMissedDaoImpl extends ContentGoldDaoSupport implements MoneyMissedDao {

    @Override
    public MoneyMissed findById(Long id) {
	return sqlSession.selectOne("MoneyMissedMapper.findById", id);
    }

    @Override
    public boolean insert(MoneyMissed entity) {
	int num = sqlSession.insert("MoneyMissedMapper.insert", entity);
	if (num > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean update(MoneyMissed entity) {
	int num = sqlSession.update("MoneyMissedMapper.update", entity);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean delete(MoneyMissed entity) {
	return false;
    }

    @Override
    public List<MoneyMissed> findMoneyMisseds(Integer status, Long startId, int limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("status", status);
	params.put("startId", startId);
	params.put("limit", limit);
	return sqlSession.selectList("MoneyMissedMapper.findMoneyMisseds", params);
    }

    @Override
    public Integer countMoneyMisseds() {
	return sqlSession.selectOne("MoneyMissedMapper.countMoneyMisseds");
    }
}
