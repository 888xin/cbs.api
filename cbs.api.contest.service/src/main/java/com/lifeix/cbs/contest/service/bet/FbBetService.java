package com.lifeix.cbs.contest.service.bet;

import com.lifeix.cbs.contest.bean.bet.BetLogResponse;
import org.json.JSONException;

import com.lifeix.cbs.contest.bean.bet.BetLogListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

/**
 * 下单接口
 * 
 * @author lifeix-sz
 * 
 */
public interface FbBetService {

    /**
     * 足球下单
     * 
     * @param userId
     * @param contestId
     * @param longbi
     * @param playId
     * @param support
     * @param r1
     * @param r2
     * @param r3
     * @param bet
     * @param content
     * @param client
     * @param ipaddress
     * @param permission
     * @param couponId
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public CustomResponse addFBBet(Long userId, Long contestId, boolean longbi, Integer playId, Integer support, Double r1,
	    Double r2, Double r3, Double bet, String content, String client, String ipaddress, Boolean permission,
	    Long couponId) throws L99IllegalParamsException, L99IllegalDataException, JSONException;

    /**
     * 获取足球下单记录
     * 
     * @param startId
     * @param contestId
     * @param type
     * @param userId
     * @param settle
     * @param limit
     * @return
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     */
    public BetLogListResponse getFbBetList(Long startId, Long contestId, Integer type, Long userId, Boolean settle,
	    Integer limit, String startTime, String endTime) throws L99IllegalDataException, L99IllegalParamsException;


    /**
     * 下注赔率修复
     */
    public BetLogResponse repair(Long bId, Integer playType, Double repairRoi, String reason) throws JSONException, L99IllegalDataException, L99IllegalParamsException;

}
