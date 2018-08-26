package com.lifeix.cbs.mall.bean.goods;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.mall.bean.order.MallRecommendListResponse;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;

/**
 * Created by lhx on 16-3-25 下午3:26
 *
 * @Description 商城主页面数据
 */
public class MallIndexResponse implements JsonSerializer<MallIndexResponse>, Response {

    private static final long serialVersionUID = -490595953630185579L;

    /**
     * 推荐商品
     */
    private MallGoodsListResponse mallGoodsListResponse ;

    /**
     * 推荐分类
     */
    private MallCategoryListResponse mallCategoryListResponse ;

    /**
     * 导航图
     */
    private MallRecommendListResponse mallRecommendListResponse ;

    public MallGoodsListResponse getMallGoodsListResponse() {
        return mallGoodsListResponse;
    }

    public void setMallGoodsListResponse(MallGoodsListResponse mallGoodsListResponse) {
        this.mallGoodsListResponse = mallGoodsListResponse;
    }

    public MallCategoryListResponse getMallCategoryListResponse() {
        return mallCategoryListResponse;
    }

    public void setMallCategoryListResponse(MallCategoryListResponse mallCategoryListResponse) {
        this.mallCategoryListResponse = mallCategoryListResponse;
    }

    public MallRecommendListResponse getMallRecommendListResponse() {
        return mallRecommendListResponse;
    }

    public void setMallRecommendListResponse(MallRecommendListResponse mallRecommendListResponse) {
        this.mallRecommendListResponse = mallRecommendListResponse;
    }

    @Override
    public JsonElement serialize(MallIndexResponse mallIndexResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
