package com.lifeix.cbs.message.dao.placard.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.message.dao.MessageDaoSupport;
import com.lifeix.cbs.message.dao.placard.PlacardTempletDao;
import com.lifeix.cbs.message.dto.PlacardTemplet;

/**
 * Created by lhx on 15-10-19 下午1:53
 *
 * @Description
 */
@Repository("placardTempletDao")
public class PlacardTempletDaoImpl extends MessageDaoSupport implements PlacardTempletDao {

    private final String PLACARD_LAST = this.getClass().getName() + ":placard_last";

    @Override
    public PlacardTemplet findById(Long templetId) {
	String cacheKey = getCacheId(templetId);

	PlacardTemplet placardTemplet = memcacheService.get(cacheKey);

	if (placardTemplet == null) {
	    placardTemplet = sqlSession.selectOne("PlacardTempletMapper.findById", templetId);
	    if (placardTemplet != null) {
		memcacheService.set(cacheKey, placardTemplet);
	    }
	}
	return placardTemplet;
    }

    /**
     * 获取最后一次公告
     *
     * @return
     */
    @Override
    public PlacardTemplet findLastTemplet() {
	// 获取最后一次公告缓存
	PlacardTemplet placardTemplet = memcacheService.get(PLACARD_LAST);

	if (placardTemplet == null) {
	    placardTemplet = sqlSession.selectOne("PlacardTempletMapper.findLastTemplet");
	    if (placardTemplet != null) {
		memcacheService.set(PLACARD_LAST, placardTemplet);
	    }
	}
	return placardTemplet;
    }

    /**
     * 系统公告数量
     *
     * @param disableFlag
     * @return
     */
    @Override
    public Integer getPlacardTempletCount(Boolean disableFlag) {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("disableFlag", disableFlag);
	return sqlSession.selectOne("PlacardTempletMapper.getPlacardTempletCount", map);
    }

    @Override
    public List<PlacardTemplet> findPlacardTemplet(Boolean disableFlag, boolean endTimeFlag, int start, int showData) {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("disableFlag", disableFlag);
	map.put("endTimeFlag", endTimeFlag);
	map.put("start", start);
	map.put("showData", showData);
	return sqlSession.selectList("PlacardTempletMapper.findPlacardTemplet", map);

    }

    @Override
    public List<PlacardTemplet> findPlacardsInner(Long startId, Integer limit) {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("startId", startId);
	map.put("limit", limit);
	return sqlSession.selectList("PlacardTempletMapper.findPlacardsInner", map);
    }

    @Override
    public PlacardTemplet insert(PlacardTemplet placardTemplet) {
	sqlSession.insert("PlacardTempletMapper.insert", placardTemplet);

	return placardTemplet;
    }

    @Override
    public boolean update(PlacardTemplet placardTemplet) {
	String cacheKey = getCacheId(placardTemplet.getTempletId());
	memcacheService.delete(cacheKey);
	return sqlSession.update("PlacardTempletMapper.update", placardTemplet) > 0;
    }

    /**
     * 公告统计
     *
     * @param templetId
     * @return
     */
    @Override
    public boolean placardCount(Long templetId) {
	return sqlSession.update("PlacardTempletMapper.placardCount", templetId) > 0;
    }

    @Override
    public boolean delete(Long templetId) {
	return sqlSession.delete("PlacardTempletMapper.delete", templetId) > 0;
    }
}
