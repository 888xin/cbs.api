package com.lifeix.cbs.contest.dao.bb.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbCupDao;
import com.lifeix.cbs.contest.dto.bb.BbCup;

@Service("bbCupDao")
public class BbCupDaoImpl extends ContestDaoSupport implements BbCupDao {

    @Override
    public boolean saveCup(List<BbCup> list) {
	if (list == null | list.size() == 0) {
	    return false;
	}
	int res = sqlSession.insert("BbCupMapper.saveCup", list);
	if (res > 0) {
	    String cacheKey = getCustomCache("getAllCup");
	    memcacheService.delete(cacheKey);
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public Boolean update(BbCup cup) {
	int res = sqlSession.update("BbCupMapper.updateName", cup);
	if (res > 0) {
	    String cacheKey = getCustomCache("getAllCup");
	    memcacheService.delete(cacheKey);
	    return true;
	}
	return false;
    }

    @Override
    public Map<Long, BbCup> getAllCup() {
	String cacheKey = getCustomCache("getAllCup");
	Map<Long, BbCup> cupMap = memcacheService.get(cacheKey);
	if (cupMap != null) {
	    return cupMap;
	}
	List<BbCup> cupList = sqlSession.selectList("BbCupMapper.getAllCup");
	cupMap = new HashMap<Long, BbCup>(cupList.size());
	for (BbCup cup : cupList) {
	    cupMap.put(cup.getTargetId(), cup);
	}
	memcacheService.set(cacheKey, cupMap);
	return cupMap;
    }

    @Override
    public Map<Long, BbCup> getCupByTargetIds(List<Long> targetIds) {
	List<BbCup> cupList = sqlSession.selectList("BbCupMapper.getCupByTargetIds", targetIds);
	Map<Long, BbCup> cupMap = new HashMap<Long, BbCup>(cupList.size());
	for (BbCup cup : cupList) {
	    cupMap.put(cup.getTargetId(), cup);
	}
	return cupMap;
    }

    @Override
    public List<BbCup> getLevelCup(int level) {
	return sqlSession.selectList("BbCupMapper.getLevelCup", level);
    }

    @Override
    public BbCup selectById(long id) {
	BbCup cup = sqlSession.selectOne("BbCupMapper.selectById", id);
	return cup;
    }
}
