package com.lifeix.cbs.api.dao.user.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.dao.ContentDaoSupport;
import com.lifeix.cbs.api.dao.user.UserRankLogDao;
import com.lifeix.cbs.api.dto.user.CbsUserRankLog;

@Service("userRankLogDao")
public class UserRankLogDaoImpl extends ContentDaoSupport implements UserRankLogDao {

    @Override
    public List<CbsUserRankLog> findAll() {
	return sqlSession.selectList("UserRankLogMapper.findAll");
    }

    @Override
    public boolean update(CbsUserRankLog rankLog) {
	return sqlSession.update("UserRankLogMapper.update", rankLog) > 0;
    }

    @Override
    public boolean insert(CbsUserRankLog numRankLog) {
	return sqlSession.insert("UserRankLogMapper.insert", numRankLog) > 0;
    }


}
