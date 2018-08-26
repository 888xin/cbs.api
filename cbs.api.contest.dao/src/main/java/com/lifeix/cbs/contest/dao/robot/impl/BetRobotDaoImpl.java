package com.lifeix.cbs.contest.dao.robot.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.robot.BetRobotDao;
import com.lifeix.cbs.contest.dto.robot.BetRobot;

@Service("betRobotDao")
public class BetRobotDaoImpl extends ContestDaoSupport implements BetRobotDao {

    @Override
    public BetRobot selectById(Long userId) {
	String cacheKey = getCacheId(userId);

	BetRobot robot = memcacheService.get(cacheKey);

	if (robot == null) {
	    robot = sqlSession.selectOne("BetRobotMapper.selectById", userId);
	    if (robot != null) {
		memcacheService.set(cacheKey, robot);
	    }
	}

	return robot;
    }

    @Override
    public boolean insert(BetRobot entity) {
	int res = sqlSession.insert("BetRobotMapper.insert", entity);
	return res > 0;
    }

    @Override
    public boolean update(BetRobot entity) {
	int res = sqlSession.update("BetRobotMapper.update", entity);
	if (res > 0) {
	    deleteObjCache(entity.getUserId());
	    return true;
	}
	return false;
    }

    @Override
    public boolean delete(BetRobot entity) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("userId", entity.getUserId());
	int res = sqlSession.delete("BetRobotMapper.delete", params);
	if (res > 0) {
	    deleteObjCache(entity.getUserId());
	    return true;
	}
	return false;
    }

    @Override
    public int getRobotCount(Boolean closeFlag) {
	Map<String, Object> params = new HashMap<String, Object>();
	if (closeFlag != null)
	    params.put("closeFlag", closeFlag);
	Integer count = sqlSession.selectOne("BetRobotMapper.getRobotCount", params);
	return (count == null || count <= 0) ? 0 : count.intValue();
    }

    @Override
    public List<BetRobot> findRobotList(Boolean closeFlag, int start, int limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	if (closeFlag != null) {
	    params.put("closeFlag", closeFlag);
	}
	params.put("start", start);
	params.put("num", limit);
	List<BetRobot> list = sqlSession.selectList("BetRobotMapper.findRobotList", params);
	return list;
    }

    /**
     * 对象缓存
     * 
     * @param contest
     * @param insertFlag
     */
    public void deleteObjCache(Long userId) {
	// 单个对象缓存
	String cacheKey = getCacheId(userId);
	memcacheService.delete(cacheKey);
    }

}
