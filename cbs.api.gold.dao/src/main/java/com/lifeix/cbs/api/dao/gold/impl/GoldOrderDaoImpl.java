package com.lifeix.cbs.api.dao.gold.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.api.dao.ContentGoldDaoSupport;
import com.lifeix.cbs.api.dao.gold.GoldOrderDao;
import com.lifeix.cbs.api.dto.gold.GoldOrder;

@Repository("goldOrderDao")
public class GoldOrderDaoImpl extends ContentGoldDaoSupport implements GoldOrderDao {

    @Override
    public GoldOrder findById(Long id) {
	GoldOrder goldOrder = sqlSession.selectOne("GoldOrderMapper.findById", id);
	return goldOrder;
    }

    @Override
    public boolean insert(GoldOrder goldOrder) {
	int num = sqlSession.insert("GoldOrderMapper.insertAndGetPrimaryKey", goldOrder);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean update(GoldOrder goldOrder) {
	int num = sqlSession.update("GoldOrderMapper.update", goldOrder);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean delete(GoldOrder goldOrder) {
	int num = sqlSession.delete("GoldOrderMapper.delete", goldOrder);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public Long insertAndGetPrimaryKey(GoldOrder goldOrder) {
	sqlSession.insert("GoldOrderMapper.insertAndGetPrimaryKey", goldOrder);
	if (goldOrder.getOrderId() > 0) {
	    return goldOrder.getOrderId();
	} else {
	    return -1L;
	}
    }

    @Override
    public GoldOrder findOrderByOrderNO(String orderNO) {
	GoldOrder goldOrder = sqlSession.selectOne("GoldOrderMapper.findOrderByOrderNO", orderNO);
	return goldOrder;
    }

    @Override
    public Long getOrderNumber(Long userId, Integer statu) {
	Map<String,Object> param = new HashMap<String,Object>();
	param.put("userId", userId);
	param.put("statu", statu);
	Long count = sqlSession.selectOne("GoldOrderMapper.getOrderNumber", param);
	return count;
    }

    @Override
    public List<GoldOrder> findOrders(Long userId, Integer statu, Integer start, Integer limit) {
	Map<String,Object> param = new HashMap<String,Object>();
	param.put("userId", userId);
	param.put("statu", statu);
	if(start!=null){
	    param.put("start", start);
	}
	if(limit==null){
	    //默认20条数据
	    limit=20;
	}
	param.put("limit", limit);
	List<GoldOrder> goldOrderList = sqlSession.selectList("GoldOrderMapper.findOrderss", param);
	return goldOrderList;
    }

}
