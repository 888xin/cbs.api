package com.lifeix.cbs.api.dao.money.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.api.dao.ContentGoldDaoSupport;
import com.lifeix.cbs.api.dao.money.MoneyCardDao;
import com.lifeix.cbs.api.dto.money.MoneyCard;

@Repository("moneyCardDao")
public class MoneyCardDaoImpl extends ContentGoldDaoSupport implements MoneyCardDao {

    public final String cacheKey = getCustomCache("findMoneyCards");

    @Override
    public MoneyCard findById(Long id) {
	String cacheKey = getCacheId(id);
	MoneyCard moneyCard = memcacheService.get(cacheKey);
	if (moneyCard == null) {
	    moneyCard = sqlSession.selectOne("MoneyCardMapper.findById", id);
	    if (moneyCard != null) {
		memcacheService.set(cacheKey, moneyCard);
	    }
	}
	return moneyCard;
    }

    @Override
    public boolean insert(MoneyCard entity) {
	int num = sqlSession.insert("MoneyCardMapper.insert", entity);
	if (num > 0) {
	    memcacheService.delete(cacheKey);
	    return true;
	}
	return false;
    }

    @Override
    public boolean update(MoneyCard entity) {
	int num = sqlSession.update("MoneyCardMapper.update", entity);
	if (num > 0) {
	    String cacheSingleKey = getCacheId(entity.getId());
	    memcacheService.delete(cacheSingleKey);
	    memcacheService.delete(cacheKey);
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean delete(MoneyCard entity) {
	int num = sqlSession.delete("MoneyCardMapper.delete", entity);
	if (num > 0) {
	    String cacheSingleKey = getCacheId(entity.getId());
	    memcacheService.delete(cacheSingleKey);
	    memcacheService.delete(cacheKey);
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public List<MoneyCard> findMoneyCards(Integer deleteFlag) {
	if (deleteFlag == 0) {
	    return getValidMoneyCards(deleteFlag);
	} else {
	    return getdeleteMoneyCards(deleteFlag);
	}

    }

    private List<MoneyCard> getValidMoneyCards(Integer deleteFlag) {
	List<MoneyCard> moneyCards = memcacheService.get(cacheKey);

	if (moneyCards == null) {
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("deleteFlag", deleteFlag);

	    moneyCards = sqlSession.selectList("MoneyCardMapper.findMoneyCards", params);
	    memcacheService.set(cacheKey, moneyCards);
	}
	return moneyCards;
    }

    private List<MoneyCard> getdeleteMoneyCards(Integer deleteFlag) {

	Map<String, Object> params = new HashMap<String, Object>();
	params.put("deleteFlag", deleteFlag);

	List<MoneyCard> moneyCards = sqlSession.selectList("MoneyCardMapper.findMoneyCards", params);
	return moneyCards;
    }

}
