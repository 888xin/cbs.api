package com.lifeix.cbs.message.service.placard;

import org.json.JSONException;

import com.lifeix.cbs.message.bean.placard.PlacardTempletListResponse;
import com.lifeix.cbs.message.bean.placard.PlacardTempletResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 15-10-19 下午3:55
 *
 * @Description
 */
public interface PlacardTempletService {

    /**
     * 更新系统公告
     */
    public void editPlacardTemplet(Long templetId, String title, String content, String endTime, Boolean disableFlag,
	    Integer linkType, String linkData) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * push公告
     */
    public void pushPlacardTemplet(Long templetId) throws L99IllegalParamsException, JSONException;

    /**
     * 系统公告列表
     */
    public PlacardTempletListResponse findPlacardTemplets(Boolean disableFlag, int nowPage, int limit);

    /**
     * 单个公告信息
     */
    public PlacardTempletResponse viewPlacardTemplet(Long templetId, boolean userFlag) throws L99IllegalParamsException;

    /**
     * 用户未读公告
     * 
     * @param userId
     * @return
     */
    public int unreadNum(Long userId) throws L99IllegalParamsException;

    public void deletePlacardTemplet(Long templetId) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 列表，内部接口用
     * 
     * @param startId
     * @param limit
     * @return
     */
    public PlacardTempletListResponse findPlacardsInner(Long startId, Integer limit);

    /**
     * 获取赛事关联公告
     * 
     * @param cupId
     * @param contestId
     * @param type
     * @return
     */
    public String findPlacardRelation(Long cupId, Long contestId, int type);

    /**
     * 一键领取龙筹卷
     * 
     * @param templetId
     * @param userId
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws NumberFormatException
     */
    public void oneKeyCoupon(Long templetId, Long userId) throws L99IllegalParamsException, NumberFormatException,
	    L99IllegalDataException;
}
