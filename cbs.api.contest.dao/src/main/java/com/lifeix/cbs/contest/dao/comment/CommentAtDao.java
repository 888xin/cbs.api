package com.lifeix.cbs.contest.dao.comment;

import com.lifeix.cbs.contest.dto.comment.CommentAt;

public interface CommentAtDao {
    /**
     * 添加评论-id自增
     * @param comment
     * @return
     */
    public Long insertAndGetPrimaryKey(CommentAt comment);
    
    
    public CommentAt getById(Long id);
}
