package com.lifeix.cbs.mall.impl.transform;

import java.util.ArrayList;
import java.util.List;

import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.mall.bean.goods.MallGoodsResponse;
import com.lifeix.cbs.mall.dto.goods.MallGoods;

/**
 * Created by lhx on 16-3-21 下午4:51
 * 
 * @Description
 */
public class MallGoodsTransformUtil {

    /**
     * 商品信息 dto to vo
     * 
     * @param mallGoods
     * @param simpleFlag
     *            true 简要信息 false 详情
     * @return
     */
    public static MallGoodsResponse toMallGoodsResponse(MallGoods mallGoods, boolean simpleFlag) {
	MallGoodsResponse mallGoodsResponse = null;
	if (mallGoods != null) {
	    mallGoodsResponse = new MallGoodsResponse();
	    mallGoodsResponse.setId(mallGoods.getId());
	    mallGoodsResponse.setCategory_id(mallGoods.getCategoryId());
	    mallGoodsResponse.setName(mallGoods.getName());
	    mallGoodsResponse.setPath(mallGoods.getPath());
	    mallGoodsResponse.setPrice(mallGoods.getPrice());
	    mallGoodsResponse.setOriginal(mallGoods.getOriginal());
	    mallGoodsResponse.setStatus(mallGoods.getStatus());
	    mallGoodsResponse.setType(mallGoods.getType());
	    mallGoodsResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(mallGoods.getCreateTime()));
	    if (!simpleFlag) { // 商品详情属性
		mallGoodsResponse.setDescr(mallGoods.getDescr());
		mallGoodsResponse.setPath_more(mallGoods.getPathMore());
		mallGoodsResponse.setSales(mallGoods.getSales());
		mallGoodsResponse.setStock(mallGoods.getStock());
		mallGoodsResponse.setPostage(mallGoods.getPostage());
		mallGoodsResponse.setEx_prop(mallGoods.getExProp());
		mallGoodsResponse.setSort_index(mallGoods.getSortIndex());
		mallGoodsResponse.setUpdate_time(CbsTimeUtils.getUtcTimeForDate(mallGoods.getUpdateTime()));
	    }
	}
	return mallGoodsResponse;
    }
    
    public static MallGoodsResponse toMallGoodsResponse(MallGoods mallGoods) {
        MallGoodsResponse mallGoodsResponse = new MallGoodsResponse();
        if (mallGoods != null) {
            mallGoodsResponse = new MallGoodsResponse();
            mallGoodsResponse.setId(mallGoods.getId());
            mallGoodsResponse.setCategory_id(mallGoods.getCategoryId());
            mallGoodsResponse.setName(mallGoods.getName());
            mallGoodsResponse.setDescr(mallGoods.getDescr());
            mallGoodsResponse.setPath(mallGoods.getPath());
            mallGoodsResponse.setPath_more(mallGoods.getPathMore());
            mallGoodsResponse.setPrice(mallGoods.getPrice());
            mallGoodsResponse.setOriginal(mallGoods.getOriginal());
            mallGoodsResponse.setSales(mallGoods.getSales());
            mallGoodsResponse.setStock(mallGoods.getStock());
            mallGoodsResponse.setPostage(mallGoods.getPostage());
            mallGoodsResponse.setStatus(mallGoods.getStatus());
            mallGoodsResponse.setType(mallGoods.getType());
            mallGoodsResponse.setEx_prop(mallGoods.getExProp());
            mallGoodsResponse.setSort_index(mallGoods.getSortIndex());
            mallGoodsResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(mallGoods.getCreateTime()));
            mallGoodsResponse.setUpdate_time(CbsTimeUtils.getUtcTimeForDate(mallGoods.getUpdateTime()));
        }
        return mallGoodsResponse;
    }
}
