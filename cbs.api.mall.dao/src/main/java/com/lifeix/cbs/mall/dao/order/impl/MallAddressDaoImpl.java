/**
 * 
 */
package com.lifeix.cbs.mall.dao.order.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.mall.dao.MallDaoSupport;
import com.lifeix.cbs.mall.dao.order.MallAddressDao;
import com.lifeix.cbs.mall.dto.order.MallAddress;

/**
 * 订单收货地址
 * 
 * @author lifeix
 * 
 */
@Repository("mallAddressDao")
public class MallAddressDaoImpl extends MallDaoSupport implements MallAddressDao {

    @Override
    public MallAddress selectById(Long id) {
	String cacheKey = getCacheId(id);
	MallAddress mallAddress = memcacheService.get(cacheKey);
	if (mallAddress == null) {
	    mallAddress = sqlSession.selectOne("MallAddressMapper.selectById", id);
	    if (mallAddress != null) {
		memcacheService.set(cacheKey, mallAddress);
	    }
	}
	return mallAddress;
    }

    @Override
    public boolean insert(MallAddress entity) {
	int res = sqlSession.insert("MallAddressMapper.insert", entity);
	if (res > 0) {
        cleanCache(entity);
	    return true;
	}
	return false;
    }

    @Override
    public boolean update(MallAddress entity) {
	int res = sqlSession.update("MallAddressMapper.update", entity);
	if (res > 0) {
	    cleanCache(entity);
	    return true;
	}
	return false;
    }

    @Override
    public boolean delete(MallAddress entity) {
	int res = sqlSession.delete("MallAddressMapper.delete", entity);
	if (res > 0) {
	    cleanCache(entity);
	    return true;
	}
	return false;
    }

    @Override
    public List<MallAddress> findMallAddress(Long userId) {
	String cacheKey = getCustomCache("findMallAddress", userId);
	List<MallAddress> ret = memcacheService.get(cacheKey);
	if (ret == null) {
	    ret = sqlSession.selectList("MallAddressMapper.findMallAddress", userId);
	    if (ret.size() > 0) {
		memcacheService.set(cacheKey, ret);
	    }
	}
	return ret;
    }

    /**
     * 清除缓存
     * 
     * @param id
     */
    private void cleanCache(MallAddress address) {

        Long id = address.getId();
        if (id != null) {
            memcacheService.delete(getCacheId(id));
        }


        memcacheService.delete(getCustomCache("findMallAddress", address.getUserId()));
    }

}
