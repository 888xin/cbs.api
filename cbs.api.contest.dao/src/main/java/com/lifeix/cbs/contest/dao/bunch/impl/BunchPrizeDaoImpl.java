package com.lifeix.cbs.contest.dao.bunch.impl;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bunch.BunchPrizeDao;
import com.lifeix.cbs.contest.dto.bunch.BunchPrize;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lhx on 16-5-19 下午4:43
 *
 * @Description
 */
@Repository("bunchPrizeDao")
public class BunchPrizeDaoImpl extends ContestDaoSupport implements BunchPrizeDao {

    @Override
    public BunchPrize selectById(long id) {
        return sqlSession.selectOne("BunchPrizeMapper.selectById", id);
    }

    @Override
    public List<BunchPrize> selectByBunchId(long bunchId) {
        String cacheKey = getCacheId(bunchId);

        List<BunchPrize> list = memcacheService.get(cacheKey);
        if (list == null) {
            list = sqlSession.selectList("BunchPrizeMapper.selectByBunchId", bunchId);
            memcacheService.set(cacheKey, list);
        }
//        List<BunchPrize> list = sqlSession.selectList("BunchPrizeMapper.selectByBunchId", bunchId);
        return list;
    }

    @Override
    public boolean insert(BunchPrize bunchPrize) {
        boolean flag = sqlSession.insert("BunchPrizeMapper.insert", bunchPrize) > 0;
        if (flag){
            memcacheService.delete(getCacheId(bunchPrize.getBunchId()));
        }
        return flag;
    }

    @Override
    public boolean insertBatch(List<BunchPrize> bunchPrizes) {
        boolean flag = sqlSession.insert("BunchPrizeMapper.insertBatch", bunchPrizes) > 0;
        if (flag){
            memcacheService.delete(getCacheId(bunchPrizes.get(0).getBunchId()));
        }
        return flag;
    }

    @Override
    public boolean update(BunchPrize bunchPrize) {
        BunchPrize bunchPrize1 = this.selectById(bunchPrize.getId());
        memcacheService.delete(getCacheId(bunchPrize1.getBunchId()));
        return sqlSession.update("BunchPrizeMapper.update", bunchPrize) > 0 ;
    }
}
