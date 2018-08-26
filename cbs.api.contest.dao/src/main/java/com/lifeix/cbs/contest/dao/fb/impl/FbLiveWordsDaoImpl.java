package com.lifeix.cbs.contest.dao.fb.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.fb.FbLiveWordsDao;
import com.lifeix.cbs.contest.dto.fb.FbLiveWords;

@Service("fbLiveWordsDao")
public class FbLiveWordsDaoImpl extends ContestDaoSupport implements FbLiveWordsDao {

    @Override
    public List<FbLiveWords> selectByContestId(Long contestId, Long endId) {
	List<FbLiveWords> contentMains = null;
	if (endId == null) {
	    String cacheKey = getCustomCache("selectByContestId", contestId);
	    contentMains = memcacheService.get(cacheKey);
	    if (contentMains == null) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contestId", contestId);
		contentMains = sqlSession.selectList("FbLiveWordsMapper.selectByContestId", params);
		if (contentMains != null) {
		    memcacheService.set(cacheKey, contentMains);
		}
	    }
	} else {
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("contestId", contestId);
	    params.put("endId", endId);
	    contentMains = sqlSession.selectList("FbLiveWordsMapper.selectByContestId", params);
	}
	return contentMains;
    }

    @Override
    public boolean saveLiveWords(List<FbLiveWords> list) {
	if (list == null || list.size() == 0)
	    return false;
	int res = sqlSession.insert("FbLiveWordsMapper.saveLiveWords", list);
	if (res > 0) {
	    Set<Long> contestIdSet = new HashSet<Long>(list.size());
	    for (FbLiveWords liveWords : list) {
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
     * 
     * @param contest
     */
    public void deleteListCache(Long contestId) {
	// 清除赛事文字直播列表缓存
	String cacheKey2 = getCustomCache("selectByContestId", contestId);
	memcacheService.delete(cacheKey2);
    }
}
