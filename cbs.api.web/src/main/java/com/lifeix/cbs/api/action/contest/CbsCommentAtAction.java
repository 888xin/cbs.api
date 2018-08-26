package com.lifeix.cbs.api.action.contest;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
import com.lifeix.cbs.contest.service.comment.CommentAtService;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

@Controller
@Path("/comment/at")
public class CbsCommentAtAction extends BaseAction {
    @Autowired
    private CommentAtService commentAtService;

    @POST
    @Path("/send")
    @Produces(MediaType.APPLICATION_JSON)
    public String send(@FormParam("contest_type") Integer contestType,@FormParam("contest_id") Long contestId, @FormParam("send_user_id") Long sendUserId,
	    @FormParam("accept_user_id") Long acceptUserId, @FormParam("client") String client,
	    @FormParam("content") String content, @FormParam("images") String images) throws L99NetworkException,
	    JSONException {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    commentAtService.postCommentAt(contestType,contestId, sendUserId, acceptUserId, content, IPUtil.getIpAddr(request), client);
	    ret.setCode(DataResponse.OK);
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
