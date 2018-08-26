package com.lifeix.cbs.api.action.contest;

import java.util.ArrayList;
import java.util.List;

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
import com.lifeix.cbs.contest.bean.circle.FriendCircleCommListResponse;
import com.lifeix.cbs.contest.service.cirle.FriendCircleCommService;

@Controller
@Path("/friend/circle/comment")
public class FriendCircleCommentResource extends BaseAction {

    @Autowired
    private FriendCircleCommService friendCircleCommService;

    /**
     * 用户发表评论
     * 
     * @param contentId
     *            猜友圈id
     * @param circleUserId
     *            发布猜友圈用户的id
     * @param commUserId
     *            评论者
     * @param content
     *            评论内容
     * @param image
     *            评论图片
     * 
     * @param reUserId
     *            被评论者id
     * @param reContent
     *            被评论的内容
     * @param reImage
     *            被评论的图片
     * @param source
     *            来源
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String add(@FormParam("content_id") Long contentId, @FormParam("circle_userid") Long circleUserId,
	    @FormParam("comm_userid") Long commUserId, @FormParam("content") String content,
	    @FormParam("image") String image, @FormParam("re_userid") Long reUserId,
	    @FormParam("re_content") String reContent, @FormParam("re_image") String reImage,
	    @FormParam("source") String source) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    String ipaddress = IPUtil.getIpAddr(request);
	    ret = friendCircleCommService.addComment(contentId, circleUserId, commUserId, content, image, reUserId,
		    reContent, reImage, source, ipaddress);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
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
     * 删除评论
     * 
     * @param id
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@FormParam("id") Long id) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    ret = friendCircleCommService.delComment(id);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
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
     * 获取单篇猜友圈评论列表
     * 
     * @param contentId
     * @param userId
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/list/detail")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("content_id") Long contentId, @QueryParam("user_id") Long userId,
	    @QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {
	DataResponse<FriendCircleCommListResponse> ret = null;
	try {
	    start();
	    ret = friendCircleCommService.findCommentDetail(contentId, userId, startId, limit);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
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
     * 获取用户未读评论列表(只显示回复和主题)
     * 
     * @param contentId
     * @param userId
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/list/unread")
    @Produces(MediaType.APPLICATION_JSON)
    public String unread(@QueryParam("user_id") Long userId, @QueryParam("page_num") Integer pageNum,
	    @QueryParam("limit") @DefaultValue("29") Integer limit) throws JSONException {
	DataResponse<FriendCircleCommListResponse> ret = null;
	try {
	    start();
	    if (pageNum == null) {
		pageNum = 0;
	    }
	    ret = friendCircleCommService.findUnreadCommentList(userId, pageNum, limit);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
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
     * 获取用户历史评论列表(只显示回复和主题)
     * 
     * @param contentId
     * @param userId
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/list/history")
    @Produces(MediaType.APPLICATION_JSON)
    public String listHistory(@QueryParam("user_id") Long userId, @QueryParam("start_id") Long startId,
	    @QueryParam("end_id") Long endId, @QueryParam("limit") @DefaultValue("29") Integer limit) throws JSONException {
	DataResponse<FriendCircleCommListResponse> ret = null;
	try {
	    start();
	    ret = friendCircleCommService.findHistoryCommentList(userId, startId, endId, limit);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
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
     * 获取单篇猜友圈所有评论数
     * 
     * @param userId
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/single/counts")
    @Produces(MediaType.APPLICATION_JSON)
    public String singleCounts(@QueryParam("content_id") Long contentId, @QueryParam("user_id") Long userId)
	    throws JSONException {
	DataResponse<Object> ret = null;
	try {
	    start();
	    ret = friendCircleCommService.getSingleCommentCounts(contentId, userId);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
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
     * 获取用户未读评论数
     * 
     * @param userId
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/unread/counts")
    @Produces(MediaType.APPLICATION_JSON)
    public String unreadCounts(@QueryParam("user_id") Long userId, @QueryParam("api_version") Integer apiVersion)
	    throws JSONException {
	DataResponse<Object> ret = null;
	try {

	    start();
	    ret = friendCircleCommService.getUnreadCounts(getSessionAccount(request), apiVersion);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
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
     * 获取评论列表
     * 
     * @param userId
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getList(@QueryParam("user_id") Long userId, @QueryParam("end_id") Long endId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) throws JSONException {
	DataResponse<FriendCircleCommListResponse> ret = null;
	try {
	    start();
	    ret = friendCircleCommService.findCommentList(userId, endId, limit);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
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
     * 屏蔽用户评论
     * 
     * @param userId
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/shield")
    @Produces(MediaType.APPLICATION_JSON)
    public String shield(@QueryParam("ids") String ids) throws JSONException {
	DataResponse<Object> ret = null;
	try {
	    start();
	    List<Long> idList = new ArrayList<Long>();
	    String[] idArray = ids.trim().split(",");
	    for (String id : idArray) {
		idList.add(Long.valueOf(id));
	    }
	    ret = friendCircleCommService.updateShield(idList);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
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
