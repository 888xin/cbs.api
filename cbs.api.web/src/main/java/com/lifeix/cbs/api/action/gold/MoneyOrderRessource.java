package com.lifeix.cbs.api.action.gold;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.bean.money.MoneyCardListResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderTNResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.money.MoneyOrderService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.api.util.IPUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.Response;

@Controller
@Path("/money/order")
public class MoneyOrderRessource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(MoneyOrderRessource.class);

    @Autowired
    private MoneyOrderService moneyOrderService;

    @GET
    @Path("/cardlist")
    @Produces(MediaType.APPLICATION_JSON)
    public String cardlist(@QueryParam("deleteFlag") @DefaultValue("0") Integer deleteFlag) throws JSONException {
	start();
	DataResponse<MoneyCardListResponse> ret = new DataResponse<MoneyCardListResponse>();
	try {
	    MoneyCardListResponse rep = moneyOrderService.findMoneyCardList(deleteFlag);
	    ret.setCode(DataResponse.OK);
	    ret.setData(rep);
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
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    @GET
    @Path("/cards")
    @Produces(MediaType.APPLICATION_JSON)
    public String cards() throws JSONException {
	start();
	DataResponse<MoneyCardListResponse> ret = new DataResponse<MoneyCardListResponse>();
	try {
	    MoneyCardListResponse rep = moneyOrderService.findMoneyCards(getSessionAccount(request));
	    ret.setCode(DataResponse.OK);
	    ret.setData(rep);
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
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 创建订单
     * 
     * @param targetId
     * @param money
     * @param paymentId
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/build")
    @Produces(MediaType.APPLICATION_JSON)
    public String build(@FormParam("card_id") Long cardId, @FormParam("target_id") Long targetId,
	    @FormParam("money") Double money, @FormParam("payment_id") Integer paymentId) throws JSONException {
	DataResponse<MoneyOrderResponse> ret = new DataResponse<MoneyOrderResponse>();
	try {
	    start();
	    MoneyOrderResponse rep = moneyOrderService.buildOrder(cardId, getSessionAccount(request), targetId, money,
		    paymentId, IPUtil.getIpAddr(request));
	    ret.setCode(DataResponse.OK);
	    ret.setData(rep);
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
	} finally {
	    end();
	}
    }

    /**
     * 确认订单
     * 
     * @param orderId
     * @param wapFlag
     *            true 网页支付 false 客户端支付
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/confirm")
    @Produces(MediaType.APPLICATION_JSON)
    public String confirm(@FormParam("from") String from, @FormParam("order_id") Long orderId,
	    @FormParam("wap_flag") @DefaultValue("false") Boolean wapFlag) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    Response rep = null;
	    if (wapFlag) {
		rep = moneyOrderService
		        .confirmWapOrder(getSessionAccount(request), orderId, IPUtil.getIpAddr(request), from);
	    } else {
		rep = moneyOrderService.confirmOrder(getSessionAccount(request), orderId, IPUtil.getIpAddr(request));
	    }
	    ret.setCode(DataResponse.OK);
	    ret.setData(rep);
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
	} finally {
	    end();
	}
    }

    /**
     * 检查订单
     * 
     * @param orderId
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/check")
    @Produces(MediaType.APPLICATION_JSON)
    public String check(@QueryParam("order_id") Long orderId) throws JSONException {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    moneyOrderService.checkOrder(getSessionAccount(request), orderId);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 银联订单流水号
     * 
     * @param orderId
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/tn")
    @Produces(MediaType.APPLICATION_JSON)
    public String tn(@QueryParam("order_id") Long orderId) throws JSONException {
	start();
	DataResponse<MoneyOrderTNResponse> ret = new DataResponse<MoneyOrderTNResponse>();
	try {
	    MoneyOrderTNResponse rep = moneyOrderService.orderTN(orderId);
	    ret.setCode(DataResponse.OK);
	    ret.setData(rep);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

}
