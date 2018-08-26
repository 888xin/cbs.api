package com.lifeix.cbs.api.dao.user.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.dao.ContentDaoSupport;
import com.lifeix.cbs.api.dao.user.CbsUserWxDao;
import com.lifeix.cbs.api.dto.user.CbsUserWx;

@Service("cbsUserWxDao")
public class CbsUserWxDaoImpl extends ContentDaoSupport implements CbsUserWxDao {

    @Override
    public List<CbsUserWx> selectByUsers(List<Long> userIds) {
	Map<String, Object> map = new HashMap<>();
	map.put("userIds", userIds);
	return sqlSession.selectList("CbsUserWxMapper.findByIds", map);
    }

    @Override
    public CbsUserWx selectById(long id) {
	return sqlSession.selectOne("CbsUserWxMapper.selectById", id);
    }

    @Override
    public void insertAndGetPrimaryKey(CbsUserWx user) {
	sqlSession.insert("CbsUserWxMapper.insertAndGetPrimaryKey", user);
    }

    @Override
    public Boolean update(CbsUserWx user) {
	int res = sqlSession.update("CbsUserWxMapper.update", user);

	if (res > 0) {
	    return true;
	}
	return false;
    }
}
