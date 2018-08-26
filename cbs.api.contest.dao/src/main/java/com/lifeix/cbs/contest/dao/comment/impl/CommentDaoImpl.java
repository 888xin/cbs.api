package com.lifeix.cbs.contest.dao.comment.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.comment.CommentDao;
import com.lifeix.cbs.contest.dto.comment.Comment;

@Service("commentDao")
public class CommentDaoImpl extends ContestDaoSupport implements CommentDao {

    private static final String COMMENT_LIST_KEY = "selectComments";

    @Override
    public Long insertAndGetPrimaryKey(Comment comment) {
	comment.setContent(comment.getContent());
	sqlSession.insert("CommentMapper.insert", comment);
	deleteCache(comment);
	return comment.getId();
    }

    @Override
    public List<Comment> findComments(Long contestId, Integer contestType, Long startId, Long endId, int limit) {
	List<Comment> comments = null;
	String cacheKey = null;
	cacheKey = getCustomCache(COMMENT_LIST_KEY, contestId);
	if (startId == null && limit == 20 && endId == null) { // 缓存内容推荐第一页
	    comments = memcacheService.get(cacheKey);
	}
	if (comments == null) {
	    if (startId == null) {
		startId = Long.MAX_VALUE;
	    }
	    String sqlMap = "CommentMapper.selectAll";
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("startId", startId);
	    params.put("limit", limit);
	    params.put("contestId", contestId);
	    if (contestType != null) {
		params.put("contestType", contestType);
	    }

	    comments = sqlSession.selectList(sqlMap, params);
	    if ((startId == Long.MAX_VALUE && limit == 20) && limit == 20) {
		memcacheService.set(cacheKey, comments);
	    }
	}
	if (endId != null) {
	    List<Comment> ret = new ArrayList<Comment>();
	    for (Comment comm : comments) {
		if (comm.getId() > endId) {
		    ret.add(comm);
		}
	    }
	    comments.clear();
	    comments.addAll(ret);
	}
	return comments;
    }

    @Override
    public boolean deleteBycirId(Long cirId, Long contestId, Integer contestType) {

	int delete = sqlSession.delete("CommentMapper.deleteByCirId", cirId);
	if (delete > 0) {
	    deleteCacheByContestId(contestId);
	}
	return delete > 0;
    }

    public void deleteCache(Comment comment) {

	if (comment == null) {
	    return;
	}
	// 列表缓存
	if (comment.getContestId() != null) {
	    String cacheKey2 = getCustomCache(COMMENT_LIST_KEY, comment.getContestId());
	    memcacheService.delete(cacheKey2);
	}

    }

    public void deleteCacheByContestId(Long contestId) {
	if (contestId == null) {
	    return;
	}
	// 列表缓存
	String cacheKey2 = getCustomCache(COMMENT_LIST_KEY, contestId);
	memcacheService.delete(cacheKey2);

    }
}
