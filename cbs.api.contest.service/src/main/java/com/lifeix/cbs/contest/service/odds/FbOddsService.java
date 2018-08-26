package com.lifeix.cbs.contest.service.odds;

import com.lifeix.cbs.contest.bean.odds.*;
import org.json.JSONException;

import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;
import com.lifeix.user.beans.ListResponse;

/**
 * 足球赔率接口
 * 
 * @author lifeix-sz
 * 
 */
public interface FbOddsService {

    /**
     * 足球赔率列表
     * 
     * @param contestId
     * @param userId
     * @param hasContest
     * @param oddsType
     * @param client
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public OddsResponse findFbOddsResponse(Long contestId, Long userId, boolean hasContest, int oddsType, String client)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException;

    /**
     * 获取足球胜平负赔率列表
     * 
     * @param startId
     * @param limit
     * @return
     */
    public OddsOpListResponse findManageOpOdds(Long startId, Integer limit, Boolean isFive, Integer byOrder);

    /**
     * 获取足球让球胜平负赔率列表
     * 
     * @param startId
     * @param limit
     * @return
     */
    public OddsJcListResponse findManageJcOdds(Long startId, Integer limit, Boolean isFive, Integer byOrder);

    /**
     * 更新足球赔率（胜平负）
     * 
     * @param oddsOpResponse
     * @return
     */
    public void updateOpOdds(OddsOpResponse oddsOpResponse) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 更新足球赔率（让球胜平负）
     * 
     * @param oddsJcResponse
     * @return
     */
    public void updateJcOdds(OddsJcResponse oddsJcResponse) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 更新足球赔率（大小球）
     */
    public void updateSizeOdds(OddsSizeResponse oddsSizeResponse) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 获取足球赛事赔率历史
     * 
     * @param contestId
     * @param oddsType
     * @param startId
     * @param limit
     * @return
     */
    public ListResponse findOddsHistory(Long contestId, Integer oddsType, Long startId, Integer limit)
	    throws L99IllegalParamsException;

    /**
     * 重新所有足球比赛的下单统计
     */
    public CustomResponse resetBetCount();

    /**
     * 重置赛事下单统计
     * 
     * @param contestType
     * @return
     * @throws L99IllegalParamsException
     */
    public CustomResponse resetContestCount(Integer contestType) throws L99IllegalParamsException;

}
