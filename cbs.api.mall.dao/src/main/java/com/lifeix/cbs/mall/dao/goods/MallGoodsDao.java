package com.lifeix.cbs.mall.dao.goods;

import com.lifeix.cbs.mall.dto.goods.MallGoods;

import java.util.List;
import java.util.Map;

/**
 * Created by lhx on 16-3-21 上午10:28
 *
 * @Description
 */
public interface MallGoodsDao {

    /**
     * 保存商品
     * @param goods
     * @return
     */
    public MallGoods insert(MallGoods goods);

    /**
     * 根据ID来查找商品
     * @param id
     * @return
     */
    public MallGoods findById(long id);

    /**
     * 查找商品集合
     * @param status
     * @param startId
     * @param limit
     * @return
     */
    //List<MallGoods> findGoods(Integer status, Long startId, Integer limit);

    /**
     * 查找商品集合（内部管理后台用）
     * @param categoryId
     * @param status
     * @param startId
     * @param limit
     * @return
     */
    List<MallGoods> findGoodsInner(Long categoryId, Integer status, Long startId, Integer limit);

    /**
     * 外露接口，只查询某一类型下的上架商品
     */
    public List<MallGoods> findGoodsByCategory(Long categoryId, Long startId, Integer limit);

    /**
     * 更新商品
     * @param mallGoods
     * @return
     */
    public boolean update(MallGoods mallGoods);

    /**
     * 删除商品
     * @param id
     * @return
     */
    public boolean delete(long id);


    /**
     * 根据ids查询
     *
     */
    public Map<Long, MallGoods> findByIds(List<Long> ids);

    boolean isHasByCategory(long categoryId);
}
