package com.lifeix.cbs.mall.service.goods;

import com.lifeix.cbs.mall.bean.goods.MallCategoryListResponse;
import com.lifeix.cbs.mall.bean.goods.MallCategoryResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 16-3-22 上午9:22
 *
 * @Description 商品类别
 */
public interface MallCategoryService {

    /**
     * 保存类别
     */
    MallCategoryResponse insert(String name, String descr, String path, Integer num,Integer sort_index) throws L99IllegalDataException;

    /**
     * 更新
     */
    void update(Long id, String name, String descr, String path, Integer num,Integer sortIndex,Integer status) throws L99IllegalDataException, L99IllegalParamsException;


    /**
     * 获取推荐商品类别
     * @param num 推荐数目
     * @return
     */
    MallCategoryListResponse getRecommendCategoryList(Integer num);

    /**
     * 获取所有类别
     */
    MallCategoryListResponse getAllCategoryList();

    /**
     * 获取所有类别（内部）
     */
    MallCategoryListResponse getAllCategoryListInner();

    /**
     * 获取单个
     */
    MallCategoryResponse view(String id) throws L99IllegalParamsException;

    /**
     * 删除单个
     */
    void delete(Long id) throws L99IllegalParamsException, L99IllegalDataException;


    /**
     * 对数量加一
     */
    boolean incrementNum(Long id) throws L99IllegalParamsException;
}
