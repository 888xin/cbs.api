package com.lifeix.cbs.api.action.message;

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
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.message.bean.placard.PlacardTempletListResponse;
import com.lifeix.cbs.message.bean.placard.PlacardTempletResponse;
import com.lifeix.cbs.message.bean.placard.PlacardUnreadNumResponse;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.cbs.message.service.placard.PlacardService;
import com.lifeix.cbs.message.service.placard.PlacardTempletService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 15-10-19 下午4:44
 *
 * @Description
 */
@Controller
@Path("/message/placard")
public class PlacardResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(PlacardResource.class);

    @Autowired
    private PlacardService placardService;

    @Autowired
    private PlacardTempletService placardTempletService;

    @Autowired
    private NotifyService notifyService;

    /**
     * 系统公告
     * 
     * @param format
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("format") @DefaultValue("json") String format,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) throws JSONException {
	DataResponse<PlacardTempletListResponse> ret = new DataResponse<PlacardTempletListResponse>();
	try {
	    start();
	    PlacardTempletListResponse data = placardService.findPlacardDatas(getSessionAccount(request), limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
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
     * 单个公告信息
     * 
     * @param format
     * @param templetId
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String view(@QueryParam("format") @DefaultValue("json") String format, @QueryParam("templet_id") Long templetId)
	    throws JSONException {
	DataResponse<PlacardTempletResponse> ret = new DataResponse<PlacardTempletResponse>();
	try {
	    start();
	    PlacardTempletResponse data = placardTempletService.viewPlacardTemplet(templetId, true);
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
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
     * 设置公告为已读
     * 
     * @param format
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/read")
    @Produces(MediaType.APPLICATION_JSON)
    public String read(@FormParam("format") @DefaultValue("json") String format) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    placardService.readPlacardTemplet(getSessionAccount(request));
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
     * 获取用户个人未读公告
     * 
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/unread/num")
    @Produces(MediaType.APPLICATION_JSON)
    public String unreadNum() throws JSONException {
	DataResponse<PlacardUnreadNumResponse> ret = new DataResponse<PlacardUnreadNumResponse>();
	try {
	    start();
	    Long userId = getSessionAccount(request);
	    int num = placardTempletService.unreadNum(userId);
	    PlacardUnreadNumResponse placardUnreadNumResponse = new PlacardUnreadNumResponse();
	    placardUnreadNumResponse.setUnread_num(num);
	    placardUnreadNumResponse.setUser_id(userId);
	    ret.setCode(DataResponse.OK);
	    ret.setData(placardUnreadNumResponse);
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
     * 设置未读消息为已读
     * 
     * @param type
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/unread/change")
    @Produces(MediaType.APPLICATION_JSON)
    public String unreadChange(@QueryParam("type") Integer type) throws JSONException {
	DataResponse<PlacardUnreadNumResponse> ret = new DataResponse<PlacardUnreadNumResponse>();
	try {
	    start();
	    Long userId = getSessionAccount(request);
	    notifyService.updateUnreadNotify(userId, type);
	    ret.setCode(DataResponse.OK);
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
     * 一键领取龙筹
     * 
     * @param templetId
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/onekey")
    @Produces(MediaType.APPLICATION_JSON)
    public String onekey(@FormParam("templet_id") Long templetId) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    placardTempletService.oneKeyCoupon(templetId, getSessionAccount(request));
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
