package com.lifeix.cbs.contest.dao.bunch.impl;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bunch.BunchContestDao;
import com.lifeix.cbs.contest.dto.bunch.BunchContest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lhx on 16-5-16 下午5:44
 *
 * @Description
 */
@Repository("bunchContestDao")
public class BunchContestDaoImpl extends ContestDaoSupport implements BunchContestDao {

    /**
     * 正在进行的列表key
     */
    private final String EX_KEY = this.getClass().getName() + ":ex";

    @Override
    public BunchContest selectById(long id) {
        String cacheKey = getCacheId(id);
        BunchContest contest = memcacheService.get(cacheKey);
        if (contest == null) {
            contest = sqlSession.selectOne("BunchContestMapper.selectById", id);
            if (contest != null) {
                memcacheService.set(cacheKey, contest);
            }
        }
        return contest;
    }

    @Override
    public Long insert(BunchContest bunchContest) {
        sqlSession.insert("BunchContestMapper.insert", bunchContest) ;
        deleteObjCache(EX_KEY);
        return bunchContest.getId();
    }

    @Override
    public boolean update(BunchContest bunchContest) {
        int res = sqlSession.update("BunchContestMapper.update", bunchContest);
        if (res > 0) {
            deleteObjCache(getCacheId(bunchContest.getId()), EX_KEY);
            return true;
        }
        return false;
    }

    @Override
    public List<BunchContest> getList() {
        List<BunchContest> list = memcacheService.get(EX_KEY);
        if (list == null) {
            list = sqlSession.selectList("BunchContestMapper.getList");
            memcacheService.set(EX_KEY, list);
        }
        return list;
    }

    @Override
    public List<BunchContest> getOldList(Long startId, Integer limit) {
        Map<String, Object> map = new HashMap<>();
        map.put("startId", startId);
        map.put("limit", limit);
        return sqlSession.selectList("BunchContestMapper.getOldList", map);
    }

    @Override
    public List<BunchContest> getSettleList(int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        return sqlSession.selectList("BunchContestMapper.getSettleList", map);
    }

    @Override
    public List<BunchContest> getInnerList(Integer status, Long startId, Integer limit) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("startId", startId);
        map.put("limit", limit);
        return sqlSession.selectList("BunchContestMapper.getInnerList", map);
    }

    public void deleteObjCache(String ... keys) {
        for (String key : keys) {
            memcacheService.delete(key);
        }
    }
}
