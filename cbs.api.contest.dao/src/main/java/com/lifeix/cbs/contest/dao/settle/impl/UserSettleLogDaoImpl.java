package com.lifeix.cbs.contest.dao.settle.impl;

import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.settle.UserSettleLogDao;
import com.lifeix.cbs.contest.dto.settle.UserSettleLog;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("userSettleLogDao")
public class UserSettleLogDaoImpl extends ContestDaoSupport implements UserSettleLogDao {

    @Override
    public List<UserSettleLog> findUserSettleListById(Long id, Boolean isLongbi, int limit) {
	Map<String, Object> map = new HashMap<String, Object>();

	map.put("id", id);
	if (isLongbi != null) {
	    map.put("isLongbi", isLongbi);
	}

	map.put("limit", limit);
	return sqlSession.selectList("UserSettleLogMapper.findUserSettleListById", map);
    }

    @Override
    public boolean insert(UserSettleLog entity) {
	int res = sqlSession.insert("UserSettleLogMapper.insert", entity);
	if (res > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public UserSettleLog findById(Long id) {
	Map<String, Object> map = new HashMap<String, Object>();

	map.put("id", id);
	return sqlSession.selectOne("UserSettleLogMapper.findById", map);
    }
    
    @Override
    public List<UserSettleLog> findByIds(List<Long> ids) {
	Map<String, Object> map = new HashMap<String, Object>();

	map.put("ids", ids);
	return sqlSession.selectList("UserSettleLogMapper.findByIds", map);
    }
    

    @Override
    public List<UserSettleLog> getUserSettleLogs(Integer userId, Integer page, String startTime, String endTime,
	    Integer limit) {

	Map<String, Object> map = new HashMap<String, Object>();
	// 时间范围为前开后闭
	Date start = CbsTimeUtils.formatDateB(startTime);
	Date endTimeDate = CbsTimeUtils.formatDateB(endTime);
	Calendar cal = Calendar.getInstance();
	cal.setTime(endTimeDate);
	cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + 1);
	Date end = cal.getTime();
	if (userId != null) {
	    map.put("userId", userId);
	}
	map.put("page", (page - 1) * limit);
	map.put("startTime", start);
	map.put("endTime", end);
	map.put("limit", limit);
	return sqlSession.selectList("UserSettleLogMapper.getUserSettleLogs", map);
    }

    @Override
    public Integer getUserSettleLogCounts(Long userId, Integer status, Date startTime, Date endTime) {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("userId", userId);
	map.put("startTime", startTime);
	map.put("endTime", endTime);
	map.put("status", status);
	return sqlSession.selectOne("UserSettleLogMapper.getUserSettleLogCounts", map);
    }

    @Override
    public UserSettleLog getUserSettleLog(Long userId, Integer type, Long contestId, Integer playId, Integer support) {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("userId", userId);
	map.put("type", type);
	map.put("contestId", contestId);
	map.put("playId", playId);
	map.put("support", support);
	return sqlSession.selectOne("UserSettleLogMapper.getUserSettleLog", map);
    }

    @Override
    public Integer deleteByContestId(Integer contestType, Long contestId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contestId", contestId);
        map.put("type", contestType);
        return sqlSession.delete("UserSettleLogMapper.deleteByContestId", map) ;
    }

}
