package com.lifeix.cbs.api.action.mall;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.api.util.IPUtil;
import com.lifeix.cbs.mall.bean.order.MallAddressListResponse;
import com.lifeix.cbs.mall.bean.order.MallExpressResponse;
import com.lifeix.cbs.mall.bean.order.MallOrderListResponse;
import com.lifeix.cbs.mall.service.order.MallAddressService;
import com.lifeix.cbs.mall.service.order.MallExpressService;
import com.lifeix.cbs.mall.service.order.MallOrderService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 商城订单接口
 * 
 * @author lifeix
 * 
 */
@Controller
@Path("/mall/order")
public class MallOrderResource extends BaseAction {

    @Autowired
    private MallOrderService mallOrderService;

    @Autowired
    private MallExpressService mallExpressService;

    @Autowired
    private MallAddressService mallAddressService;

    /**
     * 用户订单列表
     * 
     * @return
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("status") @DefaultValue("1") Integer status, @QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) {

	DataResponse<MallOrderListResponse> ret = new DataResponse<MallOrderListResponse>();
	try {
	    MallOrderListResponse resp = mallOrderService.findMallOrderList(getSessionAccount(request), false, false,
		    status, startId, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(resp);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	}
    }

    /**
     * 生成并支付订单
     * 
     * @param goodsId
     * @param goodsNum
     * @param address
     * @param postage
     * @param goodsPro
     * @param userRemark
     * @return
     */
    @Path("/add")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public String addOrder(@FormParam("goods_id") Long goodsId, @FormParam("num") @DefaultValue("1") Integer goodsNum,
	    @FormParam("address") String address, @FormParam("postage") @DefaultValue("0") Double postage,
	    @FormParam("goods_pro") String goodsPro, @FormParam("user_remark") String userRemark) {

	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    mallOrderService.buildMallOrder(goodsId, getSessionAccount(request), address, goodsNum, postage, goodsPro,
		    userRemark, IPUtil.getIpAddr(request));
	    ret.setCode(DataResponse.OK);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	}

    }

    /**
     * 用户确认收货
     * 
     * @param orderId
     * @return
     */
    @Path("/confirm")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public String confirmOrder(@FormParam("order_id") Long orderId) {

	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    mallOrderService.confirmMallOrder(orderId, getSessionAccount(request));
	    ret.setCode(DataResponse.OK);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	}

    }

    /**
     * 查询订单物流信息
     * 
     * @param orderId
     * @return
     */
    @GET
    @Path("/express")
    @Produces(MediaType.APPLICATION_JSON)
    public String express(@QueryParam("order_id") Long orderId) {

	DataResponse<MallExpressResponse> ret = new DataResponse<MallExpressResponse>();
	try {
	    MallExpressResponse resp = mallExpressService.findExpressByOrder(orderId);
	    ret.setCode(DataResponse.OK);
	    ret.setData(resp);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	}
    }

    /**
     * 商品收货地址
     * 
     * @return
     */
    @GET
    @Path("/address")
    @Produces(MediaType.APPLICATION_JSON)
    public String address() {
	DataResponse<MallAddressListResponse> ret = new DataResponse<MallAddressListResponse>();
	try {
	    MallAddressListResponse resp = mallAddressService.findMallAddressList(getSessionAccount(request));
	    ret.setCode(DataResponse.OK);
	    ret.setData(resp);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	}
    }

    /**
     * 商品收货人地址保存
     * 
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/address/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String addAddress(@FormParam("id") Long id, @FormParam("name") String name, @FormParam("address") String address,
	    @FormParam("mobile") String mobile) {

	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    mallAddressService.saveAddress(id, getSessionAccount(request), name, address, mobile);
	    ret.setCode(DataResponse.OK);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	}
    }

    /**
     * 商品收货人地址删除
     * 
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/address/del")
    @Produces(MediaType.APPLICATION_JSON)
    public String delAddress(@FormParam("id") Long id) {

	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    mallAddressService.delAddress(id, getSessionAccount(request));
	    ret.setCode(DataResponse.OK);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	}
    }
}