package com.lifeix.cbs.api.dao.mission.impl;

import com.lifeix.cbs.api.dao.ContentGoldDaoSupport;
import com.lifeix.cbs.api.dao.mission.MissionUserDao;
import com.lifeix.cbs.api.dto.mission.MissionUser;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lhx on 16-6-17 下午6:05
 *
 * @Description 用户任务
 */
@Repository("missionUserDao")
public class MissionUserDaoImpl extends ContentGoldDaoSupport implements MissionUserDao {

    @Override
    public MissionUser findById(Long id) {
        return sqlSession.selectOne("MissionUserMapper.findById", id);
    }

    @Override
    public MissionUser findByUserId(Long userId) {
        String cacheKey = getCacheId(userId);
        MissionUser missionUser = memcacheService.get(cacheKey);
        if (missionUser == null) {
            missionUser = sqlSession.selectOne("MissionUserMapper.findByUserId", userId);
            if (missionUser != null) {
                memcacheService.set(cacheKey, missionUser);
            }
        }
        return missionUser;
    }

    @Override
    public boolean insert(MissionUser missionUser) {
        return sqlSession.insert("MissionUserMapper.insert", missionUser) > 0;
    }

    @Override
    public boolean update(MissionUser missionUser) {
        if (missionUser.getUserId() == null){
            return false ;
        }
        boolean flag = sqlSession.update("MissionUserMapper.update", missionUser) > 0;
        if (flag){
            memcacheService.delete(getCacheId(missionUser.getUserId()));
        }
        return flag ;
    }

    @Override
    public List<MissionUser> findByListInner(Integer skip, Integer limit) {
        Map<String, Object> map = new HashMap<>();
        map.put("skip", skip);
        map.put("limit", limit);
        return sqlSession.selectList("MissionUserMapper.findByListInner", map);
    }

}
