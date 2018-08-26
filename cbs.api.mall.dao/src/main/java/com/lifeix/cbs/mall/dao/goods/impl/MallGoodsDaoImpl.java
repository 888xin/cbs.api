package com.lifeix.cbs.mall.dao.goods.impl;

import com.lifeix.cbs.mall.dao.MallDaoSupport;
import com.lifeix.cbs.mall.dao.goods.MallGoodsDao;
import com.lifeix.cbs.mall.dto.goods.MallGoods;
import com.lifeix.framework.memcache.MultiCacheResult;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lhx on 16-03-21 上午10:49
 *
 * @Description
 */
@Repository("mallGoodsDao")
public class MallGoodsDaoImpl extends MallDaoSupport implements MallGoodsDao {

    private final String CATEGORY = this.getClass().getName() + ":category";

    @Override
    public MallGoods insert(MallGoods mallGoods) {
        boolean flag = sqlSession.insert("MallGoodsMapper.insert", mallGoods) > 0;
        if (flag) {
            // 删除可能存在的缓存数据
            deleteMemcachedData(null, mallGoods.getCategoryId());
        }
        return mallGoods;
    }

    @Override
    public MallGoods findById(long id) {
        String cacheKey = getCacheId(id);
        MallGoods mallGoods = memcacheService.get(cacheKey);
        if (mallGoods == null) {
            mallGoods = sqlSession.selectOne("MallGoodsMapper.findById", id);
            if (mallGoods != null) {
                memcacheService.set(cacheKey, mallGoods);
            }
        }
        return mallGoods;
    }

//    @Override
//    public List<MallGoods> findGoods(Integer status, Long startId, Integer limit) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("status", status);
//        params.put("startId", startId);
//        params.put("limit", limit);
//        return sqlSession.selectList("MallGoodsMapper.findGoods", params);
//    }

    @Override
    public List<MallGoods> findGoodsInner(Long categoryId, Integer status, Long startId, Integer limit) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoryId", categoryId);
        params.put("status", status);
        params.put("startId", startId);
        params.put("limit", limit);
        return sqlSession.selectList("MallGoodsMapper.findGoodsInner", params);
    }

    @Override
    public List<MallGoods> findGoodsByCategory(Long category, Long startId, Integer limit) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoryId", category);
        params.put("startId", startId);
        params.put("limit", limit);
        /**
         * 首页进行缓存，如果传入的limit和上次不同，第一页数据还是以前的limit条，从第二页开始才会生效。
         * 如果对limit进行判断，数据不满第一页，永远也获得不了缓存，因此不对limit进行判断
         * 这是个折中的取舍方案。
         */
        if (startId == null) {
            String categoryStr = CATEGORY + category;
            List<MallGoods> mallGoodsList = memcacheService.get(categoryStr);
            if (mallGoodsList == null) {
                mallGoodsList = sqlSession.selectList("MallGoodsMapper.findGoodsByCategory", params);
                if (mallGoodsList.size() > 0) {
                    memcacheService.set(categoryStr, mallGoodsList);
                }
            }
            return mallGoodsList;
        } else {
            return sqlSession.selectList("MallGoodsMapper.findGoodsByCategory", params);

        }
    }

    @Override
    public boolean update(MallGoods goods) {
        //如果修改的是分类，还需要删除旧的分类
//        if (goods.getCategoryId() != null || goods.getSortIndex() != null || goods.getStatus() != null) {
//            MallGoods goods1 = findById(goods.getId());
//            deleteMemcachedData(null, goods1.getCategoryId());
//        }
        MallGoods goods1 = findById(goods.getId());
        deleteMemcachedData(goods.getId(), goods1.getCategoryId());
        boolean flag = sqlSession.update("MallGoodsMapper.update", goods) > 0;
        if (flag) {
            // 如果修改的是分类，还需要删除新的分类
            if (goods.getCategoryId() != null){
                deleteMemcachedData(null, goods.getCategoryId());
            }
        }
        return flag;
    }

    @Override
    public boolean delete(long id) {
        MallGoods goods = new MallGoods();
        goods.setId(id);
        goods.setStatus(-1);
        return update(goods);
    }

    @Override
    public Map<Long, MallGoods> findByIds(List<Long> ids) {
        Map<Long, MallGoods> ret = new HashMap<Long, MallGoods>();
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
                    ret.put(Long.valueOf(revertCacheId(key)), (MallGoods) obj);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        if (noCached.size() > 0) {
            Map<Long, MallGoods> mallGoodsMap = sqlSession.selectMap("MallGoodsMapper.findMapByIds", revertMultiCacheId(noCached), "id");
            Collection<Long> keys = mallGoodsMap.keySet();
            for (Long id : keys) {
                MallGoods mallGoods = mallGoodsMap.get(id);
                if (mallGoods != null) {
                    ret.put(id, mallGoods);
                    memcacheService.set(getCacheId(id), mallGoods);
                }
            }
        }
        return ret;
    }

    private void deleteMemcachedData(Long id, Long categoryId) {
        if (id != null) {
            memcacheService.delete(getCacheId(id));
        }
        if (categoryId != null) {
            String categoryStr = CATEGORY + categoryId;
            memcacheService.delete(categoryStr);
        }
    }


    @Override
    public boolean isHasByCategory(long categoryId) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        return sqlSession.selectOne("MallGoodsMapper.isHasByCategory", map) != null ;
    }
}
