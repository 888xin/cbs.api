/**
 * 
 */
package com.lifeix.cbs.mall.dao.order.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.mall.dao.MallDaoSupport;
import com.lifeix.cbs.mall.dao.order.MallExpressDao;
import com.lifeix.cbs.mall.dto.order.MallExpress;

/**
 * @author lifeix
 * 
 */
@Service("mallExpressDao")
public class MallExpressDaoImpl extends MallDaoSupport implements MallExpressDao {

    /**
     * 根据orderId查询
     */
    @Override
    public MallExpress findById(Long orderId) {
	String cacheKey = getCacheId(orderId);
	MallExpress mallExpress = memcacheService.get(cacheKey);
	if (mallExpress == null) {
	    mallExpress = sqlSession.selectOne("MallExpressMapper.findById", orderId);
	    if (mallExpress != null) {
		memcacheService.set(cacheKey, mallExpress);
	    }
	}
	return mallExpress;
    }

    /**
     * 添加物流信息
     */
    @Override
    public boolean insert(MallExpress mallExpress) {
	Integer flag = sqlSession.insert("MallExpressMapper.insert", mallExpress);
	if (flag > 0) {
	    return true;
	}
	return false;
    }

    /**
     * 根据创建时间查询物流信息
     */
    @Override
    public List<MallExpress> findListByCreateTime(String createTime, Integer state) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("createTime", createTime);
	if (state != null) {
	    params.put("state", state);
	}
	List<MallExpress> mallExpressList = sqlSession.selectList("MallExpressMapper.findListByCreateTime", params);
	return mallExpressList;
    }

    /**
     * 根据更新时间查询物流信息
     */
    @Override
    public List<MallExpress> findListByUpdateTime(String updateTime, Integer state) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("updateTime", updateTime);
	if (state != null) {
	    params.put("state", state);
	}
	List<MallExpress> mallExpressList = sqlSession.selectList("MallExpressMapper.findListByUpdateTime", params);
	return mallExpressList;
    }

    /**
     * 根据用户Id查询物流信息
     */
    @Override
    public List<MallExpress> findByUserId(Long userId, Integer state, Long startId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("userId", userId);
	params.put("limit", limit);
	if (state != null) {
	    params.put("state", state);
	}
	List<MallExpress> mallExpressList = new ArrayList<MallExpress>();
	if (startId == null) {
	    String cacheKey = getCustomCache("findByUserId", userId);
	    mallExpressList = memcacheService.get(cacheKey);
	    if (mallExpressList == null) {
		mallExpressList = sqlSession.selectList("MallExpressMapper.findByUserId", params);
		if (mallExpressList.size() > 0) {
		    memcacheService.set(cacheKey, mallExpressList);
		}
	    }
	} else {
	    params.put("startId", startId);
	    mallExpressList = sqlSession.selectList("MallExpressMapper.findByUserId", params);
	}
	return mallExpressList;
    }

    /**
     * 根据物流单号查询物流信息
     * 
     * @param expressType
     *            快递公司类型
     * @param expressNO
     *            快递单号
     */
    @Override
    public MallExpress findByExpressNO(Integer expressType, String expressNO) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("expressType", expressType);
	params.put("expressNO", expressNO);
	MallExpress mallExpress = sqlSession.selectOne("MallExpressMapper.findByExpressNO", params);
	return mallExpress;
    }

    /**
     * 更新物流信息
     */
    @Override
    public boolean update(MallExpress mallExpress) {
	boolean succee = sqlSession.update("MallExpressMapper.update", mallExpress) > 0;
	if (succee) {
	    cleanCache(mallExpress);
	}
	return succee;
    }

    /**
     * 清楚对象缓存
     * 
     * @param mallExpress
     */
    private void cleanCache(MallExpress mallExpress) {
	// 清楚单个缓存
	memcacheService.delete(getCacheId(mallExpress.getOrderId()));

	// 清楚列表缓存
	String cacheKey = getCustomCache("findByUserId", mallExpress.getUserId());
	memcacheService.delete(cacheKey);
    }

}
