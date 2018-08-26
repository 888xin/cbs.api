package com.lifeix.cbs.api.dao.rank.impl;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.dao.ContentDaoSupport;
import com.lifeix.cbs.api.dao.rank.MedalTaskDao;
import com.lifeix.cbs.api.dto.rank.MedalTask;

@Service("medalTaskDao")
public class MedalTaskDaoImpl extends ContentDaoSupport implements MedalTaskDao {

    @Override
    public MedalTask findById(Long taskId) {
	return sqlSession.selectOne("MedalTaskMapper.findById", taskId);
    }

    @Override
    public boolean update(MedalTask entity) {
	return sqlSession.update("MedalTaskMapper.update", entity) >= 1;
    }

}
