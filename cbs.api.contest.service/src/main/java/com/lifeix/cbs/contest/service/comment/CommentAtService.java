package com.lifeix.cbs.contest.service.comment;

import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

/**
 * 赛事评论
 * @author lifeix
 *
 */
public interface CommentAtService {

    /**
     * 添加at评论
     * @param contestId
     * @param userId
     * @param content
     * @param images
     * @param ipaddress
     * @return
     * @throws L99IllegalParamsException 
     * @throws L99NetworkException 
     * @throws L99IllegalDataException 
     */
    public void postCommentAt(Integer contestType,Long contestId, Long sendUserId, Long acceptUserId,String content,String ipaddress, String client) throws L99IllegalParamsException, L99NetworkException, L99IllegalDataException;

}
