package com.lifeix.cbs.content.service.comment;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.tiyu.content.bean.comment.CommentListResponse;
import com.lifeix.tiyu.content.bean.comment.CommentResponse;

public interface HessionCommentService {

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
    public DataResponse<Object> LikeContent(Long userId, Long dashboardId, String client, Double lat, Double lng,
	    String ipAddress);

    /**
     * 评论新闻
     * 
     * @param userId
     * @param dashboardId
     * @param content
     * @param image
     * @param ipAddress
     * @return
     */
    public DataResponse<CommentResponse> sendComment(Long userId, Long dashboardId, String content, String image,
	    String client, Double lat, Double lng, String ipAddress);

    /**
     * 回复评论
     * 
     * @param userId
     * @param commentId
     * @param content
     * @param image
     * @param ipAddress
     * @return
     */
    public DataResponse<CommentResponse> replyComment(Long userId, Long commentId, String content, String image,
	    String client, Double lat, Double lng, String ipAddress);

    /**
     * 评论列表
     * 
     * @param dashboardId
     * @param startId
     * @param limit
     * @return
     */
    public DataResponse<CommentListResponse> findDashboardComments(Long dashboardId, Long startId, int limit);

    /**
     * 用户的评论列表
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     */
    public DataResponse<CommentListResponse> findUserComments(Long userId, Long startId, int limit);

    /**
     * 提到用户的评论列表
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     */
    public DataResponse<CommentListResponse> findReplyUserComments(Long userId, Long startId, int limit);

    /**
     * 查询评论数量
     * 
     * @param dashboardType
     * @param dashboardData
     * @return
     */
    public DataResponse<Integer> findCommentNum(Integer dashboardType, Long dashboardData);

    /**
     * 删除评论
     * 
     * @param id
     * @return
     */
    public DataResponse<Object> dropComment(Long id);

}
