package com.lifeix.cbs.api.action.achieve;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.achieve.bean.achieve.AchieveResponse;
import com.lifeix.cbs.achieve.bean.achieve.UserAchieveResponse;
import com.lifeix.cbs.achieve.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.achieve.common.util.InternationalResources;
import com.lifeix.cbs.achieve.service.AchieveService;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/achieve")
public class AchieveResource extends BaseAction {

    @Autowired
    protected AchieveService achieveService;

    /**
     * 查看用户获得的成就列表
     * 
     * @param startLogId
     * @param endLogId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/viewother")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewOtherAchieveLogs(@QueryParam("user_id") Long userId) throws JSONException {
	DataResponse<UserAchieveResponse> ret = new DataResponse<UserAchieveResponse>();
	try {
	    start();
	    UserAchieveResponse response = achieveService.viewAchieveLogs(userId);
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 查看用户获得的成就列表
     * 
     * @param startLogId
     * @param endLogId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewAchieveLogs() throws JSONException {
	DataResponse<UserAchieveResponse> ret = new DataResponse<UserAchieveResponse>();
	try {
	    start();
	    UserAchieveResponse response = achieveService.viewAchieveLogs(getSessionAccount(request));
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 查看用户获得的成就列表
     *
     * @param startLogId
     * @param endLogId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/view/one")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewOneAchieve(@QueryParam("id") Long id) throws JSONException {
	DataResponse<AchieveResponse> ret = new DataResponse<AchieveResponse>();
	try {
	    start();
	    AchieveResponse response = achieveService.viewOneAchieve(id);
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

}
