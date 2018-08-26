package com.lifeix.cbs.contest.dao.fb.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.fb.FbCupDao;
import com.lifeix.cbs.contest.dto.fb.FbCup;

@Service("fbCupDao")
public class FbCupDaoImpl extends ContestDaoSupport implements FbCupDao {

    @Override
    public boolean saveCup(List<FbCup> list) {
	if (list == null | list.size() == 0) {
	    return false;
	}
	int res = sqlSession.insert("FbCupMapper.saveCup", list);
	if (res > 0) {
	    String cacheKey = getCustomCache("getAllCup");
	    memcacheService.delete(cacheKey);
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public Boolean update(FbCup cup) {
	int res = sqlSession.update("FbCupMapper.updateName", cup);
	if (res > 0) {
	    String cacheKey = getCustomCache("getAllCup");
	    memcacheService.delete(cacheKey);
	    return true;
	}
	return false;
    }

    @Override
    public Map<Long, FbCup> getAllCup() {
	String cacheKey = getCustomCache("getAllCup");
	Map<Long, FbCup> cupMap = memcacheService.get(cacheKey);
	if (cupMap != null) {
	    return cupMap;
	}
	List<FbCup> cupList = sqlSession.selectList("FbCupMapper.getAllCup");
	cupMap = new HashMap<Long, FbCup>(cupList.size());
	for (FbCup cup : cupList) {
	    cupMap.put(cup.getTargetId(), cup);
	}
	memcacheService.set(cacheKey, cupMap);
	return cupMap;
    }

    @Override
    public Map<Long, FbCup> getCupByTargetIds(List<Long> targetIds) {
	List<FbCup> cupList = sqlSession.selectList("FbCupMapper.getCupByTargetIds", targetIds);
	Map<Long, FbCup> cupMap = new HashMap<Long, FbCup>(cupList.size());
	for (FbCup cup : cupList) {
	    cupMap.put(cup.getTargetId(), cup);
	}
	return cupMap;
    }

    @Override
    public List<FbCup> getLevelCup(int level) {
	return sqlSession.selectList("FbCupMapper.getLevelCup", level);
    }

    @Override
    public FbCup selectById(long id) {
	FbCup cup = sqlSession.selectOne("FbCupMapper.selectById", id);
	return cup;
    }
}
