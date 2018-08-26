package com.lifeix.cbs.contest.dao.circlecomment.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.circlecomment.FriendCircleCommDao;
import com.lifeix.cbs.contest.dto.circle.FriendCircleComment;

@Service("friendCircleCommDao")
public class FriendCircleCommDaoImpl extends ContestDaoSupport implements FriendCircleCommDao {

    @Override
    public FriendCircleComment selectById(Long id) {
	FriendCircleComment comment = sqlSession.selectOne("FriendCircleCommentMapper.selectById", id);
	return comment;
    }

    @Override
    public List<FriendCircleComment> selectByIds(List<Long> ids) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("ids", ids);
	List<FriendCircleComment> comments = sqlSession.selectList("FriendCircleCommentMapper.selectByIds", params);
	return comments;
    }

    @Override
    public Long insertAndGetPrimaryKey(FriendCircleComment comment) {
	sqlSession.insert("FriendCircleCommentMapper.insertAndGetPrimaryKey", comment);
	return comment.getId();
    }

    @Override
    public Boolean deleteById(Long id) {
	int res = sqlSession.delete("FriendCircleCommentMapper.deleteById", id);
	if (res > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public List<FriendCircleComment> selectByContent(Long contentId, Long startId, int limit) {
	List<FriendCircleComment> comments = null;
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contentId", contentId);
	if (startId != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	comments = sqlSession.selectList("FriendCircleCommentMapper.selectByContent", params);
	return comments;
    }

    @Override
    public boolean updateReadComment(Long circleUserId, Long contentId) {
	Map<String, Object> parmas = new HashMap<String, Object>();
	parmas.put("circleUserId", circleUserId);
	parmas.put("contentId", contentId);
	int nums = sqlSession.update("FriendCircleCommentMapper.updateReadComment", parmas);
	if (nums > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean updateShield(List<Long> ids) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("ids", ids);
	int nums = sqlSession.update("FriendCircleCommentMapper.updateShield", params);
	if (nums > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public Long getCounts(Long contentId, Long userId) {
	Map<String, Object> parmas = new HashMap<String, Object>();
	parmas.put("contentId", contentId);
	parmas.put("userId", userId);
	Long nums = sqlSession.selectOne("FriendCircleCommentMapper.getCounts", parmas);
	return nums;
    }

    @Override
    public List<Long> selectIdsByContentId(Long contentId) {
	List<Long> ids = sqlSession.selectList("FriendCircleCommentMapper.selectIdsByContentId", contentId);
	return ids;
    }

    @Override
    public List<FriendCircleComment> getUserComment(Long userId, Long startId, Long endId, Integer limit) {

	List<FriendCircleComment> comments = null;
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("userId", userId);
	if (startId != null) {
	    params.put("startId", startId);
	}
	if (endId != null) {
	    params.put("endId", endId);
	}
	params.put("limit", limit);
	comments = sqlSession.selectList("FriendCircleCommentMapper.getUserComment", params);
	return comments;

    }

    @Override
    public List<FriendCircleComment> getCommentList(Long userId, Long endId, Integer limit) {
	List<FriendCircleComment> comments = null;
	Map<String, Object> params = new HashMap<String, Object>();
	if (userId != null) {
	    params.put("userId", userId);
	}
	if (endId != null) {
	    params.put("endId", endId);
	}
	params.put("limit", limit);
	comments = sqlSession.selectList("FriendCircleCommentMapper.getCommentList", params);
	return comments;
    }

}
