package com.lifeix.cbs.content.dao.frontpage.impl;

import com.lifeix.cbs.content.dao.ContentDaoSupport;
import com.lifeix.cbs.content.dao.frontpage.FrontPageAdDao;
import com.lifeix.cbs.content.dto.frontpage.FrontPage;
import com.lifeix.framework.memcache.MultiCacheResult;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lhx on 15-11-27 下午3:05
 *
 * @Description
 */
@Repository("frontPageAdDao")
public class FrontPageAdDaoImpl extends ContentDaoSupport implements FrontPageAdDao {

    @Override
    public Long insert(FrontPage frontPage) {
        if (sqlSession.insert("FrontPageAdMapper.insert", frontPage) > 0) {
            memcacheService.set(getCacheId(frontPage.getId()), frontPage);
            return frontPage.getId();
        }
        return -1L;
    }

    @Override
    public Map<Long, FrontPage> findByIds(List<Long> ids) {
        Map<Long, FrontPage> ret = new HashMap<Long, FrontPage>();
        if (ids == null || ids.size() == 0) {
            return ret;
        }
        MultiCacheResult cacheResult = memcacheService.getMulti(getMultiCacheId(ids));

        Collection<String> noCached = cacheResult.getMissIds();
        Map<String, Object> cacheDatas = cacheResult.getCacheData();
        for (String key : cacheDatas.keySet()) {
            Object obj = cacheDatas.get(key);
            if (obj != null) {
                try {
                    ret.put(Long.valueOf(revertCacheId(key)), (FrontPage) obj);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        if (noCached.size() > 0) {
            Map<Long, FrontPage> frontPageMap = sqlSession.selectMap("FrontPageAdMapper.findMapByIds",
                    revertMultiCacheId(noCached), "id");

            Collection<Long> keys = frontPageMap.keySet();
            for (Long fId : keys) {
                FrontPage frontPage = frontPageMap.get(fId);
                if (frontPage != null) {
                    ret.put(fId, frontPage);
                    memcacheService.set(getCacheId(fId), frontPage);
                }
            }
        }
        return ret;
    }

    @Override
    public FrontPage findById(Long fId) {

        String cacheKey = getCacheId(fId);
        FrontPage frontPage = memcacheService.get(cacheKey);
        if (frontPage == null) {
            frontPage = sqlSession.selectOne("FrontPageAdMapper.findById", fId);
            if (frontPage != null) {
                memcacheService.set(cacheKey, frontPage);
            }
        }
        return frontPage;
    }

    @Override
    public List<FrontPage> findFrontPages(Long startId, Long endId, Integer limit,Integer type) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("startId", startId);
        params.put("endId", endId);
        params.put("limit", limit);
        return sqlSession.selectList("FrontPageAdMapper.findFrontPages", params);
    }

    @Override
    public List<FrontPage> findFrontPagesInner(Long startId, Long endId, Integer limit, Integer type, Integer status, Integer skip) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("startId", startId);
        params.put("endId", endId);
        params.put("limit", limit);
        params.put("status", status);
        params.put("skip", skip);
        return sqlSession.selectList("FrontPageAdMapper.findFrontPagesInner", params);
    }

    @Override
    public boolean editFrontPagesInner(Long id, Integer status) {
        memcacheService.delete(getCacheId(id));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("status", status);
        return sqlSession.update("FrontPageAdMapper.editFrontPagesInner", params) > 0;
    }

    @Override
    public Integer findFrontPagesCount(Integer type, Integer status) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("status", status);
        return sqlSession.selectOne("FrontPageAdMapper.findFrontPagesCount", params);
    }

    @Override
    public FrontPage findByContentId(Long contentId) {
        return sqlSession.selectOne("FrontPageAdMapper.findByContentId", contentId);
    }

}
