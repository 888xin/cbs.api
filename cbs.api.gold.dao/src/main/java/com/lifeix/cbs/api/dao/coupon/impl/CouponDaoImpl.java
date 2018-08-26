package com.lifeix.cbs.api.dao.coupon.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.api.dao.ContentGoldDaoSupport;
import com.lifeix.cbs.api.dao.coupon.CouponDao;
import com.lifeix.cbs.api.dto.coupon.Coupon;
import com.lifeix.framework.memcache.MultiCacheResult;

/**
 * Created by lhx on 16-2-25 上午11:14
 * 
 * @Description
 */
@Repository("couponDao")
public class CouponDaoImpl extends ContentGoldDaoSupport implements CouponDao {

    @Override
    public Coupon findById(Long id) {
	String cacheKey = getCacheId(id);
	Coupon coupon = memcacheService.get(cacheKey);
	if (coupon == null) {
	    coupon = sqlSession.selectOne("CouponMapper.selectById", id);
	    if (coupon != null) {
		memcacheService.set(cacheKey, coupon);
	    }
	}
	return coupon;
    }

    @Override
    public boolean insert(Coupon entity) {
	if (sqlSession.insert("CouponMapper.insert", entity) > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean update(Coupon entity) {
	if (sqlSession.update("CouponMapper.update", entity) > 0) {
	    deleteObjCache(entity.getId());
	    return true;
	}
	return false;
    }

    @Override
    public boolean incrementCoupon(Long couponId) {
	if (sqlSession.update("CouponMapper.incrementCoupon", couponId) > 0) {
	    deleteObjCache(couponId);
	    return true;
	}
	return false;
    }

    @Override
    public boolean delete(Coupon entity) {
	if (sqlSession.delete("CouponMapper.delete", entity.getId()) > 0) {
	    deleteObjCache(entity.getId());
	    return true;
	}
	return false;
    }

    @Override
    public Map<Long, Coupon> findMapByIds(List<Long> ids) {
	Map<Long, Coupon> ret = new HashMap<Long, Coupon>();
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
		    ret.put(Long.valueOf(revertCacheId(key)), (Coupon) obj);
		} catch (Exception e) {
		    LOG.error(e.getMessage(), e);
		}
	    }
	}
	if (noCached.size() > 0) {
	    Map<Long, Coupon> map = sqlSession.selectMap("CouponMapper.findMapByIds", revertMultiCacheId(noCached), "id");
	    Collection<Long> keys = map.keySet();
	    for (Long id : keys) {
		Coupon coupon = map.get(id);
		if (coupon != null) {
		    ret.put(id, coupon);
		    memcacheService.set(getCacheId(id), coupon);
		}
	    }
	}
	return ret;
    }

    @Override
    public List<Coupon> selectCoupons(Integer type, Boolean valid, Long startId, int limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("limit", limit);
	if (type != null) {
	    params.put("type", type);
	}
	if (valid != null) {
	    params.put("valid", valid);
	}
	if (startId != null) {
	    params.put("startId", startId);
	}
	return sqlSession.selectList("CouponMapper.selectCoupons", params);
    }

    public void deleteObjCache(Long couponId) {

	// 单个对象缓存
	String cacheKey = getCacheId(couponId);
	memcacheService.delete(cacheKey);

    }

}
