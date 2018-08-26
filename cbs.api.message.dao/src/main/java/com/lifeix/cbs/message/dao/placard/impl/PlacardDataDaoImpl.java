package com.lifeix.cbs.message.dao.placard.impl;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.message.dao.MessageDaoSupport;
import com.lifeix.cbs.message.dao.placard.PlacardDataDao;
import com.lifeix.cbs.message.dto.PlacardData;

/**
 * Created by lhx on 15-10-19 上午11:02
 *
 * @Description 系统通知缓存用户最后一次访问记录
 */
@Repository("placardDataDao")
public class PlacardDataDaoImpl  extends MessageDaoSupport implements PlacardDataDao {

    @Override
    public boolean insert(PlacardData placardData) {
        return sqlSession.insert("PlacardDataMapper.insert",placardData) > 0;
    }

    @Override
    public PlacardData findById(Long userId) {
        String cacheKey = getCacheId(userId);

        PlacardData placardData = memcacheService.get(cacheKey);

        if (placardData == null) {
            placardData = sqlSession.selectOne("PlacardDataMapper.findById", userId);
            if (placardData != null) {
                memcacheService.set(cacheKey, placardData);
            }
        }
        return placardData;
    }

    @Override
    public boolean update(PlacardData placardData) {
        String cacheKey = getCacheId(placardData.getUserId());
        memcacheService.delete(cacheKey);
        return sqlSession.update("PlacardDataMapper.update", placardData) > 0;
    }

    @Override
    public boolean delete(Long userId) {
        String cacheKey = getCacheId(userId);
        memcacheService.delete(cacheKey);
        return sqlSession.delete("PlacardDataMapper.delete", userId) > 0;
    }

}
