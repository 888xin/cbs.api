package com.lifeix.cbs.contest.dao.yy.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.yy.YyCupDao;
import com.lifeix.cbs.contest.dto.yy.YyCup;

@Service("yyCupDao")
public class YyCupDaoImpl extends ContestDaoSupport implements YyCupDao {

    @Override
    public YyCup selectById(long id) {
	String cacheKey = getCacheId(id);

	YyCup contest = memcacheService.get(cacheKey);
	if (contest == null) {
	    contest = sqlSession.selectOne("YyCupMapper.selectById", id);
	    if (contest != null) {
		memcacheService.set(cacheKey, contest);
	    }
	}
	return contest;
    }

    @Override
    public YyCup selectByName(String cupName) {
	return sqlSession.selectOne("YyCupMapper.selectByName", cupName);
    }

    @Override
    public boolean insert(YyCup contest) {
	int res = sqlSession.insert("YyCupMapper.insert", contest);
	if (res > 0) {
	    deleteObjCache();
	    return true;
	}
	return false;
    }

    @Override
    public boolean update(YyCup contest) {
	int res = sqlSession.update("YyCupMapper.update", contest);
	if (res > 0) {
	    deleteObjCache(contest.getCupId());
	    deleteObjCache();
	    return true;
	}
	return false;
    }

    @Override
    public boolean delete(YyCup contest) {
	int delete = sqlSession.delete("YyCupMapper.delete", contest.getCupId());
	if (delete == 1) {
	    deleteObjCache(contest.getCupId());
	    deleteObjCache();
	    return true;
	}
	return false;
    }

    @Override
    public List<YyCup> findYyCups() {
	String cacheKey = getCustomCache("findYyCups");
	List<YyCup> cups = memcacheService.get(cacheKey);
	if (cups != null) {
	    return cups;
	}
	cups = sqlSession.selectList("YyCupMapper.findYyCups");
	memcacheService.set(cacheKey, cups);
	return cups;
    }

    /**
     * 对象缓存
     */
    public void deleteObjCache(Long contestId) {
	// 单个对象缓存
	String cacheKey1 = getCacheId(contestId);
	memcacheService.delete(cacheKey1);

    }

    public void deleteObjCache() {
	// 列表对象缓存
	String cacheKey2 = getCustomCache("findYyCups");
	memcacheService.delete(cacheKey2);

    }

}
