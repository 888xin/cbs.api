package com.lifeix.cbs.api.action.content;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.api.util.IPUtil;
import com.lifeix.cbs.api.util.LifeixTiyuApiDubboUtil;
import com.lifeix.cbs.content.service.comment.HessionCommentService;
import com.lifeix.cbs.contest.service.bb.BbContestService;
import com.lifeix.cbs.contest.service.fb.FbContestService;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.tiyu.content.bean.comment.CommentListResponse;
import com.lifeix.tiyu.content.bean.comment.CommentResponse;
import com.lifeix.tiyu.content.bean.main.cbs.CbsContentMainListResponse;
import com.lifeix.tiyu.content.bean.main.cbs.CbsContentResponse;

@Controller
@Path("/content")
public class ContentAction extends BaseAction {

    @Autowired
    private HessionCommentService hessionCommentService;

    @Autowired
    private FbContestService fbContestService;

    @Autowired
    private BbContestService bbContestService;

    /**
     * 新闻列表
     * 
     * @param status
     * @param tabChannel
     *            (1头条|0视频)
     * @param startId
     * @param limit
     * @param format
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String time(@QueryParam("status") @DefaultValue("0") Integer status,
	    @QueryParam("tab_channel") @DefaultValue("0") Integer tabChannel, @QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit, @QueryParam("format") @DefaultValue("json") String format)
	    throws JSONException {
	start();
	DataResponse<CbsContentMainListResponse> ret = new DataResponse<CbsContentMainListResponse>();
	CbsContentMainListResponse response;
	try {
	    switch (tabChannel) {
	    case 1:
		response = LifeixTiyuApiDubboUtil.getInstance().findRecomContents(startId, limit);
		break;
	    default:
		response = LifeixTiyuApiDubboUtil.getInstance().findVideoContents(startId, limit);
	    }
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

    /**
     * 大赢家单篇新闻
     * 
     * @param dev_token
     * @param id
     * @param dashboardId
     * @param refresh
     * @param detail
     * @param comment
     * @return
     */
    @GET
    @Path("/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String view(@HeaderParam("dev_token") String dev_token, @QueryParam("id") Long id,
	    @QueryParam("dashboard_id") Long dashboardId, @QueryParam("refresh") String refresh,
	    @QueryParam("detail") @DefaultValue("true") Boolean detail,
	    @QueryParam("comment") @DefaultValue("false") Boolean comment, @QueryParam("contest_type") Integer contestType,
	    @QueryParam("contest_id") Long contestId) {
	start();
	DataResponse<CbsContentResponse> ret = new DataResponse<CbsContentResponse>();
	try {
	    CbsContentResponse response = LifeixTiyuApiDubboUtil.getInstance().findContentById(id, dashboardId, detail,
		    comment);
	    if (contestType != null && contestId != null) {
		try {
		    if (contestType == ContestType.FOOTBALL) {
			response.setContest(fbContestService.findFbContestsById(contestId, null));
		    } else if (contestType == ContestType.BASKETBALL) {
			response.setContest(bbContestService.findBbContestsById(contestId, null));
		    }
		} catch (Exception e) {
		    LOG.error(String.format("cbs view news find contest failed %s - %s -%s", contestType, contestId,
			    e.getMessage()));
		}

	    }
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (L99NetworkException e) {
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

    /**
     * 评论新闻
     * 
     * @param dashboardId
     * @param content
     * @param image
     * @return
     */
    @POST
    @Path("/comment/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String addComment(@FormParam("dashboard_id") Long dashboardId, @FormParam("content") String content,
	    @FormParam("image") String image, @FormParam("client") String client, @FormParam("lat") Double lat,
	    @FormParam("lng") Double lng) {
	start();
	try {
	    DataResponse<CommentResponse> ret = hessionCommentService.sendComment(getSessionAccount(request), dashboardId,
		    content, image, client, lat, lng, IPUtil.getIpAddr(request));
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 回复评论
     * 
     * @param commentId
     * @param content
     * @param image
     * @return
     */
    @POST
    @Path("/comment/reply")
    @Produces(MediaType.APPLICATION_JSON)
    public String reply(@FormParam("comment_id") Long commentId, @FormParam("content") String content,
	    @FormParam("image") String image, @FormParam("client") String client, @FormParam("lat") Double lat,
	    @FormParam("lng") Double lng) {
	start();
	try {
	    DataResponse<CommentResponse> ret = hessionCommentService.replyComment(getSessionAccount(request), commentId,
		    content, image, client, lat, lng, IPUtil.getIpAddr(request));
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 新闻点赞
     * 
     * @param dashboardId
     * @return
     */
    @POST
    @Path("/comment/like")
    @Produces(MediaType.APPLICATION_JSON)
    public String like(@FormParam("dashboard_id") Long dashboardId, @FormParam("client") String client,
	    @FormParam("lat") Double lat, @FormParam("lng") Double lng) {
	start();
	try {
	    DataResponse<Object> ret = hessionCommentService.LikeContent(getSessionAccount(request), dashboardId, client,
		    lat, lng, IPUtil.getIpAddr(request));
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 评论列表
     * 
     * @param dashboardId
     * @param startId
     * @param limit
     * @return
     */
    @GET
    @Path("/comment/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("dashboard_id") Long dashboardId, @QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) {
	start();
	try {
	    DataResponse<CommentListResponse> ret = hessionCommentService.findDashboardComments(dashboardId, startId, limit);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 用户评论列表
     * 
     * @param startId
     * @param limit
     * @return
     */
    @GET
    @Path("/comment/user")
    @Produces(MediaType.APPLICATION_JSON)
    public String user(@QueryParam("user_id") Long userId, @QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) {
	start();
	try {
	    DataResponse<CommentListResponse> ret = hessionCommentService.findUserComments(userId, startId, limit);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 回复用户的评论列表
     * 
     * @param startId
     * @param limit
     * @return
     */
    @GET
    @Path("/comment/remind")
    @Produces(MediaType.APPLICATION_JSON)
    public String remind(@QueryParam("user_id") Long userId, @QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) {
	start();
	try {
	    DataResponse<CommentListResponse> ret = hessionCommentService.findReplyUserComments(userId, startId, limit);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 评论数量
     * 
     * @param dashboardType
     * @param dashboardData
     * @return
     */
    @GET
    @Path("/comment/num")
    @Produces(MediaType.APPLICATION_JSON)
    public String num(@QueryParam("dashboard_type") Integer dashboardType, @QueryParam("dashboard_data") Long dashboardData) {
	start();
	try {
	    DataResponse<Integer> ret = hessionCommentService.findCommentNum(dashboardType, dashboardData);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

}
