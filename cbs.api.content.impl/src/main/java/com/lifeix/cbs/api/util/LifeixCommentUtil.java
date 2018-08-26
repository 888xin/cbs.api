package com.lifeix.cbs.api.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.l99.common.basetype.MediaType;
import com.l99.dto.dashboard.DashboardType;
import com.l99.exception.service.L99ContentRejectedException;
import com.l99.exception.service.L99IllegalDataException;
import com.l99.exception.service.L99IllegalOperateException;
import com.l99.exception.service.L99IllegalParamsException;
import com.l99.hessian.comment.LXCommentIF;
import com.l99.pojo.comment.L99Comment;

/**
 * 评论降级包装
 * 
 * @author peter
 * 
 */
public class LifeixCommentUtil {

    private LXCommentIF commentClient;

    static class SingletonHolder {
	/**
	 * Add a private constructor to hide the implicit public one.
	 */
	private SingletonHolder() {
	}

	private static final LifeixCommentUtil INSTANCE = new LifeixCommentUtil();
    }

    public static LifeixCommentUtil getInstance() {
	return SingletonHolder.INSTANCE;
    }

    private LifeixCommentUtil() {
	WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
	commentClient = (LXCommentIF) context.getBean("commentClient");
    }

    /**
     * 评论降级开关
     */
    private boolean commentSwitch = true;

    /**
     * 评论内容
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
     * @throws L99IllegalDataException
     * @throws L99ContentRejectedException
     * @throws L99IllegalOperateException
     * @throws L99IllegalParamsException
     */
    public L99Comment replyByBoardAndGetComment(Long userId, Long dashboardId, String content, String image, String client,
	    Double lat, Double lng, String ipAddress) throws L99IllegalDataException, L99ContentRejectedException,
	    L99IllegalOperateException, L99IllegalParamsException {
	if (commentSwitch) {
	    MediaType mediaType = null;
	    if (image != null) {
		mediaType = MediaType.image_photo;
	    }
	    return commentClient.replyByBoardAndGetComment(userId, dashboardId, content, 0, ipAddress, mediaType, image,
		    false, null, client, lat, lng);
	} else {
	    return null;
	}
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
     * @throws L99IllegalOperateException
     */
    public L99Comment replyComment(Long userId, Long commentId, String content, String image, String client, Double lat,
	    Double lng, String ipAddress) throws L99IllegalOperateException {
	if (commentSwitch) {
	    String mediaType = null;
	    if (image != null) {
		mediaType = MediaType.image_photo.name();
	    }
	    return commentClient.replyComment(userId, commentId, content, ipAddress, mediaType, null, false, true, null,
		    client, lat, lng);
	} else {
	    return null;
	}
    }

    /**
     * 内容点赞
     * 
     * @param userId
     * @param dashboardId
     * @param client
     * @param lat
     * @param lng
     * @param ipAddress
     * @return
     */
    public L99Comment likeByBoard(Long userId, Long dashboardId, String client, Double lat, Double lng, String ipAddress) {
	if (commentSwitch) {
	    return commentClient.likeByBoard(userId, dashboardId, ipAddress, false, client, lat, lng);
	} else {
	    return null;
	}
    }

    /**
     * 内容评论列表
     * 
     * @param dashboardId
     * @param startId
     * @param limit
     * @return
     */
    public List<L99Comment> findCommentByBoard(Long dashboardId, Long startId, int limit) {

	if (commentSwitch) {
	    return commentClient.findCommentByBoard(dashboardId, startId, limit);
	} else {
	    return new ArrayList<L99Comment>();
	}
    }

    /**
     * 用户评论列表
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     * @throws RemoteException
     */
    public List<L99Comment> findUserComment(Long userId, Long startId, int limit) throws RemoteException {
	if (commentSwitch) {
	    return commentClient.findUserComment(userId, DashboardType.TEXT, startId, limit);
	} else {
	    return new ArrayList<L99Comment>();
	}
    }

    /**
     * 回复我的评论
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     * @throws RemoteException
     */
    public List<L99Comment> findReplyComment(Long userId, Long startId, int limit) throws RemoteException {
	if (commentSwitch) {
	    return commentClient.findReplyComment(userId, DashboardType.TEXT, startId, limit);
	} else {
	    return new ArrayList<L99Comment>();
	}
    }

    /**
     * 内容的评论数量
     * 
     * @param dashboardType
     * @param dashboardData
     * @return
     * @throws RemoteException
     */
    public int getAllCommentNumByTargetId(Integer dashboardType, Long dashboardData) throws RemoteException {
	if (commentSwitch) {
	    return commentClient.getAllCommentNumByTargetId(dashboardData, dashboardType, null, false, 0);
	} else {
	    return 0;
	}
    }

    /**
     * 删除评论
     * 
     * @param commentId
     * @return
     */
    public boolean delComment(Long commentId) {
	if (commentSwitch) {
	    return commentClient.delComment(commentId);
	} else {
	    return false;
	}
    }

    public void setCommentSwitch(boolean commentSwitch) {
	this.commentSwitch = commentSwitch;
    }

}
