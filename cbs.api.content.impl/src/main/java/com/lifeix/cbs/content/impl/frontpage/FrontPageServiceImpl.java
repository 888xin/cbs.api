package com.lifeix.cbs.content.impl.frontpage;

import com.google.gson.Gson;
import com.l99.dto.dashboard.DashboardType;
import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.ContentConstants;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.money.MoneyStatisticService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.content.bean.frontpage.FrontPageBetResponse;
import com.lifeix.cbs.content.bean.frontpage.FrontPageContentResponse;
import com.lifeix.cbs.content.bean.frontpage.FrontPageListResponse;
import com.lifeix.cbs.content.bean.frontpage.FrontPageResponse;
import com.lifeix.cbs.content.dao.frontpage.FrontPageAdDao;
import com.lifeix.cbs.content.dao.frontpage.FrontPageDao;
import com.lifeix.cbs.content.dto.frontpage.FrontPage;
import com.lifeix.cbs.content.impl.redis.RedisCommentCountHandler;
import com.lifeix.cbs.content.service.comment.HessionCommentService;
import com.lifeix.cbs.content.service.contest.ContestNewsService;
import com.lifeix.cbs.content.service.frontpage.FrontPageService;
import com.lifeix.cbs.contest.bean.circle.FriendCircleContestResponse;
import com.lifeix.cbs.contest.bean.circle.FriendCircleListResponse;
import com.lifeix.cbs.contest.bean.circle.FriendCircleResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.service.bb.BbContestService;
import com.lifeix.cbs.contest.service.cirle.FriendCircleCommService;
import com.lifeix.cbs.contest.service.cirle.FriendCircleService;
import com.lifeix.cbs.contest.service.fb.FbContestService;
import com.lifeix.common.utils.JsoupUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.framework.memcache.MemcacheService;
import com.lifeix.nyx.relationship.service.CbsRelationshipService;
import lifeix.framwork.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by lhx on 15-11-30 上午11:05
 *
 * @Description
 */
@Service("frontPageService")
public class FrontPageServiceImpl extends ImplSupport implements FrontPageService {

    // 所有头版（内容区）
    private static final String FRONTPAGE_CONTENT_ALL = "frontpage_content_all";
    // 所有头版（内容区）最多显示的数量
    private static final long FRONTPAGE_CONTENT_LIMIT = 200;

    // 所有头版（内容区）资讯
    private static final String FRONTPAGE_CONTENT_NEWS_ALL = "frontpage_content_news_all";
    // 所有头版（内容区）资讯最多显示的数量
    private static final long FRONTPAGE_CONTENT_NEWS_LIMIT = 200;

    // 头版（内容区）系统消息
    private static final String FRONTPAGE_CONTENT_MESSAGE = "frontpage_content_message";
    // 头版（内容区）系统消息最多显示的数量
    private static final int FRONTPAGE_CONTENT_MESSAGE_LIMIT = 10;

    // 所有头版（广告区 系统）
    private static final String FRONTPAGE_AD_SYSTEM = "frontpage_ad_system";
    // 所有头版（广告区 系统）最多显示的数量
    private static final long FRONTPAGE_AD_SYSTEM_LIMIT = 5;

    // 所有头版（广告区 用户）
    private static final String FRONTPAGE_AD_USER = "frontpage_ad_user";
    // 所有头版（广告区 用户）最多显示的数量
    private static final long FRONTPAGE_AD_USER_LIMIT = 2;

    // 头版广告区 需要扣的龙币
    private static final double AD_LONGBI = 1.00;
    // 头版广告区 类型二 下单理由 需要扣的龙币
    // private static final double AD_REASON_LONGBI = 2.00;
    // 头版内容区 类型一 用户的吐槽 需要扣的龙筹
    // private static final double CONTENT_REMAKES_GOLD = 100.00;
    // 头版内容区 类型二 下单理由 需要扣的龙筹
    // private static final double CONTENT_REASON_GOLD = 100.00;

    @Resource(name = "srt")
    private StringRedisTemplate srt;

    @Autowired
    private FrontPageDao frontPageDao;

    @Autowired
    private FrontPageAdDao frontPageAdDao;

    @Autowired
    private FbContestService fbContestService;

    @Autowired
    private BbContestService bbContestService;

    @Autowired
    private CbsUserService cbsUserService;

    @Autowired
    protected CouponUserService couponUserService;

    @Autowired
    protected MoneyService moneyService;

    @Autowired
    protected FriendCircleService friendCircleService;

    @Autowired
    protected MemcacheService memcacheService;

    @Autowired
    protected MoneyStatisticService moneyStatisticService;

    @Autowired
    protected ContestNewsService contestNewsService;

    @Autowired
    protected FriendCircleCommService friendCircleCommService;

    @Autowired
    protected RedisCommentCountHandler redisCommentCountHandler;

    @Autowired
    protected HessionCommentService hessionCommentService;

    @Autowired
    protected CbsRelationshipService cbsRelationshipService;

    @Override
    public void addFrontPage(Long contestId, Integer priceId, Integer contestType, Long userId, String desc, String image,
                             String link, Integer type, Long contentId, String title, Long innnerContestId, Integer innnercontestType)
            throws L99IllegalDataException, L99IllegalParamsException, JSONException {

        ZSetOperations<String, String> zso = srt.opsForZSet();
        FrontPageContentResponse frontPageContentResponse = new FrontPageContentResponse();
        FrontPage frontPage = new FrontPage();

        if (priceId != null) {
            // if (priceId == 1) {
            // couponUserService.useCoupon(couponId, userId, false, 0D,
            // ContestConstants.ContestType.FRONTPAGE, null, null, "用户发布头版内容区");
            // } else if (priceId == 11) {
            // moneyService.consumeMoney(userId, AD_LONGBI, "发表头版内容区", null);
            // }
            if (priceId == 1) {
                moneyService.consumeMoney(userId, AD_LONGBI, "发表头版内容区", null);
                // add by lhx on 16-04-13
                moneyStatisticService.insertUserConsumer(userId + "", -AD_LONGBI);
            }
        }

        frontPageContentResponse.setDesc(desc);
        frontPageContentResponse.setContestId(contestId);
        frontPageContentResponse.setContestType(contestType);
        frontPageContentResponse.setImage(image);
        frontPageContentResponse.setLink(link);
        frontPageContentResponse.setTitle(title);
        // ===start===保存赛事新闻
        if (innnerContestId != null && innnercontestType != null) {
            frontPage.setInnnerContestId(innnerContestId);
            frontPage.setInnnercontestType(innnercontestType);

            long contest_news_id = contestNewsService.add(title, desc, image, contentId, innnerContestId, innnercontestType);
            frontPageContentResponse.setContest_news_id(contest_news_id);
        }
        // ===end===保存赛事新闻
        // 转换为json字符串
        String content = JsonUtils.toJsonString(frontPageContentResponse);
        // 塞入信息到dto中
        frontPage.setContent(content);
        frontPage.setType(type);
        frontPage.setUserId(userId);
        frontPage.setContentId(contentId);

        // 信息插入到数据库中
        long fid;
        if (type > 0) {
            fid = frontPageDao.insert(frontPage);
        } else {
            fid = frontPageAdDao.insert(frontPage);
        }

        if (fid > 0) {
            if (type > 0) {
                // 头版内容区
                if (type != ContentConstants.FrontPage.TYPE_CONTENT_MESSAGE) {
                    String allContentKey = getCacheId(FRONTPAGE_CONTENT_ALL);
                    // id添加到redis里面
                    zso.add(allContentKey, fid + "", fid);
                    // 控制长度
                    long allContentKeyCount = zso.zCard(allContentKey);
                    if (allContentKeyCount > FRONTPAGE_CONTENT_LIMIT) {
                        zso.removeRange(allContentKey, 0, 0);
                    }
                }
                // 如果是新闻类型的，另外存多一次
                if (type == ContentConstants.FrontPage.TYPE_CONTENT_RECOMMEND_ZHIXUN
                        || type == ContentConstants.FrontPage.TYPE_CONTENT_RECOMMEND_BISAI
                        || type == ContentConstants.FrontPage.TYPE_CONTENT_RECOMMEND_WANGYE) {
                    String allContentNewsKey = getCacheId(FRONTPAGE_CONTENT_NEWS_ALL);
                    // id添加到redis里面
                    zso.add(allContentNewsKey, fid + "", fid);
                    // 控制长度
                    long allContentKeyNewsCount = zso.zCard(allContentNewsKey);
                    if (allContentKeyNewsCount > FRONTPAGE_CONTENT_NEWS_LIMIT) {
                        zso.removeRange(allContentNewsKey, 0, 0);
                    }
                } else if (type == ContentConstants.FrontPage.TYPE_CONTENT_MESSAGE) {
                    String messageKey = getCacheId(FRONTPAGE_CONTENT_MESSAGE);
                    List<FrontPage> frontPageList = frontPageDao.findFrontPages(null, null, FRONTPAGE_CONTENT_MESSAGE_LIMIT,
                            type);
                    memcacheService.set(messageKey, frontPageList);
                }
            } else {
                // 头版广告区
                // 所有内容
                if (type == ContentConstants.FrontPage.TYPE_AD_RECOMMEND_ZHIXUN
                        || type == ContentConstants.FrontPage.TYPE_AD_RECOMMEND_BISAI
                        || type == ContentConstants.FrontPage.TYPE_AD_RECOMMEND_WANGYE) {
                    // 官方系统
                    String allSystemKey = getCacheId(FRONTPAGE_AD_SYSTEM);
                    // id添加到redis里面
                    zso.add(allSystemKey, fid + "", fid);
                    // 控制长度
                    long allContentKeyCount = zso.zCard(allSystemKey);
                    if (allContentKeyCount > FRONTPAGE_AD_SYSTEM_LIMIT) {
                        zso.removeRange(allSystemKey, 0, 0);
                    }
                } else {
                    // 非官方系统（用户）
                    String allUserKey = getCacheId(FRONTPAGE_AD_USER);
                    // id添加到redis里面
                    zso.add(allUserKey, fid + "", fid);
                    // 控制长度
                    long allContentKeyCount = zso.zCard(allUserKey);
                    if (allContentKeyCount > FRONTPAGE_AD_USER_LIMIT) {
                        zso.removeRange(allUserKey, 0, 0);
                    }
                }
            }
            if (type == ContentConstants.FrontPage.TYPE_CONTENT_RECOMMEND_ZHIXUN) {
                //查询评论
                DataResponse<Integer> commentResponse = hessionCommentService.findCommentNum(DashboardType.TEXT, contentId);
                Integer commentNum = commentResponse.getData();
                if (commentNum > 0) {
                    int temp = redisCommentCountHandler.getCommentCount(contentId);
                    if (temp == 0) {
                        redisCommentCountHandler.addCommentCount(contentId, commentNum);
                    }
                }
            }
        } else {
            throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
        }

    }

    @Override
    public FrontPageListResponse getFrontPagesContent(Long userId, Long startId, Integer limit, Integer type)
            throws L99IllegalDataException, L99IllegalParamsException, L99NetworkException, JSONException {
        ZSetOperations<String, String> zso = srt.opsForZSet();

        Set<String> strIds;
        List<FrontPage> frontPageList = new ArrayList<>();
        // 向下加载
        if (startId != null) {

            if (type == ContentConstants.FrontPage.TYPE_CONTENT_ALL) {
                String allContentKey = getCacheId(FRONTPAGE_CONTENT_ALL);
                strIds = zso.reverseRangeByScore(allContentKey, 0, startId - 1, 0, limit);

                if (strIds.size() < limit) {
                    // 到数据库中查询
                    frontPageList = frontPageDao.findFrontPages(startId, null, limit, type);
                } else {
                    List<Long> ids = new ArrayList<Long>();
                    for (String id : strIds) {
                        ids.add(Long.valueOf(id));
                    }
                    // 根据id来查询
                    Map<Long, FrontPage> map = frontPageDao.findByIds(ids);
                    for (Long id : ids) {
                        frontPageList.add(map.get(id));
                    }
                }
            } else if (type == ContentConstants.FrontPage.TYPE_CONTENT_RECOMMEND_NEWS) {
                // 新闻
                String key = getCacheId(FRONTPAGE_CONTENT_NEWS_ALL);
                strIds = zso.reverseRangeByScore(key, 0, startId - 1, 0, limit);

                if (strIds.size() < limit) {
                    // 到数据库中查询
                    frontPageList = frontPageDao.findFrontPages(startId, null, limit, type);
                } else {
                    List<Long> ids = new ArrayList<Long>();
                    for (String id : strIds) {
                        ids.add(Long.valueOf(id));
                    }
                    // 根据id来查询
                    Map<Long, FrontPage> map = frontPageDao.findByIds(ids);
                    for (Long id : ids) {
                        frontPageList.add(map.get(id));
                    }
                }
            } else {
                // 到数据库中查询
                frontPageList = frontPageDao.findFrontPages(startId, null, limit, type);
            }

        } else {
            // 加载第一页

            if (type == ContentConstants.FrontPage.TYPE_CONTENT_ALL) {
                String allContentKey = getCacheId(FRONTPAGE_CONTENT_ALL);
                strIds = zso.reverseRangeByScore(allContentKey, 0, Double.POSITIVE_INFINITY, 0, limit);

                if (strIds.size() < limit) {
                    // 到数据库中查询
                    frontPageList = frontPageDao.findFrontPages(null, null, limit, type);
                } else {
                    List<Long> ids = new ArrayList<Long>();
                    for (String id : strIds) {
                        ids.add(Long.valueOf(id));
                    }
                    Map<Long, FrontPage> map = frontPageDao.findByIds(ids);
                    for (Long id : ids) {
                        frontPageList.add(map.get(id));
                    }
                }
            } else if (type == ContentConstants.FrontPage.TYPE_CONTENT_RECOMMEND_NEWS) {
                // 新闻
                String key = getCacheId(FRONTPAGE_CONTENT_NEWS_ALL);
                strIds = zso.reverseRangeByScore(key, 0, Double.POSITIVE_INFINITY, 0, limit);

                if (strIds.size() < limit) {
                    // 到数据库中查询
                    frontPageList = frontPageDao.findFrontPages(null, null, limit, type);
                } else {
                    List<Long> ids = new ArrayList<Long>();
                    for (String id : strIds) {
                        ids.add(Long.valueOf(id));
                    }
                    // 根据id来查询
                    Map<Long, FrontPage> map = frontPageDao.findByIds(ids);
                    for (Long id : ids) {
                        frontPageList.add(map.get(id));
                    }
                }
            } else {
                // 到数据库中查询
                frontPageList = frontPageDao.findFrontPages(null, null, limit, type);
            }
        }
        return getFrontPageListResponse(frontPageList, userId);
    }

    // 封装值对象
    private FrontPageListResponse getFrontPageListResponse(List<FrontPage> frontPageList, Long userId) throws L99IllegalParamsException,
            L99IllegalDataException, L99NetworkException, JSONException {
        List<FrontPageResponse> frontPageResponseList = new ArrayList<FrontPageResponse>();
        Long startId = null;
        List<Long> userIds = new ArrayList<>();
        //Set<String> reasonSet = new HashSet<>();
        for (FrontPage frontPage : frontPageList) {
            if (frontPage.getUserId() != null && userId != null) {
                userIds.add(frontPage.getUserId());
            }
            //FrontPageResponse frontPageResponse = getFrontPageResponse(frontPage, reasonSet);
            FrontPageResponse frontPageResponse = getFrontPageResponse(frontPage);
            if (frontPageResponse != null) {
                frontPageResponseList.add(frontPageResponse);
                startId = frontPageResponse.getF_id();
            }
        }
        if (userId != null) {
            //查找用户关系
            Map<Long, Integer> map = new HashMap<>();
            if (userIds.size() > 0) {
                map = cbsRelationshipService.getAttentionType(userId, userIds);
            }
            for (FrontPageResponse pageResponse : frontPageResponseList) {
                CbsUserResponse cbsUserResponse = pageResponse.getUser();
                if (cbsUserResponse != null) {
                    Integer type = map.get(cbsUserResponse.getUser_id());
                    cbsUserResponse.setRelationship_type(type);
                }
            }
        }
        //投注理由批量 start
//        if (reasonSet.size() > 0){
//            Map<String, Integer> map = friendCircleService.getReasonNum(reasonSet);
//            for (FrontPageResponse pageResponse : frontPageResponseList) {
//                if (pageResponse.getType() == ContentConstants.FrontPage.TYPE_CONTENT_REASON){
//                    ContestResponse contestResponse = pageResponse.getContest();
//                    String key = contestResponse.getContest_id() + ":" + contestResponse.getContest_type();
//                    Integer num = map.get(key);
//                    pageResponse.setReason_num(num);
//                }
//            }
//        }
        //投注理由批量 end
        FrontPageListResponse frontPageListResponse = new FrontPageListResponse();
        frontPageListResponse.setFrontpages(frontPageResponseList);
        frontPageListResponse.setStartId(startId);
        return frontPageListResponse;
    }

    private FrontPageResponse getFrontPageResponse(FrontPage frontPage) throws L99IllegalParamsException,
            L99IllegalDataException, L99NetworkException, JSONException {
        if (frontPage != null) {
            int frontPageType = frontPage.getType();
            FrontPageResponse frontPageResponse = new FrontPageResponse();
            FrontPageContentResponse frontPageContentResponse = new Gson().fromJson(frontPage.getContent(),
                    FrontPageContentResponse.class);
            Long contestId = frontPageContentResponse.getContestId();
            // 头版内容如果有赛事Id，就需要塞入相关的赛事信息，如果有用户信息，就要塞入用户信息
            if (contestId != null) {
                // 查询比赛，塞入赛事信息
                if (frontPageContentResponse.getContestType() == ContestConstants.ContestType.FOOTBALL) {
                    ContestResponse contestResponse = fbContestService.findFbContestsById(contestId, null);
                    frontPageResponse.setContest(contestResponse);
                } else if (frontPageContentResponse.getContestType() == ContestConstants.ContestType.BASKETBALL) {
                    ContestResponse contestResponse = bbContestService.findBbContestsById(contestId, null);
                    frontPageResponse.setContest(contestResponse);
                }
//                if (reasonSet != null && frontPage.getType() == ContentConstants.FrontPage.TYPE_CONTENT_REASON){
//                    reasonSet.add(contestId + ":" + frontPageContentResponse.getContestType());
//                }
            }
            // 查询用户信息
            Long userId = frontPage.getUserId();
            if (userId != null) {
                CbsUserResponse cbsUserResponse = cbsUserService.selectById(userId);
                frontPageResponse.setUser(cbsUserResponse);

                // 系统消息类型加入用户名
                if (ContentConstants.FrontPage.TYPE_CONTENT_MESSAGE == frontPageType) {
                    frontPageResponse.setDesc(cbsUserResponse.getName() + frontPageContentResponse.getDesc());
                }
            }
            // 塞入
            if (frontPageResponse.getDesc() == null
                    && ContentConstants.FrontPage.TYPE_CONTENT_RECOMMEND_ZHIXUN != frontPageType
                    && ContentConstants.FrontPage.TYPE_AD_RECOMMEND_ZHIXUN != frontPageType
                    && frontPageContentResponse.getDesc() != null) {
                frontPageResponse.setDesc(JsoupUtil.filterTextHtmlWithWrap(frontPageContentResponse.getDesc()));
            } else if (frontPageResponse.getDesc() == null) {
                frontPageResponse.setDesc(frontPageContentResponse.getDesc());
            }
            String imageStr = frontPageContentResponse.getImage();
            if (StringUtils.isNotEmpty(imageStr)) {
                String[] images = imageStr.split(",");
                frontPageResponse.setImage(images);
            }
            frontPageResponse.setLink(frontPageContentResponse.getLink());
            frontPageResponse.setTitle(frontPageContentResponse.getTitle());
            frontPageResponse.setF_id(frontPage.getId());
            frontPageResponse.setType(frontPageType);
            frontPageResponse.setStatus(frontPage.getStatus());
            frontPageResponse.setInnner_contest_id(frontPage.getInnnerContestId());
            frontPageResponse.setInnner_contest_type(frontPage.getInnnercontestType());
            Long contentId = frontPage.getContentId();
            frontPageResponse.setContent_id(contentId);
            frontPageResponse.setAt(frontPage.getAt());
            frontPageResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(frontPage.getCreateTime()));
            frontPageResponse.setTime(CbsTimeUtils.getCbsTimeDiff(frontPage.getCreateTime()));
            // 查找下单记录
            if (frontPageResponse.getType() == ContentConstants.FrontPage.TYPE_CONTENT_REASON && contentId != null) {

                FriendCircleListResponse friendCircleListResponse = friendCircleService.getMyFriendsCircle(null, contentId,
                        null, null, null, 1);

                List<FriendCircleResponse> contents = friendCircleListResponse.getContents();
                if (contents.size() > 0) {
                    FriendCircleResponse friendCircleResponse = contents.get(0);
                    FriendCircleContestResponse friendCircleContestResponse = friendCircleResponse.getContest();
                    if (friendCircleContestResponse != null) {
                        FrontPageBetResponse frontPageBetResponse = new FrontPageBetResponse();
                        frontPageBetResponse.setLongbi(friendCircleContestResponse.isLongbi());
                        frontPageBetResponse.setPlay(friendCircleContestResponse.getPlay());
                        frontPageBetResponse.setSupport(friendCircleContestResponse.getSupport());
                        frontPageBetResponse.setHandicap(friendCircleContestResponse.getHandicap());
                        frontPageBetResponse.setOdds(friendCircleContestResponse.getOdds());
                        frontPageBetResponse.setBet(friendCircleContestResponse.getBet());
                        frontPageBetResponse.setStatus(friendCircleContestResponse.getStatus());
                        frontPageBetResponse.setBack(friendCircleContestResponse.getBack());
                        frontPageBetResponse.setCoupon(friendCircleResponse.getCoupon());
                        frontPageResponse.setBet(frontPageBetResponse);
                    }
                    //评论数
                    if (friendCircleResponse.getCommNum() != null) {
                        frontPageResponse.setCommet_num(friendCircleResponse.getCommNum());
                    }
                }
            } else if (frontPageResponse.getType() == ContentConstants.FrontPage.TYPE_CONTENT_REMAKES && contentId != null) {
                //用户吐槽评论数
                DataResponse<Object> dr = friendCircleCommService.getSingleCommentCounts(contentId, userId);
                if (dr.getData() != null) {
                    frontPageResponse.setCommet_num(Integer.parseInt(dr.getData().toString()));
                }
            } else if (frontPageResponse.getType() == ContentConstants.FrontPage.TYPE_CONTENT_RECOMMEND_ZHIXUN && contentId != null) {
                //推荐资讯评论数
                int num = redisCommentCountHandler.getCommentCount(contentId);
                frontPageResponse.setCommet_num(num);
            }
            return frontPageResponse;
        } else {
            return null;
        }
    }

    @Override
    public FrontPageListResponse getFrontPagesAd(Integer type) throws L99IllegalParamsException, L99IllegalDataException,
            L99NetworkException, JSONException {
        ZSetOperations<String, String> zso = srt.opsForZSet();

        List<FrontPage> frontPageList = new ArrayList<FrontPage>();
        String systemKey = getCacheId(FRONTPAGE_AD_SYSTEM);

        String userKey = getCacheId(FRONTPAGE_AD_USER);

        Set<String> systemStrIds = zso.rangeByScore(systemKey, 0, Double.MAX_VALUE, 0, FRONTPAGE_AD_SYSTEM_LIMIT);
        Set<String> userStrIds = zso.rangeByScore(userKey, 0, Double.MAX_VALUE, 0, FRONTPAGE_AD_USER_LIMIT);

        List<Long> ids = new ArrayList<Long>();
        for (String id : systemStrIds) {
            ids.add(Long.valueOf(id));
        }

        for (String id : userStrIds) {
            ids.add(Long.valueOf(id));
        }

        Map<Long, FrontPage> systemMap = frontPageAdDao.findByIds(ids);

        for (Long id : ids) {
            frontPageList.add(systemMap.get(id));
        }
        // 查询官方消息
        String messageKey = getCacheId(FRONTPAGE_CONTENT_MESSAGE);
        List<FrontPage> frontPageMessageList = memcacheService.get(messageKey);
        if (frontPageMessageList == null || frontPageMessageList.size() == 0) {
            frontPageMessageList = frontPageDao.findFrontPages(null, null, FRONTPAGE_CONTENT_MESSAGE_LIMIT,
                    ContentConstants.FrontPage.TYPE_CONTENT_MESSAGE);
            memcacheService.set(messageKey, frontPageMessageList);
        }
        FrontPageListResponse frontPageListResponse = getFrontPageListResponse(frontPageList, null);
        List<FrontPageResponse> messages = new ArrayList<FrontPageResponse>();
        for (FrontPage frontPage : frontPageMessageList) {
            FrontPageResponse frontPageResponse = getFrontPageResponse(frontPage);
            messages.add(frontPageResponse);
        }
        frontPageListResponse.setMessages(messages);
        return frontPageListResponse;
    }

    @Override
    public FrontPageListResponse findFrontPagesInner(Long startId, Long endId, Integer limit, Integer type, Integer status,
                                                     Integer skip) throws L99IllegalDataException, L99IllegalParamsException, L99NetworkException, JSONException {
        List<FrontPage> frontPageList;
        int count;
        if (type > 0) {
            frontPageList = frontPageDao.findFrontPagesInner(startId, endId, limit, type, status, skip);
            if (endId != null) {
                Collections.reverse(frontPageList);
            }
            count = frontPageDao.findFrontPagesCount(type, status);
        } else {
            frontPageList = frontPageAdDao.findFrontPagesInner(startId, endId, limit, type, status, skip);
            if (endId != null) {
                Collections.reverse(frontPageList);
            }
            count = frontPageAdDao.findFrontPagesCount(type, status);
        }
        FrontPageListResponse frontPageListResponse = getFrontPageListResponse(frontPageList, null);
        List<FrontPageResponse> frontPageResponseList = frontPageListResponse.getFrontpages();
        ZSetOperations<String, String> zso = srt.opsForZSet();
        // 对象是否这队列中
        if (type > 0) {
            for (FrontPageResponse frontPageResponse : frontPageResponseList) {
                String key1 = getCacheId(FRONTPAGE_CONTENT_ALL);
                Double score1 = zso.score(key1, frontPageResponse.getF_id() + "");
                if (score1 == null) {
                    frontPageResponse.setQueue(false);
                } else {
                    frontPageResponse.setQueue(true);
                }
            }
        } else {
            for (FrontPageResponse frontPageResponse : frontPageResponseList) {
                String key2 = getCacheId(FRONTPAGE_AD_SYSTEM);
                String key3 = getCacheId(FRONTPAGE_AD_USER);
                Double score2 = zso.score(key2, frontPageResponse.getF_id() + "");
                Double score3 = zso.score(key3, frontPageResponse.getF_id() + "");
                if (score2 == null && score3 == null) {
                    frontPageResponse.setQueue(false);
                } else {
                    frontPageResponse.setQueue(true);
                }
            }
        }
        frontPageListResponse.setNumber(count);
        return frontPageListResponse;
    }

    @Override
    public void editFrontPagesInner(Long id, Integer status, Integer type, Long contestId, Integer contestType, String desc,
                                    String path, String link, Long contentId, String title, Long innnerContestId, Integer innnerContestType,
                                    Integer oper) throws L99IllegalParamsException, L99IllegalDataException {
        boolean flag;
        if (oper == ContentConstants.FrontPageStatus.OPER_STATUS) {
            if (status != null && status == ContentConstants.FrontPageStatus.NOPASS) {
                // === start ==== 删除赛事分析（如果有）
                FrontPage frontPage;
                if (type > 0) {
                    frontPage = frontPageDao.findById(id);
                } else {
                    frontPage = frontPageAdDao.findById(id);
                }
                FrontPageContentResponse frontPageContentResponse = new Gson().fromJson(frontPage.getContent(),
                        FrontPageContentResponse.class);
                Long contestNewsId = frontPageContentResponse.getContest_news_id();
                if (contestNewsId != null) {
                    contestNewsService.edit(contestNewsId, null, null, null, null, null, null, -1);
                }
                // === end ==== 删除赛事分析（如果有）

                ZSetOperations<String, String> zso = srt.opsForZSet();
                if (type > 0) {
                    String key1 = getCacheId(FRONTPAGE_CONTENT_ALL);
                    String key2 = getCacheId(FRONTPAGE_CONTENT_NEWS_ALL);
                    zso.remove(key1, id + "");
                    zso.remove(key2, id + "");
                } else {
                    // 不做判断，有则删除
                    String key2 = getCacheId(FRONTPAGE_AD_SYSTEM);
                    String key3 = getCacheId(FRONTPAGE_AD_USER);
                    zso.remove(key2, id + "");
                    zso.remove(key3, id + "");
                }
            }
            if (type > 0) {
                flag = frontPageDao.editFrontPagesInner(id, status, null, null, null, null);
            } else {
                flag = frontPageAdDao.editFrontPagesInner(id, status);
            }
            if (!flag) {
                throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
            }
            if (status != null && status == ContentConstants.FrontPageStatus.NOPASS) {
                FrontPage frontPage = frontPageDao.findById(id);
                friendCircleService.deleteInnerContentByid(frontPage.getContentId());
            }
        } else {
            if (type > 0) {
                FrontPage frontPage = frontPageDao.findById(id);
                if (frontPage != null) {
                    String content = frontPage.getContent();
                    FrontPageContentResponse frontPageContentResponse = new Gson().fromJson(content,
                            FrontPageContentResponse.class);
                    if (contestId != null) {
                        frontPageContentResponse.setContestId(contestId);
                    }
                    if (contestType != null) {
                        frontPageContentResponse.setContestType(contestType);
                    }
                    if (desc != null) {
                        frontPageContentResponse.setDesc(desc);
                    }
                    if (path != null) {
                        frontPageContentResponse.setImage(path);
                    }
                    if (link != null) {
                        frontPageContentResponse.setLink(link);
                    }
                    if (title != null) {
                        frontPageContentResponse.setTitle(title);
                    }

                    flag = frontPageDao.editFrontPagesInner(id, null, JsonUtils.toJsonString(frontPageContentResponse),
                            contentId, innnerContestId, innnerContestType);
                } else {
                    throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
                }
            } else {
                flag = frontPageAdDao.editFrontPagesInner(id, status);
            }
            if (!flag) {
                throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
            }
        }

    }

    @Override
    public boolean deleteFrontPagesByContentId(Long contentId, Integer type) throws L99IllegalParamsException {
        FrontPage frontPage;
        boolean flag;
        if (type > 0) {
            frontPage = frontPageDao.findByContentId(contentId);
            if (frontPage == null) {
                throw new L99IllegalParamsException(MsgCode.ContentMsg.CODE_CONTENT_NOT_FOUND,
                        MsgCode.ContentMsg.KEY_CONTENT_NOT_FOUND);
            }
            long id = frontPage.getId();
            flag = frontPageDao.editFrontPagesInner(id, ContentConstants.FrontPageStatus.NOPASS, null, null, null, null);
            if (flag) {
                ZSetOperations<String, String> zso = srt.opsForZSet();
                String key1 = getCacheId(FRONTPAGE_CONTENT_ALL);
                String key2 = getCacheId(FRONTPAGE_CONTENT_NEWS_ALL);
                zso.remove(key1, id + "");
                zso.remove(key2, id + "");
            }
        } else {
            frontPage = frontPageAdDao.findByContentId(contentId);
            if (frontPage == null) {
                throw new L99IllegalParamsException(MsgCode.ContentMsg.CODE_CONTENT_NOT_FOUND,
                        MsgCode.ContentMsg.KEY_CONTENT_NOT_FOUND);
            }
            long id = frontPage.getId();
            flag = frontPageAdDao.editFrontPagesInner(id, ContentConstants.FrontPageStatus.NOPASS);
            if (flag) {
                // 不做判断，有则删除
                ZSetOperations<String, String> zso = srt.opsForZSet();
                String key2 = getCacheId(FRONTPAGE_AD_SYSTEM);
                String key3 = getCacheId(FRONTPAGE_AD_USER);
                zso.remove(key2, id + "");
                zso.remove(key3, id + "");
            }
        }
        return flag;
    }

    @Override
    public void operateQueue(Long fId, Integer status, Integer type) {
        ZSetOperations<String, String> zso = srt.opsForZSet();
        if (type > 0) {
            // 头版内容区
            String allContentKey = getCacheId(FRONTPAGE_CONTENT_ALL);
            String allContentNewsKey = getCacheId(FRONTPAGE_CONTENT_NEWS_ALL);
            if (status == ContentConstants.FrontPageStatus.INSERT_QUEUE) {
                // 插入队列
                // 验证是否在redis里面
                Double score = zso.score(allContentKey, fId + "");
                Double score2 = zso.score(allContentNewsKey, fId + "");
                if (score == null) {
                    // 控制长度
                    long allContentKeyCount = zso.zCard(allContentKey);
                    if (allContentKeyCount == FRONTPAGE_CONTENT_LIMIT) {
                        // 随机删除
                        long random = (long) Math.ceil(Math.random() * FRONTPAGE_CONTENT_LIMIT);
                        zso.removeRange(allContentKey, random, random);
                    }
                    // id添加到redis里面
                    zso.add(allContentKey, fId + "", fId);
                }
                if (score2 == null) {
                    // 控制长度
                    long count = zso.zCard(allContentNewsKey);
                    if (count == FRONTPAGE_CONTENT_NEWS_LIMIT) {
                        // 随机删除
                        long random = (long) Math.ceil(Math.random() * FRONTPAGE_CONTENT_NEWS_LIMIT);
                        zso.removeRange(allContentNewsKey, random, random);
                    }
                    // id添加到redis里面
                    zso.add(allContentKey, fId + "", fId);
                }
            } else if (status == ContentConstants.FrontPageStatus.REMOVE_QUEUE) {
                // 移出队列
                zso.remove(allContentKey, fId + "");
                zso.remove(allContentNewsKey, fId + "");
            } else if (status == ContentConstants.FrontPageStatus.INSERT_TOP) {
                // 置顶
                // 控制长度
                long allContentKeyCount = zso.zCard(allContentKey);
                if (allContentKeyCount == FRONTPAGE_CONTENT_LIMIT) {
                    // 随机删除
                    long random = (long) Math.ceil(Math.random() * FRONTPAGE_CONTENT_LIMIT);
                    zso.removeRange(allContentKey, random, random);
                }
                // id添加到redis里面
                zso.add(allContentKey, fId + "", fId + 2000);

                if (type == ContentConstants.FrontPage.TYPE_CONTENT_RECOMMEND_ZHIXUN
                        || type == ContentConstants.FrontPage.TYPE_CONTENT_RECOMMEND_BISAI
                        || type == ContentConstants.FrontPage.TYPE_CONTENT_RECOMMEND_WANGYE) {
                    long count = zso.zCard(allContentNewsKey);
                    if (count == FRONTPAGE_CONTENT_NEWS_LIMIT) {
                        // 随机删除
                        long random = (long) Math.ceil(Math.random() * FRONTPAGE_CONTENT_LIMIT);
                        zso.removeRange(allContentNewsKey, random, random);
                    }
                    zso.add(allContentNewsKey, fId + "", fId + 2000);
                }
            }
        } else {
            // 头版广告区
            if (type == ContentConstants.FrontPage.TYPE_AD_REASON || type == ContentConstants.FrontPage.TYPE_AD_REMAKES) {
                String userKey = getCacheId(FRONTPAGE_AD_USER);
                if (status == ContentConstants.FrontPageStatus.INSERT_QUEUE) {
                    // 插入队列
                    // 验证是否在redis里面
                    Double score = zso.score(userKey, fId + "");
                    if (score == null) {
                        // 控制长度
                        long allContentKeyCount = zso.zCard(userKey);
                        if (allContentKeyCount == FRONTPAGE_AD_USER_LIMIT) {
                            // 删除id最小的
                            zso.removeRange(userKey, 0, 0);
                        }
                        // id添加到redis里面
                        zso.add(userKey, fId + "", fId);
                    }
                } else if (status == ContentConstants.FrontPageStatus.REMOVE_QUEUE) {
                    // 移出队列
                    zso.remove(userKey, fId + "");
                }
            } else if (type == ContentConstants.FrontPage.TYPE_AD_RECOMMEND_ZHIXUN
                    || type == ContentConstants.FrontPage.TYPE_AD_RECOMMEND_BISAI
                    || type == ContentConstants.FrontPage.TYPE_AD_RECOMMEND_WANGYE) {
                String systemKey = getCacheId(FRONTPAGE_AD_SYSTEM);
                if (status == ContentConstants.FrontPageStatus.INSERT_QUEUE) {
                    // 插入队列
                    // 验证是否在redis里面
                    Double score = zso.score(systemKey, fId + "");
                    if (score == null) {
                        // 控制长度
                        long allContentKeyCount = zso.zCard(systemKey);
                        if (allContentKeyCount == FRONTPAGE_AD_SYSTEM_LIMIT) {
                            // 删除id最小的
                            zso.removeRange(systemKey, 0, 0);
                        }
                        // id添加到redis里面
                        zso.add(systemKey, fId + "", fId);
                    }
                } else if (status == ContentConstants.FrontPageStatus.REMOVE_QUEUE) {
                    // 移出队列
                    zso.remove(systemKey, fId + "");
                }
            }
        }
    }

    @Override
    public FrontPageResponse findFrontPage(Long id, Integer type) throws L99IllegalDataException, L99IllegalParamsException,
            L99NetworkException, JSONException {
        FrontPage frontPage;
        if (type > 0) {
            frontPage = frontPageDao.findById(id);
        } else {
            frontPage = frontPageAdDao.findById(id);
        }
        return getFrontPageResponse(frontPage);
    }

    @Override
    public void addFrontPageFromCircle(Long circleId, int type) throws L99NetworkException, L99IllegalParamsException, JSONException, L99IllegalDataException {

        FrontPage frontPage = frontPageDao.findByContentId(circleId);
        //已经存在
        if (frontPage != null) {
            throw new L99IllegalDataException(MsgCode.ContentMsg.CODE_CONTENT_FOUND, MsgCode.ContentMsg.KEY_CONTENT_FOUND);
        }
        FriendCircleResponse friendCircleResponse = friendCircleService.getSingleContentResponse(circleId);
        if (friendCircleResponse == null) {
            throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
        }
        String[] images = friendCircleResponse.getImages();
        StringBuilder stringBuilder = new StringBuilder();
        if (images != null) {
            if (images.length > 0) {
                for (String image : images) {
                    stringBuilder.append(image).append(",");
                }
            } else {
                stringBuilder.append(",");
            }
        } else {
            stringBuilder.append(",");
        }
        String image = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
        Integer contestType = friendCircleResponse.getContest_type();
        if (type == ContentConstants.FrontPage.TYPE_CONTENT_REASON && contestType == null) {
            throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
        }
        this.addFrontPage(friendCircleResponse.getContest_id(), null, friendCircleResponse.getContest_type(),
                friendCircleResponse.getUser().getUser_id(), friendCircleResponse.getContent(), image,
                null, type, circleId, null, null, null);
    }

}
