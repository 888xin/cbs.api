package com.lifeix.cbs.contest.dao.yy.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.yy.YyBetDao;
import com.lifeix.cbs.contest.dto.yy.YyBet;

@Service("yyBetDao")
public class YyBetDaoImpl extends ContestDaoSupport implements YyBetDao {

    @Override
    public YyBet selectById(long id) {
	return sqlSession.selectOne("YyBetMapper.selectById", id);
    }

    @Override
    public Long insertAndGetPrimaryKey(YyBet entity) {
	sqlSession.insert("YyBetMapper.insertAndGetPrimaryKey", entity);
	return entity.getId();
    }

    @Override
    public YyBet selectByBet(Long contestId, Long userId, int support) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("userId", userId);
	params.put("support", support);
	return sqlSession.selectOne("YyBetMapper.selectByBet", params);
    }

    @Override
    public boolean updateContentId(YyBet entity) {
	int res = sqlSession.update("YyBetMapper.updateContentId", entity);
	if (res > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean updateSettle(Long id, Double back, Integer status) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("id", id);
	params.put("back", back);
	params.put("status", status);
	int res = sqlSession.update("YyBetMapper.updateSettle", params);
	if (res > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean updateContentIdById(Long contentId, Integer id) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contentId", contentId);
	params.put("id", id);
	int res = sqlSession.update("YyBetMapper.updateContentIdById", params);
	if (res > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public List<YyBet> findYyBet(Long contestId, Long userId, boolean settle, Long startId, int limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	if (contestId != null) {
	    params.put("contestId", contestId);
	}
	if (userId != null) {
	    params.put("userId", userId);
	}
	if (settle) {
	    params.put("status", 0);
	}
	if (startId != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	return sqlSession.selectList("YyBetMapper.findYyBet", params);
    }

    @Override
    public List<YyBet> findUserYyBet(Long userId, Long startId, int limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	if (userId != null) {
	    params.put("userId", userId);
	}
	if (startId != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	return sqlSession.selectList("YyBetMapper.findUserYyBet", params);
    }

    @Override
    public List<YyBet> findYyBetList(Long contestId, Long userId, boolean settle, Long startId, int limit, Date startTime,
	    Date endTime) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("userId", userId);
	if (settle) {
	    params.put("status", 0);
	}
	params.put("startId", startId);
	params.put("limit", limit);
	params.put("startTime", startTime);
	params.put("endTime", endTime);
	return sqlSession.selectList("YyBetMapper.findYyBetList", params);
    }

    @Override
    public int countUserMixBet(Long contestId, Long userId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("userId", userId);
	return sqlSession.selectOne("YyBetMapper.countUserMixBet", params);
    }

}
