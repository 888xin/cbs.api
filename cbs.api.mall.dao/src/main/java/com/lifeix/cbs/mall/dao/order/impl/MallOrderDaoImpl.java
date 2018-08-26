package com.lifeix.cbs.mall.dao.order.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.mall.dao.MallDaoSupport;
import com.lifeix.cbs.mall.dao.order.MallOrderDao;
import com.lifeix.cbs.mall.dto.order.MallOrder;

/**
 * 商城订单
 * 
 * @author lifeix
 * 
 */
@Repository("mallOrderDao")
public class MallOrderDaoImpl extends MallDaoSupport implements MallOrderDao {

    @Override
    public boolean insert(MallOrder mallOrder) {
	int res = sqlSession.insert("MallOrderMapper.insert", mallOrder);
	if (res > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public MallOrder selectById(Long id) {
	return sqlSession.selectOne("MallOrderMapper.selectById", id);
    }

    @Override
    public boolean update(MallOrder entity) {
	int res = sqlSession.update("MallOrderMapper.update", entity);
	if (res > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public List<MallOrder> findMallOrders(Long userId, List<Integer> status, Long startId, int limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	if (userId != null) {
	    params.put("userId", userId);
	}
	if (status != null) {
	    params.put("status", status);
	}
	if (startId != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	return sqlSession.selectList("MallOrderMapper.findMallOrders", params);
    }

}
