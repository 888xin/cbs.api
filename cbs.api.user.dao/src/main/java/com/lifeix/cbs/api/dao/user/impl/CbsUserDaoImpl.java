package com.lifeix.cbs.api.dao.user.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.dao.ContentDaoSupport;
import com.lifeix.cbs.api.dao.user.CbsUserDao;
import com.lifeix.cbs.api.dto.user.CbsUser;
import com.lifeix.framework.memcache.MultiCacheResult;

@Service("cbsUserDao")
public class CbsUserDaoImpl extends ContentDaoSupport implements CbsUserDao {

    @Override
    public CbsUser selectById(long id) {
	String cacheKey = getCacheId(id);

	CbsUser user = memcacheService.get(cacheKey);

	if (user == null) {
	    user = sqlSession.selectOne("CbsUserMapper.selectById", id);
	    if (user != null) {
		memcacheService.set(cacheKey, user);
	    }

	}

	return user;
    }

    @Override
    public CbsUser getCbsUserByLongNo(Long longNO) {
	String cacheKey = getCacheId(longNO);

	CbsUser user = memcacheService.get(cacheKey);

	if (user == null) {
	    user = sqlSession.selectOne("CbsUserMapper.selectByLongNO", longNO);
	    if (user != null) {
		memcacheService.set(cacheKey, user);
	    }

	}

	return user;
    }

    @Override
    public Long insertAndGetPrimaryKey(CbsUser user) {
	sqlSession.insert("CbsUserMapper.insertAndGetPrimaryKey", user);
	if (user.getUserId() > 0) {
	    deleteCache(user.getUserId());
	}
	return user.getUserId();
    }

    @Override
    public Boolean update(CbsUser user) {
	int res = sqlSession.update("CbsUserMapper.update", user);

	if (res > 0) {
	    deleteCache(user.getUserId());
	    return true;
	}
	return false;
    }

    @Override
    public Boolean deleteById(long id) {
	int res = sqlSession.delete("CbsUserMapper.deleteById", id);
	if (res > 0) {
	    deleteCache(id);
	    return true;
	}
	return false;
    }

    public void deleteCache(Long id) {

	if (id == null) {
	    return;
	}

	// 单个对象缓存
	String cacheKey1 = getCacheId(id);
	CbsUser user = memcacheService.get(cacheKey1);
	if (user != null) {
	    String cacheKey2 = getCacheId(user.getUserNo());
	    memcacheService.delete(cacheKey2);
	}
	memcacheService.delete(cacheKey1);

    }

    public Map<Long, CbsUser> findCsAccountMapByIds(List<Long> ids) {
	Map<Long, CbsUser> ret = new HashMap<Long, CbsUser>();
	if (ids == null || ids.size() == 0) {
	    return ret;
	}
	MultiCacheResult cacheResult = memcacheService.getMulti(getMultiCacheId(ids));
	Collection<String> noCached = cacheResult.getMissIds();
	Map<String, Object> cacheDatas = cacheResult.getCacheData();
	Iterator<String> it = cacheDatas.keySet().iterator();
	while (it.hasNext()) {
	    String key = it.next();
	    Object obj = cacheDatas.get(key);
	    if (obj != null) {
		try {
		    ret.put(Long.valueOf(revertCacheId(key)), (CbsUser) obj);
		} catch (Exception e) {
		    LOG.warn(String.format("CsAccountDaoImpl get multi cache fail %s - %s", key, e.getMessage()));
		}
	    }
	}
	if (noCached.size() > 0) {
	    List<CbsUser> csAccounts = sqlSession.selectList("CbsUserMapper.selectByIds", revertMultiCacheId(noCached));
	    for (CbsUser acc : csAccounts) {
		ret.put(acc.getUserId(), acc);
		memcacheService.set(getCacheId(acc.getUserId()), acc);
	    }
	}
	return ret;
    }

    @Override
    public Map<Long, CbsUser> selectByMobile(List<String> mobiles) {
	Map<Long, CbsUser> ret = new HashMap<Long, CbsUser>();
	if (mobiles == null || mobiles.size() == 0) {
	    return ret;
	}
	MultiCacheResult cacheResult = memcacheService.getMulti(getMultiCacheId(mobiles));
	Collection<String> noCached = cacheResult.getMissIds();
	Map<String, Object> cacheDatas = cacheResult.getCacheData();
	Iterator<String> it = cacheDatas.keySet().iterator();
	while (it.hasNext()) {
	    String key = it.next();
	    Object obj = cacheDatas.get(key);
	    if (obj != null) {
		try {
		    ret.put(Long.valueOf(revertCacheId(key)), (CbsUser) obj);
		} catch (Exception e) {
		    LOG.warn(String.format("CsAccountDaoImpl get multi cache fail %s - %s", key, e.getMessage()));
		}
	    }
	}
	if (noCached.size() > 0) {
	    List<CbsUser> csAccounts = sqlSession.selectList("CbsUserMapper.selectByMobile", revertMultiCacheId(noCached));
	    for (CbsUser acc : csAccounts) {
		ret.put(acc.getUserId(), acc);
		memcacheService.set(getCacheId(acc.getMobilePhone()), acc);
	    }
	}

	return ret;

    }

}
