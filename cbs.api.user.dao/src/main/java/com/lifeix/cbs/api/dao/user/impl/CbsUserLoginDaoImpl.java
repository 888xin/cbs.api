package com.lifeix.cbs.api.dao.user.impl;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.dao.ContentDaoSupport;
import com.lifeix.cbs.api.dao.user.CbsUserLoginDao;
import com.lifeix.cbs.api.dto.user.CbsUserLogin;

@Service("cbsUserLoginDao")
public class CbsUserLoginDaoImpl extends ContentDaoSupport implements CbsUserLoginDao {

    @Override
    public CbsUserLogin selectById(long userId) {
	CbsUserLogin cbsUserLogin = sqlSession.selectOne("CbsUserLoginMapper.selectById", userId);
	return cbsUserLogin;
    }

    @Override
    public boolean insert(CbsUserLogin cbsUserLogin) {
	int num = sqlSession.insert("CbsUserLoginMapper.insert", cbsUserLogin);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}

    }

    @Override
    public Boolean update(CbsUserLogin cbsUserLogin) {
	int num = sqlSession.insert("CbsUserLoginMapper.update", cbsUserLogin);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}
    }

}
