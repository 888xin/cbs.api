package com.lifeix.cbs.contest.dao.fb.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.fb.FbBetOddevenDao;
import com.lifeix.cbs.contest.dto.bet.BetOddeven;

/**
 * Created by lhx on 16-5-4 下午3:55
 * 
 * @Description
 */
@Repository("fbBetOddevenDao")
public class FbBetOddevenDaoImpl extends ContestDaoSupport implements FbBetOddevenDao {

    @Override
    public BetOddeven selectById(long id) {
	return sqlSession.selectOne("FbBetOddevenMapper.selectById", id);
    }

    @Override
    public Long insertAndGetPrimaryKey(BetOddeven BetOddeven) {
	sqlSession.insert("FbBetOddevenMapper.insertAndGetPrimaryKey", BetOddeven);
	return BetOddeven.getbId();
    }

    @Override
    public List<BetOddeven> findFbBetOddeven(Long contestId, Long userId, boolean settle, Long startId, Integer limit) {
	Map<String, Object> params = new HashMap<>();
	params.put("contestId", contestId);
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
	return sqlSession.selectList("FbBetOddevenMapper.findFbBetOddeven", params);
    }

    @Override
    public boolean updateContentId(BetOddeven BetOddeven) {
	return sqlSession.update("FbBetOddevenMapper.updateContentId", BetOddeven) > 0;
    }

    @Override
    public boolean updateContentIdById(Long contentId, Integer bId) {
	Map<String, Object> params = new HashMap<>();
	params.put("contentId", contentId);
	params.put("bId", bId);
	return sqlSession.update("FbBetOddevenMapper.updateContentIdById", params) > 0;
    }

    @Override
    public BetOddeven selectByBet(Long contestId, Long userId, int support) {
	Map<String, Object> params = new HashMap<>();
	params.put("contestId", contestId);
	params.put("userId", userId);
	params.put("support", support);
	return sqlSession.selectOne("FbBetOddevenMapper.selectByBet", params);
    }

    @Override
    public boolean updateSettle(Long bId, Double back, Integer status) {
	Map<String, Object> params = new HashMap<>();
	params.put("bId", bId);
	params.put("back", back);
	params.put("status", status);
	return sqlSession.update("FbBetOddevenMapper.updateSettle", params) > 0;
    }

    @Override
    public List<BetOddeven> findFbBetOddevenList(Long contestId, Long userId, boolean settle, Long startId, int limit,
	    Date startTime, Date endTime) {
	Map<String, Object> params = new HashMap<>();
	params.put("contestId", contestId);
	params.put("userId", userId);
	if (settle) {
	    params.put("status", 0);
	}
	params.put("startId", startId);
	params.put("limit", limit);
	params.put("startTime", startTime);
	params.put("endTime", endTime);
	return sqlSession.selectList("FbBetOddevenMapper.findFbBetOddevenList", params);
    }

    @Override
    public boolean deleteById(Long id) {
	return sqlSession.delete("FbBetOddevenMapper.deleteById", id) > 0;
    }

    @Override
    public List<BetOddeven> findFbBetOddevenByContestId(Long contestId, Long startId, Integer limit) {
	Map<String, Object> params = new HashMap<>();
	params.put("contestId", contestId);
	params.put("startId", startId);
	params.put("limit", limit);
	return sqlSession.selectList("FbBetOddevenMapper.findFbBetOddevenByContestId", params);
    }

    @Override
    public int countUserMixBet(Long contestId, Long userId) {
	Map<String, Object> params = new HashMap<>();
	params.put("contestId", contestId);
	params.put("userId", userId);
	return sqlSession.selectOne("FbBetOddevenMapper.countUserMixBet", params);
    }

    @Override
    public BetOddeven selectByContent(Long contentId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contentId", contentId);
	return sqlSession.selectOne("FbBetOddevenMapper.selectByContent", params);
    }
}
