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
import com.lifeix.cbs.api.bean.user.CbsUserStarListResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.user.CbsUserStarService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/inner/user/star")
public class UserStarInnerResource extends BaseAction {

    @Autowired
    private CbsUserStarService cbsUserStarService;

    /**
     * 推荐用户列表
     * 
     * @param hideFlag
     * @param startId
     * @param limit
     * @return
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("hide_flag") Boolean hideFlag, @QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) {
	DataResponse<CbsUserStarListResponse> ret = new DataResponse<CbsUserStarListResponse>();
	try {
	    start();
	    CbsUserStarListResponse response = cbsUserStarService.findAllStars(hideFlag, startId, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
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
     * 更新推荐用户
     * 
     * @param userId
     * @param factor
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/put")
    @Produces(MediaType.APPLICATION_JSON)
    public String put(@FormParam("user_id") Long userId, @FormParam("factor") Integer factor) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    cbsUserStarService.putUserStar(userId, factor);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
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
     * 显示或隐藏推荐
     * 
     * @param userId
     * @param hideFlag
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/onoff")
    @Produces(MediaType.APPLICATION_JSON)
    public String onoff(@FormParam("user_id") Long userId, @FormParam("hide_flag") Boolean hideFlag) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    cbsUserStarService.onOffUserStar(userId, hideFlag);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
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
}
