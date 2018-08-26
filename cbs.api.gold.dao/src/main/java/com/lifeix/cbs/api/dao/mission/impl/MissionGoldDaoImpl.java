package com.lifeix.cbs.api.dao.mission.impl;

import com.lifeix.cbs.api.dao.ContentGoldDaoSupport;
import com.lifeix.cbs.api.dao.mission.MissionGoldDao;
import com.lifeix.cbs.api.dto.mission.MissionGold;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lhx on 16-6-17 下午6:05
 *
 * @Description 积分兑换龙筹
 */
@Repository("missionGoldDao")
public class MissionGoldDaoImpl extends ContentGoldDaoSupport implements MissionGoldDao {

    private final String ALL = this.getClass().getName() + ":all";

    @Override
    public MissionGold insert(MissionGold missionGold) {
        sqlSession.insert("MissionGoldMapper.insert", missionGold);
        if (missionGold.getId() > 0){
            deleteMemcacheData(null);
        }
        return missionGold ;
    }

    @Override
    public boolean update(MissionGold missionGold) {
        boolean flag = sqlSession.delete("MissionGoldMapper.update", missionGold) > 0;
        if (flag){
            deleteMemcacheData(missionGold.getId());
        }
        return flag ;
    }

    @Override
    public MissionGold findById(Long id) {
        String cacheKey = getCacheId(id);
        MissionGold missionGold = memcacheService.get(cacheKey);
        if (missionGold == null) {
            missionGold = sqlSession.selectOne("MissionGoldMapper.findById", id);
            if (missionGold != null) {
                memcacheService.set(cacheKey, missionGold);
            }
        }
        return missionGold;
    }

    @Override
    public List<MissionGold> getAll() {
        List<MissionGold> list = memcacheService.get(ALL);
        if (list == null) {
            list = sqlSession.selectList("MissionGoldMapper.getAll");
            memcacheService.set(ALL, list);
        }
        return list;
    }

    @Override
    public boolean delete(Long id) {
        boolean flag = sqlSession.delete("MissionGoldMapper.delete", id) > 0;
        if (flag){
            deleteMemcacheData(id);
        }
        return flag ;
    }

    private void deleteMemcacheData(Long id){
        if (id != null){
            memcacheService.delete(getCacheId(id));
        }
        memcacheService.delete(ALL);
    }
}
