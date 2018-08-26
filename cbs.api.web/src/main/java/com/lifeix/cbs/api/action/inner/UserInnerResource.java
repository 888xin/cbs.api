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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.bean.user.UserLoginListResponse;
import com.lifeix.cbs.api.bean.user.UserLoginResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.user.CbsUserLoginService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.settle.UserSettleLogListResponse;
import com.lifeix.cbs.contest.service.settle.SettleLogService;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/user/inner")
public class UserInnerResource extends BaseAction {

    @Autowired
    CbsUserService cbsUserService;

    @Autowired
    SettleLogService settleLogService;

    @Autowired
    private CbsUserLoginService cbsUserLoginService;

    @Autowired
    private NotifyService notifyService;

    /**
     * 后台显示个人战绩
     * 
     * @param format
     * @param userId
     * @param nowPage
     * @param startTime
     * @param endTime
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/settle/log")
    @Produces(MediaType.APPLICATION_JSON)
    public String settleLog(@QueryParam("format") @DefaultValue("json") String format, @QueryParam("long_no") Long longNo,
	    @QueryParam("user_id") Long userId, @QueryParam("type") Integer type, @QueryParam("contest_id") Long contestId,
	    @QueryParam("play_id") Integer playId, @QueryParam("support") Integer support,
	    @QueryParam("now_page") Integer nowPage, @QueryParam("start_time") String startTime,
	    @QueryParam("end_time") String endTime, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {

	DataResponse<UserSettleLogListResponse> ret = new DataResponse<UserSettleLogListResponse>();
	try {
	    start();
	    UserSettleLogListResponse resp = null;
	    if (type != null && contestId != null && playId != null && support != null) {
		resp = settleLogService.getUserSettleLog(userId, type, contestId, playId, support);
	    } else {
		resp = settleLogService.getUserSettleLogs(longNo, nowPage, startTime, endTime, limit);
	    }

	    ret.setCode(DataResponse.OK);
	    ret.setData(resp);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 设置用户累计登陆，版本4.1开始
     * 
     * @param days
     * @param userId
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/set/reward")
    @Produces(MediaType.APPLICATION_JSON)
    public String receive(@FormParam("days") Integer days, @FormParam("user_id") Long userId) throws JSONException {
	start();
	DataResponse<Object> ret = new DataResponse<>();
	try {
	    cbsUserLoginService.setReward(days, userId);
	    ret.setCode(DataResponse.OK);
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
     * 设置累计登录路径，版本4.1开始（内部用）
     * 
     * @param days
     * @param amounts
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/set/path")
    @Produces(MediaType.APPLICATION_JSON)
    public String receive(@FormParam("days") String days, @FormParam("amounts") String amounts) throws JSONException {
	start();
	DataResponse<Object> ret = new DataResponse<>();
	try {
	    cbsUserLoginService.setPath(days, amounts);
	    ret.setCode(DataResponse.OK);
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
     * 获得登陆奖励路径
     * 
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/get/path")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPath() throws JSONException {
	start();
	DataResponse<Object> ret = new DataResponse<>();
	try {
	    UserLoginResponse userLoginResponse = cbsUserLoginService.getPath();
	    ret.setCode(DataResponse.OK);
	    ret.setData(userLoginResponse);
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
     * 获得每月登陆累计记录
     * 
     * @param time
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/get/login/statistic")
    @Produces(MediaType.APPLICATION_JSON)
    public String getStatistic(@QueryParam("time") String time) throws JSONException {
	start();
	DataResponse<Object> ret = new DataResponse<>();
	try {
	    UserLoginListResponse userLoginListResponse = cbsUserLoginService.statistic(time);
	    ret.setCode(DataResponse.OK);
	    ret.setData(userLoginListResponse);
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
     * 把所有的用户登录记录删除
     * 
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/expire")
    @Produces(MediaType.APPLICATION_JSON)
    public String expire() throws JSONException {
	start();
	DataResponse<Object> ret = new DataResponse<>();
	try {
	    cbsUserLoginService.expire();
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

    /**
     * 后台发送消息提醒
     * 
     * @param templetId
     * @param userId
     * @param targetId
     * @param params
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/prompt")
    @Produces(MediaType.APPLICATION_JSON)
    public String systemPrompt(@FormParam("templet_id") Long templetId, @FormParam("user_id") Long userId,
	    @FormParam("target_id") Long targetId, @FormParam("params") String params) throws JSONException {
	start();
	DataResponse<Object> ret = new DataResponse<>();
	try {
	    notifyService.addNotify(templetId, userId, targetId, params, null);
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
