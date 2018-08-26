package com.lifeix.cbs.mall.service.goods;

import com.lifeix.cbs.mall.bean.goods.MallGoodsListResponse;
import com.lifeix.cbs.mall.bean.goods.MallGoodsResponse;
import com.lifeix.cbs.mall.bean.goods.MallIndexResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 16-3-21 下午4:18
 *
 * @Description 商品操作
 */
public interface MallGoodsService {

    /**
     * 保存商品
     */
    public MallGoodsResponse insert(Long category_id, String name, String descr, String path, String path_more,
                                    Double price, Double original, Integer sales, Integer stock, Double postage, Integer status,
                                    Integer type, String ex_prop, Integer sort_index) throws L99IllegalDataException, L99IllegalParamsException;

    /**
     * 获取指定类别下的商品
     */
    public MallGoodsListResponse getCategoryGoodsList(Long categoryId, Long startId, Integer limit) throws L99IllegalParamsException;

    /**
     * 删除商品
     * @param id
     * @throws L99IllegalParamsException
     */
    public void delete(Long id) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 获取单个商品信息
     *
     * @param id      商品id
     * @param isInner 是否内部调用，否的话要确定该商品是上架状态的
     * @return
     * @throws L99IllegalParamsException
     */
    public MallGoodsResponse getOneMallGoods(Long id, boolean isInner) throws L99IllegalParamsException, L99IllegalDataException;


    /**
     * 获取多个商品信息（内部管理后台用）
     */
    public MallGoodsListResponse getMallGoodsInner(Long categoryId, Integer status, Long startId, Integer limit);


    /**
     * 更新商品信息
     */
    public void updateMallGoods(Long id, Long category_id, String name, String descr, String path, String path_more,
                                Double price, Double original, Integer sales, Integer stock, Double postage, Integer status,
                                Integer type, String ex_prop, Integer sort_index) throws L99IllegalDataException, L99IllegalParamsException;

    /**
     * 存储商品规格（存储到redis中。用hash存储，key为key，value为value）
     *
     * @param key   规格名称，比如尺寸，颜色等
     * @param value 规格值，比如红色，黄色等
     */
    public void saveSpecifications(String key, String value) throws L99IllegalParamsException;


    /**
     * 获得商品规格keys
     */
    public MallGoodsResponse getSpecificationsKeys();

    /**
     * 获得商品规格（根据key来返回value）
     *
     * @param key
     * @return
     */
    public MallGoodsResponse getSpecifications(String key);

    /**
     * 存储推荐商品Id（存储到redis中。用zset存储，value为id，score为存储时间）
     */
    public void saveRecommendId(String id, boolean top) throws L99IllegalParamsException;

    /**
     * 删除推荐商品Id（存储到redis中。用zset存储，value为id，score为存储时间）
     */
    public void deleteRecommendId(String id) throws L99IllegalParamsException;

    /**
     * 商城主页
     */
    public MallIndexResponse getMallIndex(int recommendCategorySize) throws L99IllegalParamsException;


}
