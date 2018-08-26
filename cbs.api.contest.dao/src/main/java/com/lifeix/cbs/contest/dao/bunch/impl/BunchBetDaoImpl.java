package com.lifeix.cbs.contest.dao.bunch.impl;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bunch.BunchBetDao;
import com.lifeix.cbs.contest.dto.bunch.BunchBet;
import com.lifeix.cbs.contest.dto.bunch.BunchContest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by lhx on 16-5-16 下午5:57
 *
 * @Description
 */
@Repository("bunchBetDao")
public class BunchBetDaoImpl extends ContestDaoSupport implements BunchBetDao {

    @Override
    public BunchBet selectById(long id) {
        String cacheKey = getCacheId(id);

        BunchBet bunchBet = memcacheService.get(cacheKey);
        if (bunchBet == null) {
            bunchBet = sqlSession.selectOne("BunchBetMapper.selectById", id);
            if (bunchBet != null) {
                memcacheService.set(cacheKey, bunchBet);
            }
        }
        return bunchBet;
    }

    @Override
    public BunchBet selectByUser(long bunchId, long userId) {
        Map<String,Object> map = new HashMap<>();
        map.put("bunchId", bunchId);
        map.put("userId", userId);
        return sqlSession.selectOne("BunchBetMapper.selectByUser", map);
    }

    @Override
    public List<BunchBet> selectByUsers(long bunchId, List<Long> userIds) {
        Map<String,Object> map = new HashMap<>();
        map.put("bunchId", bunchId);
        map.put("userIds", userIds);
        return sqlSession.selectList("BunchBetMapper.selectByUsers", map);
    }

    @Override
    public boolean insert(BunchBet bunchBet) {
        int res = sqlSession.insert("BunchBetMapper.insert", bunchBet);
        return res > 0;
    }

    @Override
    public boolean update(BunchBet bunchBet) {
        int res = sqlSession.update("BunchBetMapper.update", bunchBet);
        if (res > 0) {
            deleteObjCache(bunchBet.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateBatch(List<BunchBet> list) {
        int res = sqlSession.update("BunchBetMapper.updateBatch", list);
        if (res > 0) {
            for (BunchBet bunchBet : list) {
                deleteObjCache(bunchBet.getId());
            }
            return true;
        }
        return false;
    }

    @Override
    public List<BunchBet> getList(Long userId, Long startId, int limit) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("startId", startId);
        map.put("limit", limit);
        return sqlSession.selectList("BunchBetMapper.getList", map);
    }

    @Override
    public List<BunchBet> getAwardsList(Long bunchId, Integer status, int skip, int limit) {
        Map<String,Object> map = new HashMap<>();
        map.put("bunchId", bunchId);
        map.put("status", status);
        map.put("skip", skip);
        map.put("limit", limit);
        return sqlSession.selectList("BunchBetMapper.getAwardsList", map);
    }


    @Override
    public List<BunchBet> getListByBunchId(Long bunchId, Integer status, Long startId, int limit) {
        Map<String,Object> map = new HashMap<>();
        map.put("bunchId", bunchId);
        map.put("status", status);
        map.put("startId", startId);
        map.put("limit", limit);
        return sqlSession.selectList("BunchBetMapper.getListByBunchId", map);
    }

    public void deleteObjCache(Long bunchBetId) {
        // 单个对象缓存
        String cacheKey = getCacheId(bunchBetId);
        memcacheService.delete(cacheKey);
    }
}
