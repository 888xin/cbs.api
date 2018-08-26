package com.lifeix.cbs.api.dao.user.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lifeix.framework.memcache.MultiCacheResult;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.dao.ContentDaoSupport;
import com.lifeix.cbs.api.dao.user.CbsUserStarDao;
import com.lifeix.cbs.api.dto.user.CbsUserStar;

@Service("cbsUserStarDao")
public class CbsUserStarDaoImpl extends ContentDaoSupport implements CbsUserStarDao {

    private final String cacheIdsKey = getCacheId(":ids");

    @Override
    public CbsUserStar selectByUser(long userId) {
        return sqlSession.selectOne("CbsUserStarMapper.selectByUser", userId);
    }

    @Override
    public Boolean insert(CbsUserStar userStar) {
        int num = sqlSession.insert("CbsUserStarMapper.insert", userStar);
        if (num > 0) {
            deleteCache(userStar.getUserId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean update(CbsUserStar userStar) {
        int res = sqlSession.update("CbsUserStarMapper.update", userStar);
        if (res > 0) {
            deleteCache(userStar.getUserId());
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteByUser(Long userId) {
        int res = sqlSession.delete("CbsUserStarMapper.deleteByUser", userId);

        if (res > 0) {
            deleteCache(userId);
            return true;
        }
        return false;
    }

    @Override
    public List<CbsUserStar> findStarUsers(int limit) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("limit", limit);
        return sqlSession.selectList("CbsUserStarMapper.findStarUsers", params);
    }

    @Override
    public List<CbsUserStar> findAllStars(Boolean hideFlag, Long startId, int limit) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (hideFlag != null) {
            params.put("hideFlag", hideFlag);
        }
        if (startId != null) {
            params.put("startId", startId);
        }
        params.put("limit", limit);
        return sqlSession.selectList("CbsUserStarMapper.findAllStars", params);
    }

    @Override
    public List<Long> findAllStarsIds() {
        List<Long> list = memcacheService.get(cacheIdsKey);
        if (list == null) {
            list = sqlSession.selectList("CbsUserStarMapper.findAllStarsUserIds");
            memcacheService.set(cacheIdsKey, list);
        }
        return list;
    }

    @Override
    public Map<Long, CbsUserStar> findByIds(List<Long> userIds) {
        Map<Long, CbsUserStar> ret = new HashMap<Long, CbsUserStar>();
        if (userIds == null || userIds.size() == 0) {
            return ret;
        }
        MultiCacheResult cacheResult = memcacheService.getMulti(getMultiCacheId(userIds));

        Collection<String> noCached = cacheResult.getMissIds();
        Map<String, Object> cacheDatas = cacheResult.getCacheData();
        for (String key : cacheDatas.keySet()) {
            Object obj = cacheDatas.get(key);
            if (obj != null) {
                try {
                    ret.put(Long.valueOf(revertCacheId(key)), (CbsUserStar) obj);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        if (noCached.size() > 0) {
            Map<Long, CbsUserStar> userStarMap = sqlSession.selectMap("CbsUserStarMapper.findMapByUserIds",
                    revertMultiCacheId(noCached), "userId");
            Collection<Long> keys = userStarMap.keySet();
            for (Long id : keys) {
                CbsUserStar userStar = userStarMap.get(id);
                if (userStar != null) {
                    ret.put(id, userStar);
                    memcacheService.set(getCacheId(id), userStar);
                }
            }
        }
        return ret;
    }

    public void deleteCache(Long userId) {
        memcacheService.delete(getCacheId(userId));
        memcacheService.delete(cacheIdsKey);
    }

}
