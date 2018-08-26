package com.lifeix.cbs.api.service.user;

import com.lifeix.cbs.api.bean.user.CbsUserStarListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * @author petter
 */
public interface CbsUserStarService {

    /**
     * 获取所有推荐列表
     * 
     * @param hideFlag
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    public CbsUserStarListResponse findAllStars(Boolean hideFlag, Long startId, int limit) throws L99IllegalParamsException;

    /**
     * 更新推荐用户
     * 
     * @param userId
     * @param factor
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void putUserStar(Long userId, Integer factor) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 显示或隐藏推荐
     * 
     * @param userId
     * @param hideFlag
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void onOffUserStar(Long userId, Boolean hideFlag) throws L99IllegalParamsException, L99IllegalDataException;


    /**
     * 获取推荐用户列表
     */
    public CbsUserStarListResponse findStars(Long accountId) throws L99IllegalParamsException;
}
