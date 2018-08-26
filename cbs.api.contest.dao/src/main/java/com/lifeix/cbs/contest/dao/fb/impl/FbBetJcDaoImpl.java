package com.lifeix.cbs.contest.dao.fb.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.fb.FbBetJcDao;
import com.lifeix.cbs.contest.dto.bet.BetJc;

@Service("fbBetJcDao")
public class FbBetJcDaoImpl extends ContestDaoSupport implements FbBetJcDao {

    @Override
    public BetJc selectById(long id) {
	return sqlSession.selectOne("FbBetJcMapper.selectById", id);
    }

    @Override
    public Long insertAndGetPrimaryKey(BetJc entity) {
	sqlSession.insert("FbBetJcMapper.insertAndGetPrimaryKey", entity);
	return entity.getbId();
    }

    @Override
    public BetJc selectByBet(Long contestId, Long userId, int support) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("userId", userId);
	params.put("support", support);
	return sqlSession.selectOne("FbBetJcMapper.selectByBet", params);
    }

    @Override
    public boolean updateContentId(BetJc entity) {
	int res = sqlSession.update("FbBetJcMapper.updateContentId", entity);
	if (res > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean updateContentIdById(Long contentId, Integer bId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contentId", contentId);
	params.put("bId", bId);
	int res = sqlSession.update("FbBetJcMapper.updateContentIdById", params);
	if (res > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean updateSettle(Long bId, Double back, Integer status) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("bId", bId);
	params.put("back", back);
	params.put("status", status);
	int res = sqlSession.update("FbBetJcMapper.updateSettle", params);
	if (res > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean updateInner(Long bId, Double back, Double homeRoi, Double drawRoi, Double awayRoi) {
        Map<String, Object> params = new HashMap<>();
        params.put("bId", bId);
        params.put("back", back);
        params.put("homeRoi", homeRoi);
        params.put("drawRoi", drawRoi);
        params.put("awayRoi", awayRoi);
        int res = sqlSession.update("FbBetJcMapper.updateInner", params);
        if (res > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<BetJc> findFbBetJc(Long contestId, Long userId, boolean settle, Long startId, int limit) {
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
	return sqlSession.selectList("FbBetJcMapper.findFbBetJc", params);
    }

    @Override
    public List<BetJc> findFbBetJcList(Long contestId, Long userId, boolean settle, Long startId, int limit, Date startTime,
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
	return sqlSession.selectList("FbBetJcMapper.findFbBetJcList", params);
    }

    @Override
    public boolean deleteById(Long id) {
	return sqlSession.delete("FbBetJcMapper.deleteById", id) > 0;
    }

    @Override
    public List<BetJc> findFbBetJcByContestId(Long contestId, Long startId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	if (contestId != null) {
	    params.put("contestId", contestId);
	}
	if (startId != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	return sqlSession.selectList("FbBetJcMapper.findFbBetJcByContestId", params);
    }

    @Override
    public int countUserMixBet(Long contestId, Long userId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("userId", userId);
	return sqlSession.selectOne("FbBetJcMapper.countUserMixBet", params);
    }

    /**
     * 根据战绩查询下单记录
     * 
     * @param contentId
     * @return
     */
    @Override
    public BetJc selectByContent(Long contentId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contentId", contentId);
	return sqlSession.selectOne("FbBetJcMapper.selectByContent", params);
    }
}
