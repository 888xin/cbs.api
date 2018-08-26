package com.lifeix.cbs.content.dao.boot.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.content.dao.ContentDaoSupport;
import com.lifeix.cbs.content.dao.boot.BootInfoDao;
import com.lifeix.cbs.content.dto.boot.BootInfo;

@Service("bootInfoDao")
public class BootInfoDaoImpl extends ContentDaoSupport implements BootInfoDao {

    @Override
    public Boolean insert(BootInfo info) {
	int res = sqlSession.update("BootInfoMapper.insert", info);

	if (res > 0) {
	    deleteCache();
	    return true;
	}
	return false;
    }

    @Override
    public Boolean deleteById(long id) {

	int res = sqlSession.delete("BootInfoMapper.deleteById", id);

	if (res > 0) {
	    deleteCache();
	    return true;
	}

	return false;
    }

    @Override
    public Boolean disableById(long id) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("id", id);
	params.put("enableFlag", false);
	int res = sqlSession.update("BootInfoMapper.AbleChangeById", params);
	if (res > 0) {
	    deleteCache();
	    return true;
	}
	return false;
    }

    @Override
    public Boolean ableById(long id) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("id", id);
	params.put("enableFlag", true);
	int res = sqlSession.update("BootInfoMapper.AbleChangeById", params);
	if (res > 0) {
	    deleteCache();
	    return true;
	}
	return false;
    }

    @Override
    public List<BootInfo> findBootInfos(Long startId, int limit) {
	String sqlMap = "BootInfoMapper.findBootInfos";
	Map<String, Object> params = new HashMap<String, Object>();
	if (startId != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	return sqlSession.selectList(sqlMap, params);
    }

    @Override
    public BootInfo selectLast() {
	String cacheKey = getCustomCache("selectLast");

	BootInfo info = memcacheService.get(cacheKey);

	if (info == null) {
	    info = sqlSession.selectOne("BootInfoMapper.selectLast");
	    if (info != null) {
		memcacheService.set(cacheKey, info);
	    }
	}
	return info;
    }

    @Override
    public boolean edit(BootInfo bootInfo) {
	boolean flag = sqlSession.update("BootInfoMapper.update", bootInfo)>0;
	if(flag){
	    deleteCache();
	}
	return flag;
    }

    public void deleteCache() {

	// 最新开机信息缓存
	String cacheKey = getCustomCache("selectLast");
	memcacheService.delete(cacheKey);

    }

    @Override
    public BootInfo findOneById(Long id) {
	return sqlSession.selectOne("BootInfoMapper.findOneById", id);

    }

}
