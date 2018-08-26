package com.lifeix.cbs.content.service.frontpage;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.lifeix.cbs.content.bean.frontpage.FrontPageListResponse;
import com.lifeix.cbs.content.bean.frontpage.FrontPageResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalOperateException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import org.json.JSONException;


/**
 * Created by lhx on 15-11-30 上午10:45
 *
 * @Description
 */
public interface FrontPageService {

    /**
     * 添加头版
     * ContentConstants.FrontPage（传入的type的值）
     * 头版广告区 类型一 用户的吐槽 TYPE_AD_REMAKES  = -1;
     * 头版广告区 类型二 下单理由 TYPE_AD_REASON  = -2;
     * 头版广告区 类型三 推荐 TYPE_AD_RECOMMEND  = -3;
     * 头版内容区 类型三 推荐咨询 TYPE_CONTENT_RECOMMEND  = -4;
     * 头版内容区 类型三 推荐比赛 TYPE_CONTENT_RECOMMEND  = -5;
     * 头版内容区 类型三 推荐网页 TYPE_CONTENT_RECOMMEND  = -6;
     * 头版广告区 全部 TYPE_AD_ALL  = -100;
     * <p/>
     * 头版内容区 类型一 用户的吐槽 TYPE_CONTENT_REMAKES  = 1;
     * 头版内容区 类型二 下单理由 TYPE_CONTENT_REASON  = 2;
     * 头版内容区 类型三 官方消息 TYPE_CONTENT_NEWS  = 3;
     * 头版内容区 类型三 推荐咨询 TYPE_CONTENT_RECOMMEND  = 4;
     * 头版内容区 类型三 推荐比赛 TYPE_CONTENT_RECOMMEND  = 5;
     * 头版内容区 类型三 推荐网页 TYPE_CONTENT_RECOMMEND  = 6;
     * 头版内容区 全部 TYPE_CONTENT_ALL  = 100;
     */
    public void addFrontPage(Long contestId, Integer priceId, Integer contestType, Long userId, String desc, String image, String link, Integer type, Long contentId, String title, Long innnerContestId, Integer innnercontestType) throws L99IllegalDataException, L99IllegalParamsException, JSONException;

    /**
     * 获取头版内容区信息
     */
    public FrontPageListResponse getFrontPagesContent(Long userId, Long startId, Integer limit, Integer type) throws L99IllegalDataException, L99IllegalParamsException, L99NetworkException, JSONException;

    /**
     * 获取头版广告区信息
     */
    public FrontPageListResponse getFrontPagesAd(Integer type) throws L99IllegalParamsException, L99IllegalDataException, L99NetworkException, JSONException;

    /**
     * 获取头版内容区信息（内部接口）
     */
    public FrontPageListResponse findFrontPagesInner(Long startId, Long endId, Integer limit, Integer type, Integer status, Integer skip) throws L99IllegalDataException, L99IllegalParamsException, L99NetworkException, JSONException;

    /**
     * 修改
     */
    public void editFrontPagesInner(Long id, Integer status, Integer type, Long contestId, Integer contestType,
                                    String desc, String path, String link, Long contentId, String title,
                                    Long innnerContestId, Integer innnerContestType,Integer oper) throws JsonProcessingException, L99IllegalOperateException, JSONException, L99IllegalParamsException, L99NetworkException, L99IllegalDataException;

    /**
     * 根据contentId进行删除
     */
    public boolean deleteFrontPagesByContentId(Long contentId, Integer type) throws L99IllegalParamsException;

    /**
     * 对现有的redis队列进行操作
     */
    public void operateQueue(Long fId, Integer status, Integer type);

    /**
     * 查询单个
     */
    public FrontPageResponse findFrontPage(Long id, Integer type) throws L99IllegalDataException, L99IllegalParamsException, L99NetworkException, JSONException;


    void addFrontPageFromCircle(Long circleId, int type) throws L99NetworkException, L99IllegalParamsException, JSONException, L99IllegalDataException;
}