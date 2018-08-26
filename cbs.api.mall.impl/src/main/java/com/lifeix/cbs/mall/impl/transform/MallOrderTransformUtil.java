package com.lifeix.cbs.mall.impl.transform;

import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.mall.bean.goods.MallGoodsResponse;
import com.lifeix.cbs.mall.bean.order.MallAddressResponse;
import com.lifeix.cbs.mall.bean.order.MallExpressResponse;
import com.lifeix.cbs.mall.bean.order.MallOrderResponse;
import com.lifeix.cbs.mall.bean.order.MallRecommendResponse;
import com.lifeix.cbs.mall.dto.order.MallAddress;
import com.lifeix.cbs.mall.dto.order.MallExpress;
import com.lifeix.cbs.mall.dto.order.MallOrder;
import com.lifeix.cbs.mall.dto.order.MallRecommend;

/**
 * 订单 dto to vo
 * 
 * @author peter
 * 
 */
public class MallOrderTransformUtil {
    
    /**
     * 订单物流信息
     * 
     * @param express
     * @return
     */
    public static MallExpressResponse transformMallExpress(MallExpress express) {
	if (express == null) {
	    return null;
	}
	MallExpressResponse resp = new MallExpressResponse();
	resp.setOrderId(express.getOrderId());
	resp.setExpressType(express.getExpressType());
	resp.setExpressNO(express.getExpressNO());
	resp.setState(express.getState());
	resp.setExpressInfo(express.getExpressInfo());
	resp.setUserId(express.getUserId());
	resp.setCreateTime(CbsTimeUtils.getUtcTimeForDate(express.getCreateTime()));
	resp.setUpdateTime(CbsTimeUtils.getUtcTimeForDate(express.getUpdateTime()));
	return resp;
    }

    /**
     * 订单对象
     * 
     * @param order
     * @param goods
     * @return
     */
    public static MallOrderResponse transformMallOrder(MallOrder order, MallGoodsResponse goods) {
	if (order == null) {
	    return null;
	}
	MallOrderResponse resp = new MallOrderResponse();

	resp.setId(order.getId());
	resp.setUser_id(order.getUserId());
	resp.setGoods_id(order.getGoodsId());
	resp.setGoods_price(order.getGoodsPrice());
	resp.setGoods_num(order.getGoodsNum());
	resp.setPostage(order.getPostage());
	resp.setAmount(order.getAmount());
	resp.setOrder_address(order.getOrderAddress());
	resp.setStatus(order.getStatus());
	resp.setGoods_pro(order.getGoodsPro());
	resp.setUser_remark(order.getUserRemark());
	resp.setCancel_reason(order.getCancelReason());
	resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(order.getCreateTime()));
	resp.setShop_time(CbsTimeUtils.getUtcTimeForDate(order.getShopTime()));
	resp.setDone_time(CbsTimeUtils.getUtcTimeForDate(order.getDoneTime()));
	resp.setLog_id(order.getLogId());
	resp.setGoods(goods);
	return resp;
    }

    /**
     * 订单收货地址
     * 
     * @param address
     * @return
     */
    public static MallAddressResponse transformMallAddress(MallAddress address) {
	if (address == null) {
	    return null;
	}
	MallAddressResponse resp = new MallAddressResponse();
	resp.setId(address.getId());
	resp.setUser_id(address.getUserId());
	resp.setName(address.getName());
	resp.setMobile(address.getMobile());
	resp.setAddress(address.getAddress());
	resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(address.getCreateTime()));
	return resp;
    }

    /**
     * 商品导航
     * 
     * @param order
     * @return
     */
    public static MallRecommendResponse transformMallRecommend(MallRecommend mallRecommend) {
	if (mallRecommend == null) {
	    return null;
	}
	MallRecommendResponse resp = new MallRecommendResponse();

	resp.setId(mallRecommend.getId());
	resp.setImage(mallRecommend.getImage());
	resp.setTitle(mallRecommend.getTitle());
	resp.setType(mallRecommend.getType());
	resp.setLink(mallRecommend.getLink());
	resp.setSort(mallRecommend.getSort());
	resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(mallRecommend.getCreateTime()));
	return resp;
    }

}
