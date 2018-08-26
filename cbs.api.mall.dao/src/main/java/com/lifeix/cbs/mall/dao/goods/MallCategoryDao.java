package com.lifeix.cbs.mall.dao.goods;

import java.util.List;

import com.lifeix.cbs.mall.dto.goods.MallCategory;

/**
 * 商品分类
 *
 * @author lifeix
 *
 */
public interface MallCategoryDao {

    /**
     * 根据name来查询
     * @param name
     */
    MallCategory findByName(String name);

    /**
     * 保存商品分类
     * @param category
     */
    MallCategory insert(MallCategory category);

    /**
     * 更新
     * @param category
     */
    boolean update(MallCategory category);

    /**
     * 更新
     */
    boolean delete(long id);

    /**
     * 推荐分类
     * @param num 推荐分类的个数
     */
    List<MallCategory> findRecommendCategorys(Integer num);

    /**
     * 所有分类
     */
    List<MallCategory> findAllCategorys();

    /**
     * 所有分类（内部接口）
     */
    List<MallCategory> findAllCategorysInner();

    /**
     * 对数量加一
     * @param id
     * @return
     */
    boolean incrementNum(Long id);

}