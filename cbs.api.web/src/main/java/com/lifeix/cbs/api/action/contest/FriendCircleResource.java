package com.lifeix.cbs.api.action.contest;

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
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.circle.FriendCircleListResponse;
import com.lifeix.cbs.contest.bean.circle.FriendCircleResponse;
import com.lifeix.cbs.contest.service.cirle.FriendCircleService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/friend/circle")
public class FriendCircleResource extends BaseAction {

    @Autowired
    FriendCircleService friendCircleService;

    /**
     * 猜友圈
     * 
     * @param userId
     * @param startId
     * @param limit
     * @param client
     * @param hasContent
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/time")
    @Produces(MediaType.APPLICATION_JSON)
    public String time(@QueryParam("user_id") Long userId, @QueryParam("start_id") Integer startId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit, @QueryParam("client") String client,
	    @QueryParam("friend_type") Integer friendType) throws JSONException {
	DataResponse<FriendCircleListResponse> ret = new DataResponse<FriendCircleListResponse>();
	try {
	    start();
	    FriendCircleListResponse rep = friendCircleService.getFriendsCircle(userId, null, friendType, startId, limit);
	    ret.setData(rep);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
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
     * 获取跟投信息
     * 
     * @param
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/follow")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFollowinfo(@QueryParam("friend_circle_id") Long friendCircleId) throws JSONException {
	DataResponse<FriendCircleResponse> ret = new DataResponse<FriendCircleResponse>();
	try {
	    start();
	    FriendCircleResponse rep = friendCircleService.getFollowInfo(getSessionAccount(request), friendCircleId);
	    ret.setData(rep);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
	    return DataResponseFormat.response(ret);
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
     * 个人下单记录
     * 
     * @param time
     *            (缓存时间超过一天的比赛列表)
     * @param format
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/my")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMyFriendsCircle(@QueryParam("user_id") Long userId,
	    @QueryParam("friend_circle_id") Long friendCircleId, @QueryParam("start_id") Integer startId,
	    @QueryParam("limit") @DefaultValue("19") Integer limit, @QueryParam("client") String client,
	    @QueryParam("friend_type") Integer friendType) throws JSONException {
	DataResponse<FriendCircleListResponse> ret = new DataResponse<FriendCircleListResponse>();
	try {
	    start();
	    FriendCircleListResponse rep = friendCircleService.getMyFriendsCircle(userId, friendCircleId, null, friendType,
		    startId, limit);
	    ret.setData(rep);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
	    return DataResponseFormat.response(ret);
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
     * shanchu dangpian
     * 
     * @param friendCircleId
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String delMyFriendsCircle(@FormParam("friend_circle_id") Long friendCircleId) throws JSONException {
	DataResponse<FriendCircleListResponse> ret = new DataResponse<FriendCircleListResponse>();
	try {
	    start();
	    friendCircleService.deleteContentByid(friendCircleId);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
	    return DataResponseFormat.response(ret);
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
     * 发表内容
     */
    @POST
    @Path("/publish")
    @Produces(MediaType.APPLICATION_JSON)
    public String post(@FormParam("type") @DefaultValue("0") Integer type, @FormParam("content") String content,
	    @FormParam("images") String images, @FormParam("client") String client, @FormParam("price_id") Integer priceId,
	    @FormParam("content_id") Long contentId) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    friendCircleService.publishContent(getSessionAccount(request), content, images, contentId, priceId, client);
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
	    ret.setMsg(InternationalResources.getInstance().locale("error.basic.serviceerror", request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 获得单个
     */
    @GET
    @Path("/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String view(@QueryParam("id") Long id) throws JSONException {
	DataResponse<Object> ret = new DataResponse<>();
	try {
	    start();
	    FriendCircleResponse data = friendCircleService.getSingleContentResponse(id);
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
     * 获取下单理由
     */
    @GET
    @Path("/reason")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFriendsCircleReason(@QueryParam("contest_id") Long contestId, @QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit, @QueryParam("contest_type") Integer contestType)
	    throws JSONException {
	DataResponse<Object> ret = new DataResponse<>();
	try {
	    start();
	    FriendCircleListResponse rep = friendCircleService.getReasonsList(contestId, contestType, startId, limit);
	    ret.setData(rep);
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

}
