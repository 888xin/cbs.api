package com.lifeix.cbs.mall.dao.order.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.mall.dao.MallDaoSupport;
import com.lifeix.cbs.mall.dao.order.MallRecommendDao;
import com.lifeix.cbs.mall.dto.order.MallRecommend;

/**
 * 商城推荐
 * 
 * @author lifeix
 * 
 */
@Repository("mallRecommendDao")
public class MallRecommendDaoImpl extends MallDaoSupport implements MallRecommendDao {

    public final String cacheKey = getCacheId("findMallRecommends");

    @Override
    public boolean insert(MallRecommend mallRecommend) {

	int res = sqlSession.insert("MallRecommendMapper.insert", mallRecommend);
	if (res > 0) {
	    memcacheService.delete(cacheKey);
	    return true;
	}
	return false;
    }

    @Override
    public MallRecommend selectById(Long id) {
	return sqlSession.selectOne("MallRecommendMapper.selectById", id);
    }

    @Override
    public boolean update(MallRecommend mallRecommend) {
	int res = sqlSession.update("MallRecommendMapper.update", mallRecommend);
	if (res > 0) {
	    memcacheService.delete(cacheKey);
	    return true;
	}
	return false;
    }

    @Override
    public boolean delete(Long id) {
	int num = sqlSession.delete("MallRecommendMapper.delete", id);
	if (num > 0) {
	    memcacheService.delete(cacheKey);
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public List<MallRecommend> findMallRecommends() {
	List<MallRecommend> mallRecommends;
	mallRecommends = memcacheService.get(cacheKey);
	if (mallRecommends == null || mallRecommends.size() == 0) {
	    mallRecommends = sqlSession.selectList("MallRecommendMapper.findMallRecommends");
	    if (mallRecommends != null && mallRecommends.size() > 0) {
		memcacheService.set(cacheKey, mallRecommends);
	    }
	}
	return mallRecommends;
    }

}
