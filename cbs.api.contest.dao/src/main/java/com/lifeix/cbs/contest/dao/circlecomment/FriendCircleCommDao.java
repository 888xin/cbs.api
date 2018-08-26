package com.lifeix.cbs.contest.dao.circlecomment;

import java.util.List;

import com.lifeix.cbs.contest.dto.circle.FriendCircleComment;

/**
 * 
 * @author jacky
 * 
 */
public interface FriendCircleCommDao {
    /**
     * 根据主键查询记录
     * 
     * @param id
     * @return
     */
    public FriendCircleComment selectById(Long id);

    /**
     * 根据主键列表查询记录
     * 
     * @param id
     * @return
     */
    public List<FriendCircleComment> selectByIds(List<Long> ids);

    /**
     * 根据contentId获取id列表
     * 
     * @param contentId
     * @return
     */
    public List<Long> selectIdsByContentId(Long contentId);

    /**
     * 获取评论列表带分页
     * 
     * @param userId
     * @param endId
     * @param limit
     * @return
     */
    public List<FriendCircleComment> getCommentList(Long userId, Long endId, Integer limit);

    /**
     * 插入
     * 
     * @param comment
     * @return
     */
    public Long insertAndGetPrimaryKey(FriendCircleComment comment);

    /**
     * 根据主键删除
     * 
     * @param id
     * @return
     */
    public Boolean deleteById(Long id);

    /**
     * 根据发表的朋友圈Id获取评论列表
     * 
     * @param contentId
     * @param startId
     * @param limit
     * @return
     */
    public List<FriendCircleComment> selectByContent(Long contentId, Long startId, int limit);

    /**
     * 更新发布猜友圈用户所有未读评论为已读评论
     * 
     * @param circleUserId
     * @return
     */
    public boolean updateReadComment(Long circleUserId, Long contentId);

    /**
     * 后台屏蔽已发表的评论
     * 
     * @param ids
     * @return
     */
    public boolean updateShield(List<Long> ids);

    /**
     * 获取单篇猜友圈所有评论数目
     * 
     * @param contentId
     * @param userId
     * @return
     */
    public Long getCounts(Long contentId, Long userId);

    /**
     * 获得用户的评论列表
     */
    public List<FriendCircleComment> getUserComment(Long userId, Long startId, Long endId, Integer limit);

}
