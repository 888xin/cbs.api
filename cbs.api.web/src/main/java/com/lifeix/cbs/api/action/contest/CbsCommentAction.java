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
import com.lifeix.cbs.api.util.IPUtil;
import com.lifeix.cbs.contest.bean.comment.CbsCommentListResponse;
import com.lifeix.cbs.contest.service.comment.CommentService;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

@Controller
@Path("/comment")
public class CbsCommentAction extends BaseAction {
    @Autowired
    private CommentService commentService;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("start_id") Long startId, @QueryParam("end_id") Long endId,
	    @QueryParam("contest_id") Long contestId,@QueryParam("contest_type") Integer contestType,
	    @QueryParam("limit") @DefaultValue("20") Integer limit,
	    @QueryParam("format") @DefaultValue("json") String format) throws JSONException {
	start();
	DataResponse<CbsCommentListResponse> ret = new DataResponse<CbsCommentListResponse>();
	CbsCommentListResponse response;
	try {
	    response = commentService.findComments(contestId,contestType, startId, endId, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
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

    @POST
    @Path("/send")
    @Produces(MediaType.APPLICATION_JSON)
    public String send(@FormParam("contest_id") Long contestId, @FormParam("contest_type") Integer contestType,
	    @FormParam("client") String client, @FormParam("content") String content, @FormParam("images") String images,
	    @FormParam("cir_id") @DefaultValue("0") Long cirId, @FormParam("accept_user_id") Long acceptUserId)
	    throws L99NetworkException, JSONException {
	start();
	DataResponse<CbsCommentListResponse> ret = new DataResponse<CbsCommentListResponse>();
	CbsCommentListResponse response;
	try {
	    commentService.postComment(contestId,contestType, getSessionAccount(request), acceptUserId,content, images,
		    IPUtil.getIpAddr(request), client, cirId, true);
	    ret.setCode(DataResponse.OK);
	    response = commentService.findComments(contestId, contestType ,null, null, 20);
	    ret.setData(response);
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
	} finally {
	    end();
	}
    }

}
