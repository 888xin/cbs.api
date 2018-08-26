package com.lifeix.cbs.contest.dao.bb.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbBetOddevenDao;
import com.lifeix.cbs.contest.dto.bet.BetOddeven;

/**
 * Created by lhx on 16-5-4 下午3:43
 * 
 * @Description
 */
@Repository("bbBetOddevenDao")
public class BbBetOddevenDaoImpl extends ContestDaoSupport implements BbBetOddevenDao {

    @Override
    public BetOddeven selectById(long id) {
	return sqlSession.selectOne("BbBetOddevenMapper.selectById", id);
    }

    @Override
    public Long insertAndGetPrimaryKey(BetOddeven BetOddeven) {
	sqlSession.insert("BbBetOddevenMapper.insertAndGetPrimaryKey", BetOddeven);
	return BetOddeven.getbId();
    }

    @Override
    public List<BetOddeven> findBbBetOddeven(Long contestId, Long userId, boolean settle, Long startId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
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
	return sqlSession.selectList("BbBetOddevenMapper.findBbBetOddeven", params);
    }

    @Override
    public boolean updateContentId(BetOddeven BetOddeven) {
	return sqlSession.update("BbBetOddevenMapper.updateContentId", BetOddeven) > 0;
    }

    @Override
    public boolean updateContentIdById(Long contentId, Integer bId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contentId", contentId);
	params.put("bId", bId);
	return sqlSession.update("BbBetOddevenMapper.updateContentIdById", params) > 0;
    }

    @Override
    public BetOddeven selectByBet(Long contestId, Long userId, int support) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("userId", userId);
	params.put("support", support);
	return sqlSession.selectOne("BbBetOddevenMapper.selectByBet", params);
    }

    @Override
    public boolean updateSettle(Long bId, Double back, Integer status) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("bId", bId);
	params.put("back", back);
	params.put("status", status);
	return sqlSession.update("BbBetOddevenMapper.updateSettle", params) > 0;
    }

    @Override
    public List<BetOddeven> findBbBetOddevenList(Long contestId, Long userId, boolean settle, Long startId, int limit,
	    Date startTime, Date endTime) {
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
	return sqlSession.selectList("BbBetOddevenMapper.findBbBetOddevenList", params);
    }

    @Override
    public boolean deleteById(Long id) {
	return sqlSession.delete("BbBetOddevenMapper.deleteById", id) > 0;
    }

    @Override
    public List<BetOddeven> findBbBetOddevenByContestId(Long contestId, Long startId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("startId", startId);
	params.put("limit", limit);
	return sqlSession.selectList("BbBetOddevenMapper.findBbBetOddevenByContestId", params);
    }

    @Override
    public int countUserMixBet(Long contestId, Long userId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("userId", userId);
	return sqlSession.selectOne("BbBetOddevenMapper.countUserMixBet", params);
    }

    @Override
    public BetOddeven selectByContent(Long contentId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contentId", contentId);
	return sqlSession.selectOne("BbBetOddevenMapper.selectByContent", params);
    }
}
