package com.lifeix.cbs.contest.dao.contest.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.contest.ContestAdDao;
import com.lifeix.cbs.contest.dto.contest.ContestAd;

@Service("contestAdDao")
public class ContestAdDaoImpl extends ContestDaoSupport implements ContestAdDao {

    @Override
    public ContestAd selectById(long id) {
	String cacheKey = getCacheId(id);

	ContestAd bean = memcacheService.get(cacheKey);
	if (bean == null) {
	    bean = sqlSession.selectOne("ContestAdMapper.selectById", id);
	    if (bean != null) {
		memcacheService.set(cacheKey, bean);
	    }
	}
	return bean;
    }

    @Override
    public boolean insert(ContestAd bean) {
	int res = sqlSession.insert("ContestAdMapper.insert", bean);
	return res > 0;
    }

    @Override
    public boolean update(ContestAd bean) {
	int res = sqlSession.update("ContestAdMapper.update", bean);
	if (res > 0) {
	    deleteObjCache(bean.getId());
	    return true;
	}
	return false;
    }

    @Override
    public boolean updateHide(Long id, boolean hideFlag) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("id", id);
	params.put("hideFlag", hideFlag);

	int delete = sqlSession.delete("ContestAdMapper.updateHide", params);
	if (delete == 1) {
	    deleteObjCache(id);
	    return true;
	}
	return false;
    }

    @Override
    public List<ContestAd> findContestAds(Integer contestType, Boolean hideFlag, Long startId, int limit) {
	List<ContestAd> contentMains = null;
	Map<String, Object> params = new HashMap<String, Object>();
	if (contestType != null) {
	    params.put("contestType", contestType);
	}

	if (hideFlag != null) {
	    params.put("hideFlag", hideFlag);
	}
	if (startId != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	contentMains = sqlSession.selectList("ContestAdMapper.findContestAds", params);
	return contentMains;
    }

    /**
     * 对象缓存
     */
    public void deleteObjCache(Long id) {

	// 单个对象缓存
	String cacheKey = getCacheId(id);
	memcacheService.delete(cacheKey);

    }
}
