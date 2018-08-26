package com.lifeix.cbs.contest.dao.bb.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbContestExtDao;
import com.lifeix.cbs.contest.dto.bb.BbContestExt;

@Service("bbContestExtDao")
public class BbContestExtDaoImpl extends ContestDaoSupport implements BbContestExtDao {

    @Override
    public BbContestExt selectById(Long contestId) {
	String cacheKey = getCacheId(contestId);

	BbContestExt contestExt = memcacheService.get(cacheKey);

	if (contestExt == null) {
	    contestExt = sqlSession.selectOne("BbContestExtMapper.selectById", contestId);
	    if (contestExt != null) {
		memcacheService.set(cacheKey, contestExt);
	    }
	}

	return contestExt;
    }

    @Override
    public boolean saveContestExt(List<BbContestExt> list) {
	if (list == null || list.size() == 0)
	    return false;
	int res = sqlSession.insert("BbContestExtMapper.saveContestExt", list);
	if (res > 0) {
	    for (BbContestExt contestExt : list) {
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
    public void deleteObjCache(BbContestExt contestExt) {

	// 单个对象缓存
	String cacheKey = getCacheId(contestExt.getContestId());
	memcacheService.delete(cacheKey);

    }
}
