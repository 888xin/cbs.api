package com.lifeix.cbs.contest.service.contest;

import com.lifeix.cbs.contest.bean.contest.ContestAdListResponse;
import com.lifeix.cbs.contest.bean.contest.ContestAdResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 赛事廣告接口
 * 
 * @author lifeix
 * 
 */
public interface ContestAdService {

    /**
     * 单个赛事廣告
     * 
     * @param id
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestAdResponse viewAdContest(Long id) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 添加赛事廣告
     * 
     * @param contestId
     * @param contestType
     * @param title
     * @param images
     * @param text
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void insertContestAd(Long contestId, Integer contestType, String title, String images, String text)
	    throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 修改赛事廣告
     * 
     * @param id
     * @param contestId
     * @param contestType
     * @param title
     * @param images
     * @param text
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void updateContestAd(Long id, Long contestId, Integer contestType, String title, String images, String text)
	    throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 赛事廣告置為隱藏
     * 
     * @param id
     * @param contestId
     * @param contestType
     * @param title
     * @param images
     * @param text
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void updateHide(Long id, boolean hideFlag) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 賽事廣告列表
     * 
     * @param contestType
     * @param hideFlag是否隐藏
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestAdListResponse findContestsAdInner(Integer contestType, Boolean hideFlag, Long startId, int limit)
	    throws L99IllegalParamsException;

    /**
     * 賽事廣告列表
     * 
     * @param contestType
     * @param hideFlag是否隐藏
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestAdListResponse findContestsAd(Integer contestType, int limit) throws L99IllegalParamsException;

}
