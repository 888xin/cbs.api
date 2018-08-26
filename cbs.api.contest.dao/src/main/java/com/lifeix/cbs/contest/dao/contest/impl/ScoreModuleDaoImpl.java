package com.lifeix.cbs.contest.dao.contest.impl;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.contest.ScoreModuleDao;
import com.lifeix.cbs.contest.dto.contest.ScoreModule;

@Repository("scoreModuleDao")
public class ScoreModuleDaoImpl extends ContestDaoSupport implements ScoreModuleDao {

    @Override
    public ScoreModule selectByContestType(Integer contestType) {
	String cacheKey = getCustomCache("selectByContestType", contestType);
	ScoreModule sm = memcacheService.get(cacheKey);
	if (sm == null) {
	    sm = sqlSession.selectOne("ScoreModuleMapper.selectByContestType", contestType);
	}
	return sm;
    }

    @Override
    public boolean updateModuleValue(ScoreModule sm) {
	boolean flag = sqlSession.update("ScoreModuleMapper.updateModuleValue", sm) > 0;
	if (flag) {
	    deleteCustomCache(sm);
	}
	return flag;
    }

    private void deleteCustomCache(ScoreModule sm) {
	String cacheKey = getCustomCache("selectByContestType", sm.getContestType());
	memcacheService.delete(cacheKey);
    }

}
