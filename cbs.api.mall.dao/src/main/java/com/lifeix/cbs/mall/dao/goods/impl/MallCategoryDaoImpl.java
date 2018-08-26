package com.lifeix.cbs.mall.dao.goods.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.mall.dao.MallDaoSupport;
import com.lifeix.cbs.mall.dao.goods.MallCategoryDao;
import com.lifeix.cbs.mall.dto.goods.MallCategory;

/**
 * 商品分类
 *
 * @author lifeix
 */
@Repository("mallCategoryDao")
public class MallCategoryDaoImpl extends MallDaoSupport implements MallCategoryDao {

    private final String RECOMMEND = this.getClass().getName() + ":recommend";
    private final String ALL = this.getClass().getName() + ":all";

    @Override
    public MallCategory findByName(String name) {
        return sqlSession.selectOne("MallCategoryMapper.findByName", name);
    }

    @Override
    public MallCategory insert(MallCategory category) {
        boolean flag = sqlSession.insert("MallCategoryMapper.insert", category) > 0;
        if (flag) {
            // 删除缓存
            deleteMemcachedData();
        }
        return category;
    }

    @Override
    public boolean update(MallCategory category) {
        boolean flag = sqlSession.update("MallCategoryMapper.update", category) > 0;
        if (flag) {
            // 删除缓存
            deleteMemcachedData();
        }
        return flag;
    }

    @Override
    public boolean delete(long id) {
        boolean flag = sqlSession.delete("MallCategoryMapper.delete", id) > 0;
        if (flag) {
            // 删除缓存
            deleteMemcachedData();
        }
        return flag;
    }

    @Override
    public List<MallCategory> findRecommendCategorys(Integer num) {
        List<MallCategory> ret = memcacheService.get(RECOMMEND);
        //推荐数量改变，缓存也跟着改变
        if (ret == null || ret.size() != num) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("num", num);
            ret = sqlSession.selectList("MallCategoryMapper.findAllCategorys",params);
            if (ret.size() > 0) {
                memcacheService.set(RECOMMEND, ret);
            }
        }
        return ret;
    }

    @Override
    public List<MallCategory> findAllCategorys() {
        List<MallCategory> ret = memcacheService.get(ALL);
        if (ret == null) {
            ret = sqlSession.selectList("MallCategoryMapper.findAllCategorys");
            if (ret.size() > 0) {
                memcacheService.set(ALL, ret);
            }
        }
        return ret;
    }

    @Override
    public List<MallCategory> findAllCategorysInner() {
        return sqlSession.selectList("MallCategoryMapper.findAllCategorysInner");
    }

    @Override
    public boolean incrementNum(Long id) {
        boolean flag = sqlSession.update("MallCategoryMapper.incrementNum", id) > 0;
        if (flag) {
            // 删除缓存
            deleteMemcachedData();
        }
        return flag;
    }

    public void deleteMemcachedData() {

        // 推荐分类缓存
        memcacheService.delete(RECOMMEND);

        // 所有分类缓存
        memcacheService.delete(ALL);

    }
}
