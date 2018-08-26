package com.lifeix.cbs.api.action.inner;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.circle.FriendCircleListResponse;
import com.lifeix.cbs.contest.service.cirle.FriendCircleService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Controller
@Path("/inner/friendCircle")
public class FriendCircleInnerResource extends BaseAction {
    @Autowired
    private FriendCircleService friendCircleService;

    /**
     * 获取列表
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("limit") @DefaultValue("40") Integer limit, @QueryParam("start_id") Long startId,
	    @QueryParam("end_id") Long endId, @QueryParam("search_key") String searchKey, @QueryParam("skip") Integer skip)
	    throws JSONException {
	DataResponse<FriendCircleListResponse> ret = new DataResponse<>();
	try {
	    start();
	    String s = new String(searchKey.getBytes("ISO-8859-1"), "utf-8");
	    FriendCircleListResponse data;
	    if (StringUtils.isEmpty(searchKey)) {
		data = friendCircleService.getInnerMyFriendsCircle(null, s, startId, endId, limit, skip);
	    } else {
		data = friendCircleService.getInnerSolrMyFriendsCircle(null, s, startId, endId, limit, skip);
	    }

	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
	} catch (L99IllegalParamsException | L99IllegalDataException e) {
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

    @GET
    @Path("/block")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("ids") String ids) {
	DataResponse<Object> ret = new DataResponse<>();
	try {
	    start();
	    friendCircleService.deleteContentByids(ids);
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

    @POST
    @Path("/publish")
    @Produces(MediaType.APPLICATION_JSON)
    public String post(@FormParam("type") @DefaultValue("1") Integer type, @FormParam("content") String content,
	    @FormParam("images") String images, @FormParam("user_id") Long userId, @FormParam("content_id") Long contentId)
	    throws JSONException {
	DataResponse<Object> ret = new DataResponse<>();
	try {
	    friendCircleService.publishContentInner(userId, content, images, contentId, type);
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

    @GET
    @Path("/list/reason")
    @Produces(MediaType.APPLICATION_JSON)
    public String listReason(@QueryParam("start_id") Long startId, @QueryParam("end_id") Long endId,
	    @QueryParam("type") @DefaultValue("1") Integer type, @QueryParam("limit") @DefaultValue("20") Integer limit,
	    @QueryParam("skip") Integer skip) {
	DataResponse<Object> ret = new DataResponse<>();
	try {
	    start();
	    FriendCircleListResponse data = friendCircleService.getInnerReasonList(startId, endId, limit, skip, type);
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
     * 后台获取未结算战绩
     * 
     * @param startId
     * @param endId
     * @param type
     * @param limit
     * @param skip
     * @return
     */
    @GET
    @Path("/list/nosettle")
    @Produces(MediaType.APPLICATION_JSON)
    public String listReason(@QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit) {
	DataResponse<Object> ret = new DataResponse<>();
	try {
	    start();
	    FriendCircleListResponse data = friendCircleService.getInnerFirCirNoSettleList(startId, limit);
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
}
