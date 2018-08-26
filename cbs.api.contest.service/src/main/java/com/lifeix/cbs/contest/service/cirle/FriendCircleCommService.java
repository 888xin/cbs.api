package com.lifeix.cbs.contest.service.cirle;

import java.util.List;

import org.json.JSONException;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.contest.bean.circle.FriendCircleCommListResponse;
import com.lifeix.cbs.contest.bean.circle.FriendCircleCommResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

public interface FriendCircleCommService {

    /**
     * 添加猜友圈评论
     * 
     * @param contentId
     * @param circleUserId
     * @param commUserId
     * @param content
     * @param image
     * @param reUserId
     * @param reContent
     * @param reImage
     * @param source
     * @param ipaddress
     * @return
     */
    public DataResponse<Object> addComment(Long contentId, Long circleUserId, Long commUserId, String content, String image,
	    Long reUserId, String reContent, String reImage, String source, String ipaddress);

    /**
     * 删除热门评论
     * 
     * @param id
     * @return
     */
    public DataResponse<Object> delComment(Long id);

    /**
     * 单条猜友圈评论列表详细
     * 
     * @param contentId
     * @param startId
     * @param limit
     * @return
     */
    public DataResponse<FriendCircleCommListResponse> findCommentDetail(Long contentId, Long userId, Long startId, int limit);

    /**
     * 获取未读评论列表（只显示回复和主题）
     * 
     * @param userId
     * @param pageNum
     * @param limit
     * @return
     */
    public DataResponse<FriendCircleCommListResponse> findUnreadCommentList(Long userId, Integer pageNum, Integer limit);

    /**
     * 获取历史评论列表（只显示回复和主题）
     * 
     * @param userId
     * @param startId
     * @param endId
     * @param limit
     * @return
     */
    public DataResponse<FriendCircleCommListResponse> findHistoryCommentList(Long userId, Long startId, Long endId,
	    Integer limit);

    /**
     * 获取评论列表
     * 
     * @param userId
     * @param startId
     * @param endId
     * @param limit
     * @return
     */
    public DataResponse<FriendCircleCommListResponse> findCommentList(Long userId, Long endId, Integer limit);

    /**
     * 根据主键id列表屏蔽用户评论
     * 
     * @param ids
     * @return
     */
    public DataResponse<Object> updateShield(List<Long> ids);

    /**
     * 获取用户未读取的评论数
     * 
     * @param userId
     * @return
     */
    public DataResponse<Object> getUnreadCounts(Long userId, Integer api_version);

    /**
     * 获取单篇评论数
     * 
     * @param contentId
     * @param userId
     * @return
     */
    public DataResponse<Object> getSingleCommentCounts(Long contentId, Long userId);

    /**
     * 获取单条评论
     * 
     * @param id
     *            主键
     * @return
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99IllegalDataException
     * @throws L99NetworkException
     */
    public FriendCircleCommResponse getSingleComment(Long id) throws L99IllegalParamsException, JSONException,
	    L99NetworkException, L99IllegalParamsException, L99IllegalDataException;
}
