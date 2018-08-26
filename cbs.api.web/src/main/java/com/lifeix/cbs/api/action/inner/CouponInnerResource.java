package com.lifeix.cbs.api.action.inner;

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
import com.lifeix.cbs.api.bean.coupon.CouponListResponse;
import com.lifeix.cbs.api.bean.coupon.CouponUserListResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.coupon.CouponService;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

/**
 * Created by lhx on 16-2-25 下午3:22
 * 
 * @Description
 */
@Controller
@Path("/inner/coupon")
public class CouponInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(CouponInnerResource.class);

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponUserService cbsUserService;

    @Autowired
    private CouponUserService couponUserService;

    /**
     * 新增或更新龙筹券
     */
    @POST
    @Path("/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String edit(@FormParam("id") Long id, @FormParam("name") String name, @FormParam("hour") Integer hour,
	    @FormParam("price") Integer price, @FormParam("range_key") Integer rangeKey,
	    @FormParam("range_value") String rangeValue, @FormParam("sum") Integer sum, @FormParam("num") Integer num,
	    @FormParam("proportion") Double proportion, @FormParam("descr") String descr, @FormParam("valid") Boolean valid)
	    throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    couponService.saveOrUpdate(id, hour, name, price, rangeKey, rangeValue, sum, num, proportion, descr, valid);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 获取龙筹券列表
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("type") Integer type, @QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("30") Integer limit, @QueryParam("valid") @DefaultValue("true") Boolean valid)
	    throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    CouponListResponse data = couponService.selectCoupons(type, valid, startId, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 发送给用户
     */
    @POST
    @Path("/send")
    @Produces(MediaType.APPLICATION_JSON)
    public String send(@FormParam("id") Long couponId, @FormParam("user_ids") String userIds, @FormParam("num") Integer num)
	    throws JSONException {
	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	try {
	    start();
	    CustomResponse data = cbsUserService.sendCouponToUser(couponId, userIds.split(","), num);
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 更改龙筹券状态（激活或失效）
     */
    @POST
    @Path("/valid")
    @Produces(MediaType.APPLICATION_JSON)
    public String edit(@FormParam("id") Long id, @FormParam("valid") Boolean valid) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    couponService.toggerValid(id, valid);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 后台查询用户龙筹券列表
     * 
     * @param active
     *            true 可用 false 不可用
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/user/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("active") @DefaultValue("true") Boolean active, @QueryParam("user_id") Long user_id,
	    @QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {
	start();
	DataResponse<CouponUserListResponse> ret = new DataResponse<CouponUserListResponse>();
	try {
	    ret.setData(couponUserService.findUserCouponList(user_id, active, startId, limit));
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
