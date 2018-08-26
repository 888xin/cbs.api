package com.lifeix.cbs.contest.service.cirle;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lifeix.cbs.contest.bean.circle.FriendCircleListResponse;
import com.lifeix.cbs.contest.bean.circle.FriendCircleResponse;
import com.lifeix.exception.service.L99ContentRejectedException;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalOperateException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.user.beans.CustomResponse;

public interface FriendCircleService {

    /**
     * 活得猜友圈记录
     */
    public FriendCircleListResponse getFriendsCircle(Long accountId, String client, Integer friendType, Integer page,
	    Integer limit) throws L99IllegalParamsException;

    /**
     * 获取跟投信息
     */
    public FriendCircleResponse getFollowInfo(Long userId, Long friendCircleId) throws L99IllegalParamsException;

    /**
     * 我的普通下单记录
     */
    public FriendCircleListResponse getMyFriendsCircle(Long userId, Long friendCircleId, String client, Integer friendType,
	    Integer page, Integer limit) throws L99IllegalParamsException;

    /**
     * 我的押押下单记录
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    public FriendCircleListResponse getMyYayaCircle(Long userId, Long startId, int limit) throws L99IllegalParamsException;

    /**
     * 查看自己的下单记录
     */
    public FriendCircleListResponse getInnerMyFriendsCircle(Long userId, String searchKey, Long startId, Long endId,
	    Integer limit, Integer skip) throws L99IllegalParamsException, L99IllegalDataException, NumberFormatException,
	    L99NetworkException, JSONException;

    public FriendCircleListResponse getInnerSolrMyFriendsCircle(Long userId, String searchKey, Long startId, Long endId,
	    Integer limit, Integer skip) throws L99IllegalParamsException, L99IllegalDataException, NumberFormatException,
	    L99NetworkException, JSONException;

    /**
     * 发表内容，包括发表爆料（心情）和竞猜
     */
    public Long postContent(Long userId, Integer type, String content, String images, Long audioId, Long contestId,
	    Integer contestType, String params, String client, Boolean permission, Integer coupon)
	    throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 发布内容
     */
    public void publishContent(Long userId, String content, String images, Long contentId, Integer priceId, String client)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException, L99NetworkException,
	    L99ContentRejectedException;

    /**
     * 根据单篇id删除单篇
     * 
     * @param id
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99NetworkException
     * @throws L99IllegalDataException
     * @throws L99IllegalOperateException
     * @throws JsonProcessingException
     */
    public void deleteContentByid(Long id) throws L99IllegalParamsException;

    public void deleteContentByids(String ids) throws L99IllegalParamsException;

    /**
     * 根据单篇id删除单篇
     * 
     * @param id
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99NetworkException
     * @throws L99IllegalDataException
     * @throws L99IllegalOperateException
     * @throws JsonProcessingException
     */
    public void deleteInnerContentByid(Long id) throws L99IllegalParamsException;

    /**
     * 根据contestId来删除
     */
    public void deleteByContestId(Integer contestType, Long contestId) throws L99IllegalParamsException;

    /**
     * 更新竞猜的比赛结果信息
     * 
     * @param contentId
     * @param result
     * @param repair
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     */
    public void editContentContest(Long contentId, String result, Boolean repair) throws L99IllegalDataException,
	    L99IllegalParamsException;

    /**
     * 获取单条吐槽
     * 
     * @param contentId
     * @return
     * @throws JSONException
     * @throws L99NetworkException
     * @throws L99IllegalParamsException
     */
    public FriendCircleResponse getSingleContentResponse(Long contentId) throws L99IllegalParamsException,
	    L99NetworkException, JSONException;

    /**
     * 获取单条吐槽
     */
    public Map<Long, FriendCircleResponse> getMapContentResponse(List<Long> contentIds) throws L99IllegalParamsException,
	    L99NetworkException, JSONException;

    /**
     * 获取多条下单内容
     * 
     * @param contentIds
     * @return
     * @throws L99IllegalParamsException
     * @throws L99NetworkException
     * @throws JSONException
     */
    public Map<Long, FriendCircleResponse> getMapContentResponseWithNoComments(List<Long> contentIds)
	    throws L99IllegalParamsException;

    /**
     * 获取多条下单内容中获胜赔率
     * 
     * @param contentIds
     * @return
     */
    public Map<Long, Double> getMapContentWinOdds(List<Long> contentIds);

    /**
     * add by lhx on 16-01-12 内部接口用来免费通过朋友圈发表头版
     */
    public void publishContentInner(Long userId, String content, String images, Long contentId, Integer type)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException, L99NetworkException,
	    L99ContentRejectedException;

    /**
     * 清理未添加结果的战绩
     */
    public CustomResponse settleCircle(String circleIds);

    /**
     * 获取下单理由
     */
    FriendCircleListResponse getInnerReasonList(Long startId, Long endId, Integer limit, Integer skip, int type)
	    throws L99NetworkException, L99IllegalParamsException, JSONException;

    /**
     * 获取猜友圈下单理由
     */
    FriendCircleListResponse getReasonsList(Long contestId, Integer contestType, Long startId, Integer limit)
	    throws L99IllegalParamsException, JSONException, L99NetworkException;

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
     * @throws JSONException
     * @throws L99NetworkException
     * @throws L99IllegalParamsException
     */
    public FriendCircleListResponse getInnerFirCirNoSettleList(Long startId, Integer limit)
	    throws L99IllegalParamsException, L99NetworkException, JSONException;

    /**
     * 押押的单个下注详情
     */
    FriendCircleResponse findOneYayaCircle(Long id) throws L99IllegalParamsException;

}
