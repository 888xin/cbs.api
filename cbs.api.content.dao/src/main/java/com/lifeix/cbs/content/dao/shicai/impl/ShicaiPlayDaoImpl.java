package com.lifeix.cbs.content.dao.shicai.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.content.dao.ContentDaoSupport;
import com.lifeix.cbs.content.dao.shicai.ShicaiPlayDao;
import com.lifeix.cbs.content.dto.shicai.ShicaiPlay;


/**
 * @author wenhuans
 * 2015年11月3日 下午6:17:20
 * 
 */
@Repository("shicaiPlayDao")
public class ShicaiPlayDaoImpl extends ContentDaoSupport implements ShicaiPlayDao{

    
    
    @Override
    public ShicaiPlay findById(Long id) {
	return sqlSession.selectOne("ShicaiPlayMapper.findById", id);
    }
    
    @Override
    public Map<Long, ShicaiPlay> findMapByIds(List<Long> ids){
	return sqlSession.selectMap("ShicaiPlayMapper.findMapByIds", ids, "id");
    }
    
    @Override
    public Long insert(ShicaiPlay shicaiPlay) {
	if(sqlSession.insert("ShicaiPlayMapper.insert", shicaiPlay)>0){
	    return shicaiPlay.getId();
	}
	return -1L;
    }

    @Override
    public ShicaiPlay findByAccountId(Long accountId) {
	ShicaiPlay shicaiPlay = sqlSession.selectOne("ShicaiPlayMapper.findByAccountId", accountId);
	return shicaiPlay;
    }

    @Override
    public ShicaiPlay findByAccountIdAndScId(Long accountId, Long scId){
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("accountId", accountId);
	params.put("scId", scId);
	return sqlSession.selectOne("ShicaiPlayMapper.findByAccountIdAndScId", params);
    }

    @Override
    public Long updateNumAndSumNum(ShicaiPlay shicaiPlay) {
	if(sqlSession.update("ShicaiPlayMapper.updateNumAndSumNum", shicaiPlay)>0){
	    return shicaiPlay.getId();
	}
	return -1L;
    }

    @Override
    public List<ShicaiPlay> findList(Long accountId, Long startId, Long endId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("accountId", accountId);
	params.put("startId", startId);
	params.put("endId", endId);
	params.put("limit", limit);
 	return sqlSession.selectList("ShicaiPlayMapper.findList", params);
    }

}

