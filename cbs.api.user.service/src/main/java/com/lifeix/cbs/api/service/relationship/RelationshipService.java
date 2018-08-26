package com.lifeix.cbs.api.service.relationship;

import java.util.List;

import com.lifeix.cbs.api.bean.relationship.RelationShipListResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface RelationshipService {
    
    /**
     * 加关注
     * @param accountId
     * @param targetId
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException 
     */
    public CbsUserResponse addFollow(Long accountId, Long targetId) throws L99IllegalParamsException, L99IllegalDataException;
    
    /**
     * 取消关注
     * @param accountId
     * @param targetId
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException 
     */
    public void deleteFollow(Long accountId, Long targetId) throws L99IllegalParamsException, L99IllegalDataException;
    

    /**
     * 根据类型获取用户（关注、粉丝、双向关注）列表
     * 
     * @param targetId
     * @param startId
     * @param type
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    public RelationShipListResponse getRelationshipByType(Integer type, Long accountId,Long targetId, Long startId, Long endId,
	    Integer limit) throws L99IllegalParamsException;
    
    /**
     * 获得用户所有 关注的用户id
     * @param accountId
     * @return
     */
    public List<Long> getAllMeAttention(Long accountId);
    
    /**
     * 获得用户所有 我的粉丝id
     * @param accountId
     * @return
     */
    public List<Long> getAllAttentionMe(Long accountId);
    
    
}