package com.lifeix.cbs.contest.dao.circle.impl;

import com.lifeix.cbs.api.common.util.ContestConstants.FriendType;
import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.circle.FriendCircleDao;
import com.lifeix.cbs.contest.dto.circle.FriendCircle;
import com.lifeix.framework.memcache.MultiCacheResult;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("friendCircleDao")
public class FriendCircleDaoImpl extends ContestDaoSupport implements FriendCircleDao {

    private final String HAS_REASON = this.getClass().getName() + ":hasReason:";

    @Override
    public List<FriendCircle> getFriendCircleByIds(List<Long> circleIds, String client, Boolean hasContent) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("circleIds", circleIds);
        return sqlSession.selectList("FriendCircleMapper.getFriendCircleByIds", map);
    }

    @Override
    public Integer insert(FriendCircle friendCircle) {
        return sqlSession.insert("FriendCircleMapper.insert", friendCircle);
    }

    @Override
    public List<FriendCircle> getMyCircleByIds(Long userId, String client, Integer friendType, int page, int limit) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        // map.put("client", client);
        if (friendType != null) {
            if (friendType.intValue() == FriendType.CONTEST) {
                map.put("type", 1);
            } else if (friendType.intValue() == FriendType.REASON) {
                map.put("type", 1);
                map.put("hasContent", 1);
            } else if (friendType.intValue() == FriendType.TUCAO) {
                map.put("type", 0);
                map.put("hasContent", 1);
            }
        }
        map.put("startId", (page - 1) * limit);
        map.put("limit", limit + 1);
        return sqlSession.selectList("FriendCircleMapper.getMyCircleByIds", map);

    }

    public List<FriendCircle> getMyYayaCircle(Long userId, Long startId, int limit) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        if (startId != null) {
            map.put("startId", startId);
        }
        map.put("limit", limit);
        return sqlSession.selectList("FriendCircleMapper.getMyYayaCircle", map);
    }

    @Override
    public FriendCircle findById(Long id) {
        return sqlSession.selectOne("FriendCircleMapper.findById", id);
    }

    @Override
    public boolean updateFriendCircle(FriendCircle cont) {
        boolean flag = sqlSession.update("FriendCircleMapper.updateContent", cont) > 0;
        if (flag){
            deleteCache(cont);
        }
        return flag ;
    }

    @Override
    public Integer deleteById(Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        Integer result = sqlSession.update("FriendCircleMapper.deleteById", map);
        if (result > 0){
            FriendCircle friendCircle = findById(id);
            deleteCache(friendCircle);
        }
        return result ;
    }

    @Override
    public List<FriendCircle> getInnerCircles(Long userId, String searchKey, Long startId, Long endId, Integer limit,
                                              Integer skip) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("content", searchKey);
        map.put("startId", startId);
        map.put("endId", endId);
        map.put("limit", limit);
        map.put("skip", skip);
        return sqlSession.selectList("FriendCircleMapper.getInnerCircles", map);
    }

    @Override
    public Integer getInnerCirclesNum(Long userId, String searchKey) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("content", searchKey);
        return sqlSession.selectOne("FriendCircleMapper.getInnerCirclesNum", map);

    }

    @Override
    public List<FriendCircle> findByContestId(Integer contestType, Long contestId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contestType", contestType);
        map.put("contestId", contestId);
        return sqlSession.selectList("FriendCircleMapper.findByContestId", map);
    }

    /**
     * 查询未结算的战绩
     *
     * @param startId
     * @param limit
     * @return
     */
    @Override
    public List<FriendCircle> findNotSettles(Long startId, int limit) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (startId != null) {
            map.put("startId", startId);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -3);
        map.put("createTime", calendar.getTime());
        map.put("limit", limit);
        return sqlSession.selectList("FriendCircleMapper.findNotSettles", map);
    }

    @Override
    public List<FriendCircle> getInnerReasonList(Long startId, Long endId, Integer limit, Integer skip, int type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startId", startId);
        map.put("endId", endId);
        map.put("limit", limit);
        map.put("skip", skip);
        map.put("type", type);
        return sqlSession.selectList("FriendCircleMapper.getInnerReasonList", map);
    }

    @Override
    public List<FriendCircle> getReasonList(long contestId, int contestType, Long startId, int limit) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startId", startId);
        map.put("contestId", contestId);
        map.put("limit", limit);
        map.put("contestType", contestType);
        return sqlSession.selectList("FriendCircleMapper.getReasonList", map);
    }

    @Override
    public Integer getInnerReasonNum(int type) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        return sqlSession.selectOne("FriendCircleMapper.getInnerReasonNum", map);
    }

    @Override
    public boolean isHasReason(long contestId, int contestType) {
        String key = String.format(HAS_REASON + "%d:%d", contestId, contestType);
        Boolean flag = memcacheService.get(key);
        if (flag == null){
            Map<String, Object> map = new HashMap<>();
            map.put("contestId", contestId);
            map.put("contestType", contestType);
            flag = sqlSession.selectOne("FriendCircleMapper.isHasReason", map) != null;
            memcacheService.set(key,flag);
        }
        return flag ;
    }

    @Override
    public List<FriendCircle> findInnerNotSettles(Long startId, int limit) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (startId != null) {
            map.put("startId", startId);
        }
        map.put("limit", limit);
        return sqlSession.selectList("FriendCircleMapper.findInnerNotSettles", map);
    }

    @Override
    public Map<String, String> getReasonUser(Set<String> set) {
        Map<String, String> ret = new HashMap<>();
        if (set.size() == 0) {
            return ret;
        }
        MultiCacheResult cacheResult = memcacheService.getMulti(getMultiCacheId(set));
        Collection<String> noCached = cacheResult.getMissIds();
        Map<String, Object> cacheDatas = cacheResult.getCacheData();
        for (String key : cacheDatas.keySet()) {
            Object obj = cacheDatas.get(key);
            if (obj != null) {
                try {
                    ret.put(revertCacheId(key), (String) obj);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        if (noCached.size() > 0) {
            for (String s : noCached) {
                String[] keys = s.split(":");
                Map<String, Object> map = new HashMap<>();
                map.put("contestId", Long.valueOf(keys[keys.length-1]));
                map.put("contestType", Integer.valueOf(keys[keys.length-2]));
                List<Long> result = sqlSession.selectList("FriendCircleMapper.getReasonUser", map);
                StringBuilder stringBuilder = new StringBuilder();
                for (Long aLong : result) {
                    stringBuilder.append(aLong).append(",");
                }
                if (stringBuilder.length() > 0){
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                String resultStr = stringBuilder.toString();
                ret.put(revertCacheId(s), resultStr);
                memcacheService.set(s, resultStr);
            }
        }
        return ret;
    }

    private void deleteCache(FriendCircle friendCircle) {
        Integer type = friendCircle.getType();
        Boolean hasContent = friendCircle.getHasContent();
        if (type != null && type == 1 && hasContent != null && hasContent){
            Long contestId = friendCircle.getContestId();
            Integer contestType = friendCircle.getContestType();
            if (contestId != null && contestType != null){
                memcacheService.delete(getCacheId(contestType + ":" + contestId ));
                String key = String.format(HAS_REASON + "%d:%d", contestId, contestType);
                memcacheService.delete(key);
            }
        }
    }

}
