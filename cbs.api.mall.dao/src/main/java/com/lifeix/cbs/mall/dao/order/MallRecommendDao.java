/**
 * 
 */
package com.lifeix.cbs.mall.dao.order;

import java.util.List;

import com.lifeix.cbs.mall.dto.order.MallRecommend;

/**
 * 商品导航
 * 
 * @author lifeix
 * 
 */
public interface MallRecommendDao {

    public MallRecommend selectById(Long id);

    public boolean insert(MallRecommend recommend);

    public boolean update(MallRecommend recommend);

    public boolean delete(Long id);

    /**
     * 查询推荐的商品
     * 
     * @return
     */
    public List<MallRecommend> findMallRecommends();

}
