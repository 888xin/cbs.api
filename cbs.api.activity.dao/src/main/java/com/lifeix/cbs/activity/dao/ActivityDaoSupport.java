package com.lifeix.cbs.activity.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.lifeix.cbs.api.common.dao.DaoSupport;
import com.lifeix.framework.memcache.MemcacheService;

public abstract class ActivityDaoSupport extends DaoSupport {

    @Autowired
    @Qualifier("activitySqlSession")
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
