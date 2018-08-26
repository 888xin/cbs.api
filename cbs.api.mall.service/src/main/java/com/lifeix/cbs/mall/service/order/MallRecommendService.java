package com.lifeix.cbs.mall.service.order;

import java.util.Date;

import org.json.JSONException;

import com.lifeix.cbs.mall.bean.order.MallRecommendListResponse;
import com.lifeix.cbs.mall.bean.order.MallRecommendResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface MallRecommendService {

    /**
     * 获取商品导航
     * 
     * @param id
     * @return
     * @throws L99IllegalParamsException
     */
    public MallRecommendResponse findMallRecommend(Long id) throws L99IllegalParamsException;
    
    /**
     * 获取商品导航列表
     * 
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    public MallRecommendListResponse findMallRecommends()
	    throws L99IllegalParamsException;

    /**
     * 保存订单推荐
     * 
     * @param id
     * @param image
     * @param title
     * @param type
     * @param link
     * @param sort
     * @param createTime
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     * @throws JSONException
     */
    public MallRecommendResponse insert(String image, String title, Integer type, String link, Integer sort) throws L99IllegalDataException, L99IllegalParamsException, JSONException;

    /**
     * 根据id删除订单推荐
     * 
     * @param id
     * @return
     * @throws L99IllegalParamsException
     */
    public void deleteMallRecommendByid(Long id) throws L99IllegalParamsException, L99IllegalDataException;

    
    /**
     * 更新订单推荐
     */
    public void updateRecommend(Long id,String image,String title,Integer type,String link,Integer sort) throws L99IllegalDataException, L99IllegalParamsException;
}
