package com.lifeix.cbs.contest.dao.bb.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbBetSizeDao;
import com.lifeix.cbs.contest.dto.bet.BetSize;

/**
 * Created by lhx on 16-4-27 上午10:19
 * 
 * @Description
 */
@Repository("bbBetSizeDao")
public class BbBetSizeDaoImpl extends ContestDaoSupport implements BbBetSizeDao {

    @Override
    public BetSize selectById(long id) {
	return sqlSession.selectOne("BbBetSizeMapper.selectById", id);
    }

    @Override
    public Long insertAndGetPrimaryKey(BetSize betSize) {
	sqlSession.insert("BbBetSizeMapper.insertAndGetPrimaryKey", betSize);
	return betSize.getbId();
    }

    @Override
    public List<BetSize> findBbBetSize(Long contestId, Long userId, boolean settle, Long startId, Integer limit) {
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
	return sqlSession.selectList("BbBetSizeMapper.findBbBetSize", params);
    }

    @Override
    public boolean updateContentId(BetSize betSize) {
	return sqlSession.update("BbBetSizeMapper.updateContentId", betSize) > 0;
    }

    @Override
    public boolean updateContentIdById(Long contentId, Integer bId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contentId", contentId);
	params.put("bId", bId);
	return sqlSession.update("BbBetSizeMapper.updateContentIdById", params) > 0;
    }

    @Override
    public BetSize selectByBet(Long contestId, Long userId, int support) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("userId", userId);
	params.put("support", support);
	return sqlSession.selectOne("BbBetSizeMapper.selectByBet", params);
    }

    @Override
    public boolean updateSettle(Long bId, Double back, Integer status) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("bId", bId);
	params.put("back", back);
	params.put("status", status);
	return sqlSession.update("BbBetSizeMapper.updateSettle", params) > 0;
    }

    @Override
    public boolean updateInner(Long bId, Double back, Double bigRoi, Double tinyRoi) {
        Map<String, Object> params = new HashMap<>();
        params.put("bId", bId);
        params.put("back", back);
        params.put("bigRoi", bigRoi);
        params.put("tinyRoi", tinyRoi);
        int res = sqlSession.update("BbBetSizeMapper.updateInner", params);
        if (res > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<BetSize> findBbBetSizeList(Long contestId, Long userId, boolean settle, Long startId, int limit,
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
	return sqlSession.selectList("BbBetSizeMapper.findBbBetSizeList", params);
    }

    @Override
    public boolean deleteById(Long id) {
	return sqlSession.delete("BbBetSizeMapper.deleteById", id) > 0;
    }

    @Override
    public List<BetSize> findBbBetSizeByContestId(Long contestId, Long startId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("startId", startId);
	params.put("limit", limit);
	return sqlSession.selectList("BbBetSizeMapper.findBbBetSizeByContestId", params);
    }

    @Override
    public int countUserMixBet(Long contestId, Long userId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("userId", userId);
	return sqlSession.selectOne("BbBetSizeMapper.countUserMixBet", params);
    }

    @Override
    public BetSize selectByContent(Long contentId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contentId", contentId);
	return sqlSession.selectOne("BbBetSizeMapper.selectByContent", params);
    }
}
