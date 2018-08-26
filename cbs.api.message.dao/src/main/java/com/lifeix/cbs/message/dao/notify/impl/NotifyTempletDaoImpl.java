package com.lifeix.cbs.message.dao.notify.impl;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.message.dao.MessageDaoSupport;
import com.lifeix.cbs.message.dao.notify.NotifyTempletDao;
import com.lifeix.cbs.message.dto.notify.NotifyTemplet;

@Service("notifyTempletDao")
public class NotifyTempletDaoImpl extends MessageDaoSupport implements NotifyTempletDao {

    @Override
    public NotifyTemplet findById(Long templetId) {
	return sqlSession.selectOne("NotifyTempletMapper.findById", templetId);
    }

}
