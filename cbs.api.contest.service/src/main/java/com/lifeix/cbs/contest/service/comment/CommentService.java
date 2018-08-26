package com.lifeix.cbs.contest.service.comment;

import org.json.JSONException;

import com.lifeix.cbs.contest.bean.comment.CbsCommentListResponse;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

/**
 * 赛事评论
 * @author lifeix
 *
 */
public interface CommentService {

    /**
     * 
     * @param startId 加载更多
     * @param endId   加载最新
     * @param limit
     * @return
     * @throws L99IllegalParamsException 
     * @throws JSONException 
     * @throws L99NetworkException 
     */
    public CbsCommentListResponse findComments(Long contestId,Integer contestType,Long startId, Long endId, int limit) throws L99IllegalParamsException, L99NetworkException, JSONException;
    
    /**
     * 添加评论
     * @param contestId
     * @param userId
     * @param content
     * @param images
     * @param ipaddress
     * @return
     * @throws L99IllegalParamsException 
     */
    public Long postComment(Long contestId,Integer contestType, Long userId, Long acceptUserId, String content,
                            String images, String ipaddress, String client,Long cirCommentId, boolean missionValidate) throws L99IllegalParamsException;
    /**
     * 评论数量
     * @param contestId
     * @return
     */
    public Integer findCommentNum(Long contestId);
    /**
     * 
     * @param cirId  猜友圈评论id
     * @param contestId 赛事id
     */
    public void deleteByCirId(Long cirCommentId ,Long contestId,Integer contestType);


}
