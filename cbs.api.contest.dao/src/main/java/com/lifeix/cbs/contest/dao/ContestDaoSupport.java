package com.lifeix.cbs.contest.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.lifeix.cbs.api.common.dao.DaoSupport;
import com.lifeix.framework.memcache.MemcacheService;

public abstract class ContestDaoSupport extends DaoSupport {

    @Autowired
    @Qualifier("contestSqlSession")
    protected SqlSession sqlSession;

    @Autowired
    protected MemcacheService memcacheService;

    public void setSqlSession(SqlSession sqlSession) {
	this.sqlSession = sqlSession;
    }

    public void setMemcacheService(MemcacheService memcacheService) {
	this.memcacheService = memcacheService;
    }

}
