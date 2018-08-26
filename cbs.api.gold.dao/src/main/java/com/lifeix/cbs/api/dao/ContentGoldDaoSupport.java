package com.lifeix.cbs.api.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.lifeix.cbs.api.common.dao.DaoSupport;
import com.lifeix.framework.memcache.MemcacheService;

public abstract class ContentGoldDaoSupport extends DaoSupport {
    
    public static final Logger LOG = LoggerFactory.getLogger(ContentGoldDaoSupport.class);

    @Autowired
    @Qualifier("contentGoldSqlSession")
    protected SqlSession sqlSession;

    @Autowired
    protected MemcacheService memcacheService;
}
