package com.lifeix.cbs.content.dao.shicai.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.content.dao.ContentDaoSupport;
import com.lifeix.cbs.content.dao.shicai.ShicaiDao;
import com.lifeix.cbs.content.dto.shicai.Shicai;


/**
 * @author wenhuans
 * 2015年11月3日 下午6:10:53
 * 
 */
@Repository("shicaiDao")
public class ShicaiDaoImpl extends ContentDaoSupport implements ShicaiDao{

    
    
    @Override
    public Long insert(Shicai shicai) {
	
	if(sqlSession.insert("ShicaiMapper.insert", shicai)>0){
	    return shicai.getScId();
	}
	return -1L;
    }

    @Override
    public Shicai findById(Long scId) {
	Shicai shicai = sqlSession.selectOne("ShicaiMapper.findById", scId);
	return shicai;
    }

    @Override
    public Boolean update(Shicai shicai) {
	if(sqlSession.update("ShicaiMapper.update", shicai)>0){
	    return true;
	}
	return false;
    }

    @Override
    public List<Shicai> findList(Long startId, Long endId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("startId", startId);
	params.put("endId", endId);
	params.put("limit", limit);
	return sqlSession.selectList("ShicaiMapper.findList", params);
    }

}

