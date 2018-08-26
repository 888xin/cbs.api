package com.lifeix.cbs.contest.dao.comment.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.comment.CommentAtDao;
import com.lifeix.cbs.contest.dto.comment.CommentAt;

@Service("commentAtDao")
public class CommentAtDaoImpl extends ContestDaoSupport implements CommentAtDao {

    private static final String COMMENT_LIST_KEY = "selectCommentsAt";

    @Override
    public Long insertAndGetPrimaryKey(CommentAt comment) {
	comment.setContent(comment.getContent());
	sqlSession.insert("CommentAtMapper.insertAndGetPrimaryKey", comment);
	deleteCache(comment);
	return comment.getId();
    }

    public void deleteCache(CommentAt comment) {
	if (comment == null) {
	    return;
	}
	// 列表缓存
	if (comment.getId() != null) {
	    String cacheKey2 = getCustomCache(COMMENT_LIST_KEY, comment.getId());
	    memcacheService.delete(cacheKey2);
	}
    }

    @Override
    public CommentAt getById(Long id) {
	String cacheKey = null;
	cacheKey = getCustomCache(COMMENT_LIST_KEY, id);
	CommentAt comment = memcacheService.get(cacheKey);
	if (comment == null) {
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("id", id);
	    comment = sqlSession.selectOne("CommentAtMapper.getById", params);
	    memcacheService.set(cacheKey, comment);
	}
	return comment;
    }
}
