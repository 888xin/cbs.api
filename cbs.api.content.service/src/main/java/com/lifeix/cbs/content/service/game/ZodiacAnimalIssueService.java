package com.lifeix.cbs.content.service.game;

import com.lifeix.cbs.content.bean.game.ZodiacAnimalListResponse;
import com.lifeix.cbs.content.bean.game.ZodiacAnimalResponse;
import com.lifeix.cbs.content.bean.game.ZodiacPlayListResponse;
import com.lifeix.cbs.content.bean.game.ZodiacPlayResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import org.json.JSONException;

public interface ZodiacAnimalIssueService {

    /**
     * 获得当前期所有信息
     */
    public ZodiacAnimalResponse getNowIssue(Long userId, boolean isLongbi) throws L99IllegalParamsException, JSONException,
	    L99NetworkException, L99IllegalDataException;

    /**
     * 用户下单
     * @param bets 生肖1,下单的金额1;生肖2,下单的金额2
     * @param userCouponIds 龙筹券ids 多个id用英文逗号隔开
     */
    public ZodiacPlayResponse bet(Long userId, Integer gameId, String bets, String userCouponIds, boolean isLongbi)
	    throws L99IllegalParamsException, L99NetworkException, L99IllegalDataException, JSONException;

    /**
     * 获取当前期中奖号码
     */
    public ZodiacAnimalResponse getCurrentWinner(Integer gameId) throws L99IllegalParamsException, L99NetworkException,
	    L99IllegalDataException, JSONException;

    /**
     * 改变下单每条狗的下单数
     */
    public void changeBetNum(Integer gameId, String zodiacNo, Long num);

    /**
     * 获得每条狗的下单数
     */
    public ZodiacAnimalResponse getBetNum(Integer gameId);

    /**
     * 派奖
     */
    public ZodiacAnimalResponse toPrize(Integer gameId) throws L99IllegalParamsException, JSONException,
	    L99IllegalDataException, L99NetworkException;

    /**
     * 获取用户下单列表
     */
    public ZodiacPlayListResponse getUserPlayHistorys(Long userId, Integer startId, Integer limit);

    /**
     * 获取往期中奖记录
     */
    public ZodiacAnimalListResponse getPlayHistorys(Integer startId, Integer limit);

    /**
     * 手动派奖
     */
    public void toPrize(String startTime, String endTime) throws L99IllegalParamsException, L99IllegalDataException, L99NetworkException, JSONException;

}
