package com.lifeix.cbs.api.util;

import java.util.List;

import org.json.JSONException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.l99.dto.dashboard.DashboardType;
import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.content.impl.transform.CommentTransformUtil;
import com.lifeix.cbs.content.service.comment.HessionCommentService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.tiyu.content.bean.comment.CommentListResponse;
import com.lifeix.tiyu.content.bean.comment.CommentResponse;
import com.lifeix.tiyu.content.bean.main.ContentCommentResponse;
import com.lifeix.tiyu.content.bean.main.cbs.CbsContentMainListResponse;
import com.lifeix.tiyu.content.bean.main.cbs.CbsContentResponse;
import com.lifeix.tiyu.content.service.content.cbs.CbsContentViewService;

public class LifeixTiyuApiDubboUtil {

    private LifeixTiyuApiDubboUtil() {
	WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
	cbsContentViewService = (CbsContentViewService) context.getBean("cbsContentViewService");
	hessionCommentService = (HessionCommentService) context.getBean("hessionCommentService");
    }

    private static class InstanceHolder {
	/**
	 * Add a private constructor to hide the implicit public one.
	 */
	private InstanceHolder() {
	}

	private static final LifeixTiyuApiDubboUtil INSTANCE = new LifeixTiyuApiDubboUtil();
    }

    public static LifeixTiyuApiDubboUtil getInstance() {
	return InstanceHolder.INSTANCE;
    }

    private CbsContentViewService cbsContentViewService;

    private HessionCommentService hessionCommentService;

    /**
     * 头条模板
     * 
     * @param startId
     * @param limit
     * @return
     */
    public CbsContentMainListResponse findRecomContents(Long startId, Integer limit) {
	CbsContentMainListResponse resp = cbsContentViewService.findRecomContents(startId, limit, null, null);
	return resp;
    }

    /**
     * 视频模板
     * 
     * @param startId
     * @param limit
     * @return
     */
    public CbsContentMainListResponse findVideoContents(Long startId, int limit) {
	CbsContentMainListResponse resp = cbsContentViewService.findVideoContents(startId, limit);
	return resp;
    }

    /**
     * 单篇资讯
     * 
     * @param id
     * @param dashboardId
     * @param detail
     * @param commentFlag
     * @return
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99NetworkException
     */
    public CbsContentResponse findContentById(Long id, Long dashboardId, boolean detail, boolean commentFlag)
	    throws L99IllegalParamsException, L99NetworkException, JSONException {
	CbsContentResponse resp = cbsContentViewService.findContentById(id, dashboardId, detail, commentFlag);
	// 修改热门评论头像
	List<ContentCommentResponse> hotComments = resp.getHot_comments();
	if (hotComments != null && hotComments.size() > 0) {
	    for (int i = 0; i < hotComments.size(); i++) {
		hotComments.get(i).setUser_avatar(
		        hotComments.get(i).getUser_avatar()
		                .replace(CommentTransformUtil.DAFAULT_OLD, CommentTransformUtil.DAFAULT_AVATAR));
	    }
	}

	return resp;
    }

    /**
     * 评论列表
     * 
     * @param dashboardId
     * @param startId
     * @param limit
     * @return
     */
    public DataResponse<CommentListResponse> getCommentList(Long dashboardId, Long startId, int limit) {
	return hessionCommentService.findDashboardComments(dashboardId, startId, limit);
    }

    /**
     * 评论新闻
     * 
     * @param userId
     * @param dashboardId
     * @param content
     * @param image
     * @param client
     * @param lat
     * @param lng
     * @param ipAddress
     * @return
     */
    public DataResponse<CommentResponse> sendComment(Long userId, Long dashboardId, String content, String image,
	    String client, Double lat, Double lng, String ipAddress) {
	return hessionCommentService.sendComment(userId, dashboardId, content, image, client, lat, lng, ipAddress);
    }

    /**
     * 回复评论
     * 
     * @param userId
     * @param commentId
     * @param content
     * @param image
     * @param client
     * @param lat
     * @param lng
     * @param ipAddress
     * @return
     */
    public DataResponse<CommentResponse> replyComment(Long userId, Long commentId, String content, String image,
	    String client, Double lat, Double lng, String ipAddress) {
	return hessionCommentService.replyComment(userId, commentId, content, image, client, lat, lng, ipAddress);
    }

    /**
     * 查询类容评论数量
     * 
     * @param contentId
     * @return
     */
    public DataResponse<Integer> findCommentNum(Long contentId) {
	return hessionCommentService.findCommentNum(DashboardType.TEXT, contentId);
    }

    /**
     * 删除新闻
     * 
     * @param contentId
     * @return
     */
    public DataResponse<Boolean> dropContent(Long contentId) {
	DataResponse<Boolean> ret = new DataResponse<Boolean>();
	try {
	    cbsContentViewService.dropContent(contentId);
	    ret.setData(true);
	} catch (L99IllegalParamsException | L99IllegalDataException e) {
	    e.printStackTrace();
	    ret.setData(false);
	}
	return ret;
    }


}
