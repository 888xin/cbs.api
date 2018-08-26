package com.lifeix.cbs.api.dao.money.impl;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.api.dao.ContentGoldDaoSupport;
import com.lifeix.cbs.api.dao.money.MoneyOrderDao;
import com.lifeix.cbs.api.dto.money.MoneyOrder;

@Repository("moneyOrderDao")
public class MoneyOrderDaoImpl extends ContentGoldDaoSupport implements MoneyOrderDao {

    @Override
    public MoneyOrder findById(Long id) {
	return sqlSession.selectOne("MoneyOrderMapper.findById", id);
    }

    @Override
    public boolean insert(MoneyOrder entity) {
	int num = sqlSession.insert("MoneyOrderMapper.insert", entity);
	if (num > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean update(MoneyOrder entity) {
	int num = sqlSession.update("MoneyOrderMapper.update", entity);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean delete(MoneyOrder entity) {
	return false;
    }

}
