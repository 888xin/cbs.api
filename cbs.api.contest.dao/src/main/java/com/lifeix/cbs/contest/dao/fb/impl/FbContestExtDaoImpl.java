package com.lifeix.cbs.contest.dao.fb.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.fb.FbContestExtDao;
import com.lifeix.cbs.contest.dto.fb.FbContestExt;

@Service("fbContestExtDao")
public class FbContestExtDaoImpl extends ContestDaoSupport implements FbContestExtDao {

    @Override
    public FbContestExt selectById(Long contestId) {
	String cacheKey = getCacheId(contestId);

	FbContestExt contestExt = memcacheService.get(cacheKey);

	if (contestExt == null) {
	    contestExt = sqlSession.selectOne("FbContestExtMapper.selectById", contestId);
	    if (contestExt != null) {
		memcacheService.set(cacheKey, contestExt);
	    }
	}

	return contestExt;
    }

    @Override
    public boolean saveContestExt(List<FbContestExt> list) {
	if (list == null || list.size() == 0)
	    return false;
	int res = sqlSession.insert("FbContestExtMapper.saveContestExt", list);
	if (res > 0) {
	    for (FbContestExt contestExt : list) {
		deleteObjCache(contestExt);
	    }
	}
	return res > 0;
    }

    /**
     * 对象缓存
     * 
     * @param insertFlag
     */
    public void deleteObjCache(FbContestExt contestExt) {

	// 单个对象缓存
	String cacheKey = getCacheId(contestExt.getContestId());
	memcacheService.delete(cacheKey);

    }
}
