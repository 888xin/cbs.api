package com.lifeix.cbs.api.action.gold.coupon;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.bean.coupon.CouponUserListResponse;
import com.lifeix.cbs.api.bean.coupon.CouponUserResponse;
import com.lifeix.cbs.api.bean.gold.GoldLogListResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/coupon/user")
public class CouponUserResource extends BaseAction {

    @Autowired
    private CouponUserService couponUserService;

    /**
     * 领取优惠卷
     * 
     * @param couponId
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/receive")
    @Produces(MediaType.APPLICATION_JSON)
    public String receive(@QueryParam("coupon_id") Long couponId) throws JSONException {
	start();
	DataResponse<GoldLogListResponse> ret = new DataResponse<GoldLogListResponse>();
	try {
	    couponUserService.grantCoupon(couponId, getSessionAccount(request), false);
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
     * add by lhx on 16-03-15 活动龙筹卷是否领过以及剩余的数量
     */
    @GET
    @Path("/check")
    @Produces(MediaType.APPLICATION_JSON)
    public String check(@QueryParam("coupon_id") Long couponId) throws JSONException {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    CouponUserResponse couponUserResponse = couponUserService.checkCoupon(couponId, getSessionAccount(request));
	    ret.setCode(DataResponse.OK);
	    ret.setData(couponUserResponse);
	} catch (L99IllegalParamsException e) {
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
     * 用户龙筹券列表
     * 
     * @param isUsed
     *            true 可用 false 不可用
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("active") @DefaultValue("true") Boolean active, @QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) throws JSONException {
	start();
	DataResponse<CouponUserListResponse> ret = new DataResponse<CouponUserListResponse>();
	try {
	    ret.setData(couponUserService.findUserCouponList(getSessionAccount(request), active, startId, limit));
	    ret.setCode(DataResponse.OK);
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
