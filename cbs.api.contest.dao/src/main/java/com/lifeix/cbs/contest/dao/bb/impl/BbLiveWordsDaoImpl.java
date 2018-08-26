package com.lifeix.cbs.contest.dao.bb.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbLiveWordsDao;
import com.lifeix.cbs.contest.dto.bb.BbLiveWords;

@Repository("bbLiveWordsDao")
public class BbLiveWordsDaoImpl extends ContestDaoSupport implements BbLiveWordsDao {

    @Override
    public List<BbLiveWords> selectByContestId(Long contestId, Long endId) {
	List<BbLiveWords> contentMains = null;
	if (endId == null) {
	    String cacheKey = getCustomCache("selectByContestId", contestId);
	    contentMains = memcacheService.get(cacheKey);
	    if (contentMains == null) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contestId", contestId);
		contentMains = sqlSession.selectList("BbLiveWordsMapper.selectByContestId", params);
		if (contentMains != null) {
		    memcacheService.set(cacheKey, contentMains);
		}
	    }
	} else {
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("contestId", contestId);
	    params.put("endId", endId);
	    contentMains = sqlSession.selectList("BbLiveWordsMapper.selectByContestId", params);
	}
	return contentMains;
    }

    @Override
    public List<BbLiveWords> selectByContestIdNew(Long contestId, Long endId) {
	List<BbLiveWords> contentMains = null;
	if (endId == null) {
	    String cacheKey = getCustomCache("selectByContestIdNew", contestId);
	    contentMains = memcacheService.get(cacheKey);
	    if (contentMains == null) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contestId", contestId);
		contentMains = sqlSession.selectList("BbLiveWordsMapper.selectByContestIdNew", params);
		if (contentMains != null) {
		    memcacheService.set(cacheKey, contentMains);
		}
	    }
	} else {
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("contestId", contestId);
	    params.put("endId", endId);
	    contentMains = sqlSession.selectList("BbLiveWordsMapper.selectByContestIdNew", params);
	}
	return contentMains;
    }

    @Override
    public boolean saveLiveWords(List<BbLiveWords> list) {
	if (list == null || list.size() == 0)
	    return false;
	int res = sqlSession.insert("BbLiveWordsMapper.saveLiveWords", list);
	if (res > 0) {
	    Set<Long> contestIdSet = new HashSet<Long>(list.size());
	    for (BbLiveWords liveWords : list) {
		contestIdSet.add(liveWords.getContestId());
	    }
	    for (Long contestId : contestIdSet) {
		deleteListCache(contestId);
	    }
	}
	return res > 0;
    }

    /**
     * 列表缓存
     */
    public void deleteListCache(Long contestId) {
	// 清除赛事文字直播列表缓存
	String cacheKey1 = getCustomCache("selectByContestId", contestId);
	memcacheService.delete(cacheKey1);
	String cacheKey2 = getCustomCache("selectByContestIdNew", contestId);
	memcacheService.delete(cacheKey2);
    }
}
