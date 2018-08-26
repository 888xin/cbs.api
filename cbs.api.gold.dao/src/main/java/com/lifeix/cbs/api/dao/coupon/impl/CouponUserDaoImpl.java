package com.lifeix.cbs.api.dao.coupon.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.api.dao.ContentGoldDaoSupport;
import com.lifeix.cbs.api.dao.coupon.CouponUserDao;
import com.lifeix.cbs.api.dto.coupon.CouponUser;

@Repository("couponUserDao")
public class CouponUserDaoImpl extends ContentGoldDaoSupport implements CouponUserDao {

    @Override
    public CouponUser findById(Long id) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("id", id);
	return sqlSession.selectOne("CouponUserMapper.selectById", params);
    }

    @Override
    public boolean insert(CouponUser entity) {
	int num = sqlSession.insert("CouponUserMapper.insertAndGetPrimaryKey", entity);
	if (num > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean update(CouponUser entity) {
	if (sqlSession.update("CouponUserMapper.update", entity) > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean delete(CouponUser entity) {
	return false;
    }

    @Override
    public List<CouponUser> findCouponUsersByIds(List<Long> ids) {
	return sqlSession.selectList("CouponUserMapper.findCouponUsersByIds", ids);
    }

    /**
     * 查找用户可用的龙筹券(未使用并未过期)
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     */
    @Override
    public List<CouponUser> findUserActiveCoupon(Long userId, List<Integer> filterPrice, Long startId, int limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("userId", userId);
	params.put("endTime", new Date());
	if (startId != null) {
	    params.put("startId", startId);
	}
	if (filterPrice != null && filterPrice.size() > 0) {
	    params.put("filterPrice", filterPrice);
	}
	params.put("limit", limit);
	return sqlSession.selectList("CouponUserMapper.findUserActiveCoupon", params);
    }

    /**
     * 查找用户已过期的龙筹券
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     */
    @Override
    public List<CouponUser> findUserExpiredCoupons(Long userId, Long startId, int limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("userId", userId);
	params.put("endTime", new Date());
	if (startId != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	return sqlSession.selectList("CouponUserMapper.findUserExpiredCoupons", params);
    }

    @Override
    public boolean insertByBatch(List<CouponUser> list) {
	return sqlSession.insert("CouponUserMapper.insertByBatch", list) > 0;
    }

    /**
     * 批量更新用户发送消息标志位
     * 
     * @param ids
     * @param notifyFlag
     * @return
     */
    @Override
    public boolean updateNotifyFlagByIds(List<Long> ids, Boolean notifyFlag) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("ids", ids);
	params.put("notifyFlag", notifyFlag);
	int num = sqlSession.update("CouponUserMapper.updateNotifyFlagByIds", params);
	return num > 0 ? true : false;
    }

    /**
     * 分组查询用户后一天过期龙筹券
     * 
     * @param used
     * @param endTime
     * @return
     */
    @Override
    public List<CouponUser> findPigeonMess(Boolean used, Date endTime, Long startId, int limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("used", used);
	params.put("endTime", endTime);
	if (startId != null && startId != 0) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	return sqlSession.selectList("CouponUserMapper.findPigeonMess", params);
    }

    /**
     * 查找即将到期的龙筹券
     * 
     * @param notifyFlag
     * @param endTime
     * @param startId
     * @param limit
     * @return
     */
    @Override
    public List<CouponUser> findMessage(Boolean notifyFlag, Date endTime, Long startId, int limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("notifyFlag", notifyFlag);
	params.put("endTime", endTime);
	if (startId != null && startId != 0) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	return sqlSession.selectList("CouponUserMapper.findMessage", params);
    }

}
