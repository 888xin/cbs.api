package com.lifeix.cbs.contest.dao.circle;

import com.lifeix.cbs.contest.dto.circle.FriendCircle;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FriendCircleDao {
    /**
     * 根据ids活得猜友圈记录(不包含押押)
     */
    public List<FriendCircle> getFriendCircleByIds(List<Long> circleIds, String client, Boolean hasContent);

    /**
     * 我的战绩(不包含押押)
     */
    public List<FriendCircle> getMyCircleByIds(Long userId, String client, Integer hasContent, int page, int limit);

    /**
     * 我的押押列表
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     */
    public List<FriendCircle> getMyYayaCircle(Long userId, Long startId, int limit);

    public Integer insert(FriendCircle friendCircle);

    public FriendCircle findById(Long id);

    public Integer deleteById(Long id);

    /**
     * 更新内容附加字段
     */
    public boolean updateFriendCircle(FriendCircle cont);

    public List<FriendCircle> getInnerCircles(Long userId, String searchKey, Long startId, Long endId, Integer limit,
	    Integer skip);

    public Integer getInnerCirclesNum(Long userId, String searchKey);

    /**
     * 根据contest来获取
     */
    public List<FriendCircle> findByContestId(Integer contestType, Long contestId);

    /**
     * 查询未结算的战绩
     * 
     * @param startId
     * @param limit
     * @return
     */
    public List<FriendCircle> findNotSettles(Long startId, int limit);

    /**
     * 获取下单理由
     */
    List<FriendCircle> getInnerReasonList(Long startId, Long endId, Integer limit, Integer skip, int type);

    /**
     * 获取下单理由（外）
     */
    List<FriendCircle> getReasonList(long contestId, int contestType, Long startId, int limit);

    /**
     * 获取下单理由数量
     */
    Integer getInnerReasonNum(int type);

    /**
     * 是否有下单理由
     */
    boolean isHasReason(long contestId, int contestType);

    /**
     * 后台查询未结算战绩
     * 
     * @param startId
     * @param limit
     * @return
     */
    public List<FriendCircle> findInnerNotSettles(Long startId, int limit);

    /**
     * 获取下单理由数量（批量）
     */
    Map<String, String> getReasonUser(Set<String> set);

}
