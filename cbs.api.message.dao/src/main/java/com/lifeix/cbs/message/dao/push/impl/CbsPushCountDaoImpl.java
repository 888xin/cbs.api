package com.lifeix.cbs.message.dao.push.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.message.dao.MessageDaoSupport;
import com.lifeix.cbs.message.dao.push.CbsPushCountDao;
import com.lifeix.cbs.message.dto.push.CbsPushCount;

@Service("cbsPushCountDao")
public class CbsPushCountDaoImpl extends MessageDaoSupport implements CbsPushCountDao {

    @Override
    public boolean replace(CbsPushCount pushCount) {
	return sqlSession.insert("CbsPushCountMapper.replace", pushCount) > 0;
    }

    @Override
    public List<CbsPushCount> findRoiPushs(Integer showData) {
	
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("limit", showData);
	
	return sqlSession.selectList("CbsPushCountMapper.findRoiPushs", map);
    }

    @Override
    public boolean update(CbsPushCount count) {
	return sqlSession.update("CbsPushCountMapper.update", count) > 0;
    }

}
