package com.lifeix.cbs.content.dao.game.impl;

import com.lifeix.cbs.content.dao.ContentDaoSupport;
import com.lifeix.cbs.content.dao.game.ZodiacAnimalDao;
import com.lifeix.cbs.content.dto.game.ZodiacAnimal;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lhx on 15-12-11 上午11:34
 * 
 * @Description
 */
@Repository("zodiacAnimalDao")
public class ZodiacAnimalDaoImpl extends ContentDaoSupport implements ZodiacAnimalDao {

    private final String NOW = this.getClass().getName() + ":now";

    @Override
    public boolean insert(ZodiacAnimal zodiacAnimal) {
	return sqlSession.insert("ZodiacAnimalMapper.insert", zodiacAnimal) > 0;
    }

    
    @Override
    public ZodiacAnimal findOne(Date time) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("now", time);
	return sqlSession.selectOne("ZodiacAnimalMapper.findOneByTime", params);
    }
    
    @Override
    public ZodiacAnimal findOne(Date now, Integer id) {
	ZodiacAnimal zodiacAnimal;
	String cacheKey = null;
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("now", now);
	params.put("id", id);
	if (now != null) {
	    zodiacAnimal = memcacheService.get(NOW);
	    long nowTime = new Date().getTime();
	    if (zodiacAnimal != null && nowTime >= zodiacAnimal.getEndTime().getTime()) {
		// 重新获取当前期数
		zodiacAnimal = sqlSession.selectOne("ZodiacAnimalMapper.findOne", params);
		if (zodiacAnimal != null) {
		    memcacheService.set(NOW, zodiacAnimal);
		    return zodiacAnimal;
		}
	    }
	} else if (id != null) {
	    cacheKey = getCacheId(id);
	    zodiacAnimal = memcacheService.get(cacheKey);
	} else {
	    return null;
	}

	if (zodiacAnimal == null) {
	    zodiacAnimal = sqlSession.selectOne("ZodiacAnimalMapper.findOne", params);
	    if (zodiacAnimal != null) {
		if (now != null) {
		    memcacheService.set(NOW, zodiacAnimal);
		} else {
		    memcacheService.set(cacheKey, zodiacAnimal);
		}
	    }
	}
	return zodiacAnimal;
    }

    @Override
    public boolean update(Integer id, Integer status, String lottery) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("id", id);
	params.put("status", status);
	params.put("lottery", lottery);
	if (sqlSession.update("ZodiacAnimalMapper.update", params) > 0) {
	    String cacheKey = getCacheId(id);
	    memcacheService.delete(cacheKey);
	    return true;
	}
	return false;
    }

    @Override
    public List<ZodiacAnimal> findNoLotteryList(String startTime, String endTime) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("startTime", startTime);
	params.put("endTime", endTime);
	return sqlSession.selectList("ZodiacAnimalMapper.findNoLotteryList", params);
    }

    @Override
    public List<ZodiacAnimal> findZodiacs(Integer startId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("startId", startId);
	params.put("limit", limit);
	return sqlSession.selectList("ZodiacAnimalMapper.findZodiacs", params);
    }

    @Override
    public boolean insertBatch(List<ZodiacAnimal> animals) {
	return sqlSession.insert("ZodiacAnimalMapper.insertBatch", animals) > 0;
    }

    @Override
    public ZodiacAnimal findLast() {
	return sqlSession.selectOne("ZodiacAnimalMapper.findLast");
    }


}
