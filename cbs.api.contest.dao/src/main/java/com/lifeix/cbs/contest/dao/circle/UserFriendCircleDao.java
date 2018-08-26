package com.lifeix.cbs.contest.dao.circle;

import java.util.List;

import com.lifeix.cbs.contest.dto.circle.UserFriendCircle;

public interface UserFriendCircleDao {
/**
 * 活得用户猜友圈内容id
 */
    public List<Long> getUserFriendCircleIds(Long userId,List<Long> targetIds,Integer friendType, Integer page, Integer limit);
    
    public UserFriendCircle getUserFriendCircle(Long friendCircleId); 
    
    public Integer insert(List<UserFriendCircle> userCirlces);
    
    /**
     * 更新朋友圈
     * @param ufc
     * @return
     */
    public Integer update(UserFriendCircle ufc);
    
    public Integer deleteByfriendCircleId(Long friendCircleId);
}
