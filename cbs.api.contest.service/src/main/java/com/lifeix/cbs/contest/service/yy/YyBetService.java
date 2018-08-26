package com.lifeix.cbs.contest.service.yy;

import org.json.JSONException;

import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

/**
 * 押押下单接口
 * 
 * @author lifeix-sz
 * 
 */
public interface YyBetService {

    /**
     * 押押下单
     * 
     * @param userId
     * @param contestId
     * @param longbi
     * @param support
     * @param roi
     * @param bet
     * @param content
     * @param client
     * @param ipaddress
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public CustomResponse addYyBet(Long userId, Long contestId, boolean longbi, Integer support, Double roi, Double bet,
	    String content, Long couponUserId, String client, String ipaddress, String from)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException;

    /**
     * 重新所有押押比赛的下单统计
     */
    public CustomResponse resetBetCount();

}
