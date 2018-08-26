package com.lifeix.cbs.contest.dao.comment;

import java.util.List;

import com.lifeix.cbs.contest.dto.comment.Comment;

public interface CommentDao {
    /**
     * 添加评论-id自增
     * @param comment
     * @return
     */
    public Long insertAndGetPrimaryKey(Comment comment);
    /**
     * 查找评论
     * @param startId
     * @param endId
     * @param limit
     * @return
     */
    public List<Comment> findComments(Long contestId ,Integer contestType ,Long startId, Long endId, int limit);
    /**
     * 删除猜友圈的评论
     * @param cirId
     * @return
     */
    public boolean deleteBycirId(Long cirId ,Long contestId,Integer contestType);


}
