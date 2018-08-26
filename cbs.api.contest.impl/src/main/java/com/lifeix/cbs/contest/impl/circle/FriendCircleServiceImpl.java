package com.lifeix.cbs.contest.impl.circle;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.achieve.bean.bo.ContentAchieveBO;
import com.lifeix.cbs.achieve.common.constant.BehaviorConstants;
import com.lifeix.cbs.achieve.service.AchieveService;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.BroadcastMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.ContentMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.UserMsg;
import com.lifeix.cbs.api.common.mission.Mission;
import com.lifeix.cbs.api.common.solr.CbsSolrUtil;
import com.lifeix.cbs.api.common.util.*;
import com.lifeix.cbs.api.common.util.BetConstants.BetResultStatus;
import com.lifeix.cbs.api.common.util.BetConstants.BetStatus;
import com.lifeix.cbs.api.common.util.ContentConstants.FrontPage;
import com.lifeix.cbs.api.common.util.ContentConstants.PostType;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.ContestConstants.FriendType;
import com.lifeix.cbs.api.service.misson.MissionUserService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.relationship.RelationshipService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.api.util.UserConstants;
import com.lifeix.cbs.content.service.frontpage.FrontPageService;
import com.lifeix.cbs.contest.bean.circle.FriendCircleContestResponse;
import com.lifeix.cbs.contest.bean.circle.FriendCircleListResponse;
import com.lifeix.cbs.contest.bean.circle.FriendCircleResponse;
import com.lifeix.cbs.contest.bean.odds.OddsDssResponse;
import com.lifeix.cbs.contest.bean.yy.YyOptionResponse;
import com.lifeix.cbs.contest.dao.bb.*;
import com.lifeix.cbs.contest.dao.circle.FriendCircleDao;
import com.lifeix.cbs.contest.dao.circle.UserFriendCircleDao;
import com.lifeix.cbs.contest.dao.fb.*;
import com.lifeix.cbs.contest.dao.yy.YyBetDao;
import com.lifeix.cbs.contest.dao.yy.YyContestDao;
import com.lifeix.cbs.contest.dto.bb.BbContest;
import com.lifeix.cbs.contest.dto.bet.BetJc;
import com.lifeix.cbs.contest.dto.bet.BetOddeven;
import com.lifeix.cbs.contest.dto.bet.BetOp;
import com.lifeix.cbs.contest.dto.bet.BetSize;
import com.lifeix.cbs.contest.dto.circle.FriendCircle;
import com.lifeix.cbs.contest.dto.circle.UserFriendCircle;
import com.lifeix.cbs.contest.dto.fb.FbContest;
import com.lifeix.cbs.contest.dto.odds.OddsJc;
import com.lifeix.cbs.contest.dto.odds.OddsOp;
import com.lifeix.cbs.contest.dto.odds.OddsSize;
import com.lifeix.cbs.contest.dto.yy.YyBet;
import com.lifeix.cbs.contest.dto.yy.YyContest;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.cirle.FriendCircleCommService;
import com.lifeix.cbs.contest.service.cirle.FriendCircleService;
import com.lifeix.cbs.contest.service.comment.CommentService;
import com.lifeix.cbs.contest.util.transform.BbOddsDssDateUtil;
import com.lifeix.cbs.contest.util.transform.FbOddsDssDateUtil;
import com.lifeix.cbs.contest.util.transform.YyContestTransformUtil;
import com.lifeix.common.utils.RegexUtil;
import com.lifeix.common.utils.StringUtil;
import com.lifeix.exception.service.*;
import com.lifeix.user.beans.CustomResponse;
import lifeix.framwork.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("friendCircleService")
public class FriendCircleServiceImpl extends ImplSupport implements FriendCircleService {

    @Autowired
    RelationshipService relationshipService;

    @Autowired
    UserFriendCircleDao userFriendCircleDao;

    @Autowired
    FriendCircleDao friendCircleDao;

    @Autowired
    CbsUserService cbsUserService;

    @Autowired
    FbContestDao fbContestDao;

    @Autowired
    BbContestDao bbContestDao;

    @Autowired
    private FbBetOpDao fbBetOpDao;

    @Autowired
    private FbBetJcDao fbBetJcDao;

    @Autowired
    private BbBetOpDao bbBetOpDao;

    @Autowired
    private BbBetJcDao bbBetJcDao;

    @Autowired
    YyContestDao yyContestDao;

    @Autowired
    YyBetDao yyBetDao;

    @Autowired
    MoneyService moneyService;

    @Autowired
    FrontPageService frontPageService;

    @Autowired
    FriendCircleCommService friendCircleCommService;

    @Autowired
    private CommentService commentService;

    @Autowired
    protected AchieveService achieveService;

    @Autowired
    private BbOddsJcDao bbJcDao;

    @Autowired
    private BbOddsOpDao bbOpDao;

    @Autowired
    private BbOddsSizeDao bbOddsSizeDao;

    @Autowired
    private FbOddsJcDao fbJcDao;

    @Autowired
    private FbOddsOpDao fbOpDao;

    @Autowired
    private FbOddsSizeDao fbOddsSizeDao;

    @Autowired
    private FbBetSizeDao fbBetSizeDao;

    @Autowired
    private FbBetOddevenDao fbBetOddevenDao;

    @Autowired
    private BbBetSizeDao bbBetSizeDao;

    @Autowired
    private BbBetOddevenDao bbBetOddevenDao;

    @Autowired
    private MissionUserService missionUserService;

    private static final int SUM_INSERT = 1000;

    @Override
    public FriendCircleListResponse getFriendsCircle(Long accountId, String client, Integer friendType, Integer page,
	    Integer limit) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(accountId);

	if (page == null) {
	    page = 1;
	}
	FriendCircleListResponse repList = new FriendCircleListResponse();
	List<FriendCircleResponse> friendCircles = new ArrayList<FriendCircleResponse>();
	// 活得用户关注用户id
	List<Long> targetIds = relationshipService.getAllMeAttention(accountId);
	// targetIds.add(516L);
	if (targetIds == null || targetIds.isEmpty()) {
	    targetIds = new ArrayList<Long>();

	}
	targetIds.add(accountId);
	// 根据关注用户ids获得猜友圈中间表对应的猜友圈circles
	List<Long> userCircleIds = userFriendCircleDao.getUserFriendCircleIds(accountId, targetIds, friendType, page, limit);

	if (userCircleIds == null || userCircleIds.isEmpty()) {
	    return repList;
	}
	if (userCircleIds.size() > limit) {
	    userCircleIds = userCircleIds.subList(0, limit);
	    repList.setStartId((long) page + 1);
	} else {
	    repList.setStartId(-1L);
	}

	List<FriendCircle> circles = friendCircleDao.getFriendCircleByIds(userCircleIds, client, true);

	if (circles == null || circles.isEmpty()) {
	    return repList;
	}

	// 活得所有用户ids对应的用户对象
	Map<Long, CbsUserResponse> userMap = cbsUserService.findCsAccountMapByIds(targetIds);
	FriendCircleResponse friendCircle = null;
	// 根据circles活得猜友圈记录
	for (FriendCircle cs : circles) {
	    // 获得用户信息
	    CbsUserResponse user = userMap.get(cs.getUserId());
	    friendCircle = getFriendCircleInfo(user, friendCircle, cs);
	    friendCircles.add(friendCircle);
	}

	DataResponse<Object> dr = friendCircleCommService.getUnreadCounts(accountId, null);
	repList.setContents(friendCircles);
	repList.setMsgNum(Integer.parseInt(dr.getData().toString()));
	return repList;
    }

    private int getFriendType(int type, boolean hasContent) {
	int friendType = 0;
	if (type == 1) {
	    friendType = FriendType.CONTEST;
	    if (hasContent) {
		friendType = FriendType.REASON;
	    }
	} else if (type == 0 && hasContent) {
	    friendType = FriendType.TUCAO;
	}

	return friendType;
    }

    @Override
    public FriendCircleListResponse getInnerMyFriendsCircle(Long userId, String searchKey, Long startId, Long endId,
	    Integer limit, Integer skip) throws L99IllegalParamsException, L99IllegalDataException, NumberFormatException,
	    L99NetworkException, JSONException {
	if (com.alibaba.dubbo.common.utils.StringUtils.isNotEmpty(searchKey)) {
	    if (searchKey.matches("\\d+")) {
		userId = Long.parseLong(searchKey);
	    }
	}

	FriendCircleListResponse rep = new FriendCircleListResponse();
	List<FriendCircle> circles = new ArrayList<FriendCircle>();
	List<FriendCircleResponse> friendCircles = new ArrayList<FriendCircleResponse>();
	rep.setContents(friendCircles);
	circles.addAll(friendCircleDao.getInnerCircles(userId, searchKey, startId, endId, limit, skip));
	rep.setNumber(friendCircleDao.getInnerCirclesNum(userId, searchKey));
	if (startId != null) {
	    Collections.reverse(circles);
	}
	// 根据circles活得猜友圈记录
	for (FriendCircle cs : circles) {
	    FriendCircleResponse friendCircle = new FriendCircleResponse();
	    // 活得用户信息
	    friendCircle.setUser(cbsUserService.getCbsUserByUserId(cs.getUserId(), false));// 设置用户对象
	    friendCircle.setClient(cs.getClient());
	    friendCircle.setContent(cs.getContent());
	    friendCircle.setFriend_circle_id(cs.getId());
	    friendCircle.setType(cs.getType());
	    int friendTypeTmp = getFriendType(cs.getType(), cs.getHasContent());
	    friendCircle.setFriendType(friendTypeTmp);
	    friendCircle.setCreate_time(CbsTimeUtils.getUtcTimeForDate(cs.getCreateTime()));
	    friendCircle.setData_flag(cs.getDeleteFlag() ? 1 : 0);
	    friendCircle.setCoupon(cs.getCoupon());

	    String params = cs.getParams();
	    friendCircle.setHasContent(cs.getHasContent());
	    if (StringUtils.isNotBlank(params)) {
		try {
		    JSONObject obj = new JSONObject(params);
		    // fillContentContest(cs, friendCircle, obj);

		    if (obj.has("images")) {
			String photoIdStr = obj.getString("images");
			if (com.alibaba.dubbo.common.utils.StringUtils.isNotEmpty(photoIdStr)) {
			    friendCircle.setImages(photoIdStr.split(","));
			}

		    }
		} catch (Exception e) {
		    LOG.error(e.getMessage(), e);
		}
	    }

	    friendCircles.add(friendCircle);
	}

	if (!circles.isEmpty()) {
	    rep.setStartId(circles.get(0).getId());
	    rep.setEndId(circles.get(circles.size() - 1).getId());
	}

	return rep;
    }

    @Override
    public FriendCircleListResponse getInnerSolrMyFriendsCircle(Long userId, String searchKey, Long startId, Long endId,
	    Integer limit, Integer skip) throws L99IllegalParamsException, L99IllegalDataException, NumberFormatException,
	    L99NetworkException, JSONException {

	FriendCircleListResponse rep = new FriendCircleListResponse();
	List<FriendCircleResponse> friendCircles = new ArrayList<FriendCircleResponse>();
	String jsonStr = CbsSolrUtil.getInstance().selectInnerCircles(null, getMutilSeachKey(searchKey), startId, endId,
	        limit, skip, false);

	String jsonCount = CbsSolrUtil.getInstance().selectInnerCircles(null, getMutilSeachKey(searchKey), startId, endId,
	        limit, skip, true);

	JSONObject count = new JSONObject(jsonCount);

	JSONObject ret = new JSONObject(jsonStr);
	int status = ret.getJSONObject("responseHeader").getInt("status");
	if (status == 0) {
	    JSONObject repJson = ret.getJSONObject("response");
	    rep = JsonUtils.jsonString2Bean(repJson.toString(), FriendCircleListResponse.class);
	    JSONObject countJson = count.getJSONObject("response");
	    rep.setNumber(countJson.optInt("numFound"));
	} else {
	    JSONObject errorRet = ret.getJSONObject("error");
	    throw new L99NetworkException(errorRet.optString("code"), errorRet.optString("msg"));
	}

	// circles.addAll(friendCircleDao.getInnerCircles(userId, searchKey,
	// startId, endId, limit));

	if (startId != null) {
	    Collections.reverse(rep.getDocs());
	}

	for (FriendCircleResponse cs : rep.getDocs()) {

	    FriendCircleResponse friendCircle = new FriendCircleResponse();
	    // 活得用户信息
	    friendCircle.setUser(cbsUserService.getCbsUserByUserId(cs.getUser_id(), false));// 设置用户对象
	    friendCircle.setClient(cs.getClient());
	    friendCircle.setContent(cs.getContent());
	    friendCircle.setFriend_circle_id(cs.getFriend_circle_id());
	    friendCircle.setType(cs.getType());
	    int friendTypeTmp = getFriendType(cs.getType(), cs.isHas_content());
	    friendCircle.setHasContent(cs.isHas_content());

	    friendCircle.setFriendType(friendTypeTmp);
	    friendCircle.setCreate_time(cs.getCreate_time());
	    friendCircle.setData_flag(cs.getData_flag());
	    friendCircle.setCoupon(cs.getCoupon());

	    String params = cs.getParams();
	    friendCircle.setHasContent(cs.isHas_content());
	    if (StringUtils.isNotBlank(params)) {
		try {
		    JSONObject obj = new JSONObject(params);
		    // fillContentContest(cs, friendCircle, obj);

		    if (obj.has("images")) {
			String photoIdStr = obj.getString("images");
			if (com.alibaba.dubbo.common.utils.StringUtils.isNotEmpty(photoIdStr)) {
			    friendCircle.setImages(photoIdStr.split(","));
			}

		    }
		} catch (Exception e) {
		    LOG.error(e.getMessage(), e);
		}
	    }

	    friendCircles.add(friendCircle);

	}

	rep.setContents(friendCircles);
	if (!friendCircles.isEmpty()) {
	    rep.setStartId(friendCircles.get(0).getFriend_circle_id());
	    rep.setEndId(friendCircles.get(friendCircles.size() - 1).getFriend_circle_id());
	}
	return rep;
    }

    private String getMutilSeachKey(String key) {
	if (StringUtils.isEmpty(key)) {
	    return null;
	}
	StringBuffer sb = new StringBuffer();
	if (com.alibaba.dubbo.common.utils.StringUtils.isNotEmpty(key)) {
	    if (key.matches("\\d+")) {
		sb.append("user_id:" + key + " OR content:" + key);
	    } else {
		sb.append("content:" + key);
	    }
	}
	return sb.toString();
    }

    @Override
    public FriendCircleResponse getFollowInfo(Long curUserId, Long friendCircleId) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(curUserId);
	ParamemeterAssert.assertDataNotNull(friendCircleId);

	FriendCircleResponse friendCircle = new FriendCircleResponse();

	FriendCircle fc = friendCircleDao.findById(friendCircleId);
	CbsUserResponse user = cbsUserService.selectById(curUserId);
	return getFriendCircleInfo(user, friendCircle, fc);
    }

    public FriendCircleResponse getFriendCircleInfo(CbsUserResponse user, FriendCircleResponse friendCircle, FriendCircle cs) {
	friendCircle = new FriendCircleResponse();
	friendCircle.setUser_id(cs.getUserId());
	friendCircle.setUser(user);// 设置用户对象
	friendCircle.setClient(cs.getClient());
	friendCircle.setContent(cs.getContent());
	friendCircle.setFriend_circle_id(cs.getId());
	friendCircle.setType(cs.getType());
	int friendTypeTmp = getFriendType(cs.getType(), cs.getHasContent());
	friendCircle.setFriendType(friendTypeTmp);
	friendCircle.setCreate_time(CbsTimeUtils.getUtcTimeForDate(cs.getCreateTime()));
	friendCircle.setData_flag(cs.getDeleteFlag() ? 1 : 0);
	friendCircle.setCoupon(cs.getCoupon());

	String params = cs.getParams();
	friendCircle.setHasContent(cs.getHasContent());
	if (StringUtils.isNotBlank(params)) {
	    try {
		JSONObject obj = new JSONObject(params);
		fillContentContest(cs, friendCircle, obj);

		if (obj.has("images")) {
		    String photoIdStr = obj.getString("images");
		    if (com.alibaba.dubbo.common.utils.StringUtils.isNotEmpty(photoIdStr)) {
			friendCircle.setImages(photoIdStr.split(","));
		    }

		}
	    } catch (Exception e) {
		LOG.error(e.getMessage(), e);
	    }
	}

	DataResponse<Object> dr = friendCircleCommService.getSingleCommentCounts(cs.getId(), user.getUser_id());
	if (dr.getData() != null) {
	    friendCircle.setCommNum(Integer.parseInt(dr.getData().toString()));
	}
	return friendCircle;
    }

    @Override
	public FriendCircleListResponse getMyFriendsCircle(Long userId, Long friendCircleId, String client, Integer friendType,
													   Integer page, Integer limit) throws L99IllegalParamsException {

		if (page == null) {
			page = 1;
		}
		FriendCircleListResponse repList = new FriendCircleListResponse();
		List<FriendCircleResponse> friendCircles = new ArrayList<>();
		List<FriendCircle> circles = new ArrayList<>();
		if (userId != null) {
			circles = friendCircleDao.getMyCircleByIds(userId, client, friendType, page, limit);
		} else if (friendCircleId != null) {
			FriendCircle fc = friendCircleDao.findById(friendCircleId);
			userId = fc.getUserId();
			circles.add(fc);
		} else {
			throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
		}

		if (circles == null || circles.isEmpty()) {
			return repList;
		}

		CbsUserResponse user = cbsUserService.selectById(userId);
		FriendCircleResponse friendCircle = null;
		//查找投注理由数量
		Set<String> reasonSet = new HashSet<>();
		for (FriendCircle cs : circles) {
			int contestTypeTmp = cs.getContestType();
			if (contestTypeTmp == 1 || contestTypeTmp == 0){
				reasonSet.add(contestTypeTmp + ":" + cs.getContestId());
			}
		}
		Map<String, String> map = friendCircleDao.getReasonUser(reasonSet);
		// 根据circles活得猜友圈记录
		for (FriendCircle cs : circles) {
			friendCircle = getFriendCircleInfo(user, friendCircle, cs);
			String users = map.get(friendCircle.getContest_type() +":" + friendCircle.getContest_id());
			int reasonNum = 0 ;
			if (StringUtils.isNotBlank(users)){
				String[] userStrs = users.split(",");
				for (String userStr : userStrs) {
					if (!userStr.equals(userId+"")){
						reasonNum ++ ;
					}
				}
			}
			friendCircle.setReason_num(reasonNum);
			friendCircles.add(friendCircle);
		}

		if (friendCircles.size() > limit) {
			friendCircles = friendCircles.subList(0, limit);
			repList.setStartId(Long.valueOf(page + 1));
		} else {
			repList.setStartId(-1L);
		}

		DataResponse<Object> dr = friendCircleCommService.getUnreadCounts(userId, null);
		repList.setContents(friendCircles);
		repList.setMsgNum(Integer.parseInt(dr.getData().toString()));

		repList.setContents(friendCircles);
		return repList;

	}

    @Override
    public FriendCircleListResponse getMyYayaCircle(Long userId, Long startId, int limit) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(userId);

	List<FriendCircle> circles = friendCircleDao.getMyYayaCircle(userId, startId, limit);

	CbsUserResponse user = cbsUserService.selectById(userId);

	List<FriendCircleResponse> friendCircles = new ArrayList<FriendCircleResponse>();
	FriendCircleResponse friendCircle = null;
	for (FriendCircle cs : circles) {
	    friendCircle = getFriendCircleInfo(user, friendCircle, cs);
	    friendCircles.add(friendCircle);
	    startId = cs.getId();
	}

	if (friendCircles.size() == 0 || friendCircles.size() < limit) { // 没有数据或数据不够返回-1
	    startId = -1L;
	}

	FriendCircleListResponse repList = new FriendCircleListResponse();
	repList.setContents(friendCircles);
	repList.setNumber(friendCircles.size());
	repList.setStartId(startId);

	return repList;

    }

    /**
     * 填充赛事内容信息
     * 
     * @param content
     * @param resp
     * @param obj
     */
    private void fillContentContest(FriendCircle content, FriendCircleResponse resp, JSONObject obj) {
	try {
	    FriendCircleContestResponse contest = new FriendCircleContestResponse();

	    // add by lhx start
	    resp.setContest_id(content.getContestId());
	    resp.setContest_type(content.getContestType());
	    // end
	    double curHandicap = 0;
	    double curOdds = 0;
	    boolean betFlag = true;
	    Long contestId = content.getContestId();
	    Long userId = resp.getUser() != null ? resp.getUser().getUser_id() : null;
	    long fcUserId = content.getUserId();
	    // 下单信息
	    if (obj.has("bet")) {
		JSONObject betObj = obj.getJSONObject("bet");

		// 比赛类型
		int contestType = content.getContestType();
		// 比赛id
		contest.setContestId(contestId);
		// 比赛类型
		contest.setType(contestType);
		// 比赛时间
		String time = betObj.optString("time", null);
		if (StringUtils.isNotBlank(time)) {
		    contest.setTime(time);
		}

		// 支持方
		int support = betObj.getInt("support");
		contest.setSupportId(support);

		// 下单金额
		double bet = betObj.optDouble("bet", 0);
		contest.setBet(bet);

		// 比赛结果
		int status = betObj.optInt("status");
		contest.setStatus(status);

		// 返回金额
		if (betObj.has("back")) {
		    double back = betObj.optDouble("back", 0);
		    if (BetResultStatus.WIN == status) {
			// 下单获胜赢钱
			contest.setBack(back);
		    } else if (BetResultStatus.LOSS == status) {
			// 下单失败输钱
			contest.setBack(CommerceMath.sub(back, bet));
		    } else if (BetResultStatus.DRAW == status) {
			contest.setBack(back);
		    }
		}

		// 普通赛事
		if (contestType == ContestType.FOOTBALL || contestType == ContestType.BASKETBALL) {
		    // 联赛名
		    contest.setCn(betObj.optString("cName", null));

		    // 联赛主色调
		    contest.setColor(betObj.optString("color", null));

		    // 主队
		    contest.setHn(betObj.optString("hn", null));
		    contest.setHl(betObj.optString("hl", null));
		    if (betObj.has("hs")) { // 主队分数
			contest.setHs(betObj.getInt("hs"));
		    }

		    // 客队
		    contest.setAn(betObj.optString("an", null));
		    contest.setLongbi(betObj.optBoolean("isLongbi", false));
		    contest.setAl(betObj.optString("al", null));
		    if (betObj.has("as")) { // 客队分数
			contest.setAs(betObj.getInt("as"));
		    }

		    // 玩法id
		    int pId = betObj.getInt("playId");
		    contest.setPlayId(pId);
		    PlayType play = PlayType.findPlayTypeByIdAndType(pId, contestType);
		    if (play != null) {
			contest.setPlay(play.getName());
		    }

		    // 盘口
		    contest.setHandicap(betObj.optDouble("handicap", 0.0D));

		    if (contestType == ContestType.FOOTBALL) { // 足球比赛

			if (PlayType.FB_SPF.getId() == pId) {
			    // 查询最新赔率及盘口
			    OddsOp oddsOp = fbOpDao.selectByContest(contestId);
			    contest.setPlay(PlayType.FB_SPF.getName());
			    // 判断是否已下单
			    BetOp betOp = fbBetOpDao.selectByBet(contestId, userId, support);
			    if (betOp == null) {
				betFlag = false;
			    }

			    if (BetStatus.HOME == support) {
				contest.setSupport("主胜");
				contest.setOdds(betObj.getDouble("r1"));
				curOdds = oddsOp.getHomeRoi();
			    } else if (BetStatus.DRAW == support) {
				contest.setSupport("平局");
				contest.setOdds(betObj.getDouble("r2"));
				curOdds = oddsOp.getDrawRoi();
			    } else if (BetStatus.AWAY == support) {
				contest.setSupport("客胜");
				contest.setOdds(betObj.getDouble("r3"));
				curOdds = oddsOp.getAwayRoi();
			    }
			} else if (PlayType.FB_RQSPF.getId() == pId) {
			    // 查询最新赔率及盘口
			    OddsJc oddsJc = fbJcDao.selectByContest(contestId);
			    curHandicap = oddsJc.getHandicap();
			    contest.setPlay(PlayType.FB_RQSPF.getName());
			    // 判断是否已下单
			    BetJc betJc = fbBetJcDao.selectByBet(contestId, userId, support);
			    if (betJc == null) {
				betFlag = false;
			    }

			    if (BetStatus.HOME == support) {
				contest.setSupport("主胜");
				contest.setOdds(betObj.getDouble("r1"));
				curOdds = oddsJc.getHomeRoi();
			    } else if (BetStatus.DRAW == support) {
				contest.setSupport("平局");
				contest.setOdds(betObj.getDouble("r2"));
				curOdds = oddsJc.getDrawRoi();
			    } else if (BetStatus.AWAY == support) {
				contest.setSupport("客胜");
				contest.setOdds(betObj.getDouble("r3"));
				curOdds = oddsJc.getAwayRoi();
			    }
			} else if (PlayType.FB_SIZE.getId() == pId) {
			    // 查询最新赔率及盘口
			    OddsSize oddsSize = fbOddsSizeDao.selectByContest(contestId);
			    curHandicap = oddsSize.getHandicap();
			    contest.setPlay(PlayType.FB_SIZE.getName());
			    // 判断是否已下单
			    BetSize betSize = fbBetSizeDao.selectByBet(contestId, userId, support);
			    if (betSize == null) {
				betFlag = false;
			    }
			    if (BetStatus.HOME == support) {
				contest.setSupport("大于" + contest.getHandicap() + "球");
				contest.setOdds(betObj.getDouble("r1"));
				curOdds = oddsSize.getBigRoi();
			    } else if (BetStatus.AWAY == support) {
				contest.setSupport("小于" + contest.getHandicap() + "球");
				contest.setOdds(betObj.getDouble("r3"));
				curOdds = oddsSize.getTinyRoi();
			    }
			} else if (PlayType.FB_ODDEVEN.getId() == pId) {
			    // 查询最新赔率及盘口
			    OddsDssResponse oddsDssResponse = FbOddsDssDateUtil.getInstance().getDssResponse();
			    contest.setPlay(PlayType.FB_ODDEVEN.getName());
			    // 判断是否已下单
			    BetOddeven betOddeven = fbBetOddevenDao.selectByBet(contestId, userId, support);
			    if (betOddeven == null) {
				betFlag = false;
			    }

			    if (BetStatus.HOME == support) {
				contest.setSupport("单数");
				contest.setOdds(betObj.getDouble("r1"));
				curOdds = oddsDssResponse.getOdd_roi();
			    } else if (BetStatus.AWAY == support) {
				contest.setSupport("双数");
				contest.setOdds(betObj.getDouble("r3"));
				curOdds = oddsDssResponse.getEven_roi();
			    }
			}
		    } else if (contestType == ContestType.BASKETBALL) { // 篮球比赛

			if (PlayType.BB_SPF.getId() == pId) {
			    // 查询最新赔率及盘口
			    OddsOp oddsOp = bbOpDao.selectByContest(contestId);
			    contest.setPlay(PlayType.BB_SPF.getName());
			    // 判断是否已下单
			    BetOp betOp = bbBetOpDao.selectByBet(contestId, userId, support);
			    if (betOp == null) {
				betFlag = false;
			    }
			    if (BetStatus.HOME == support) {
				contest.setSupport("主胜");
				contest.setOdds(betObj.getDouble("r1"));
				curOdds = oddsOp.getHomeRoi();
			    } else if (BetStatus.AWAY == support) {
				contest.setSupport("客胜");
				contest.setOdds(betObj.getDouble("r3"));
				curOdds = oddsOp.getAwayRoi();
			    }
			} else if (PlayType.BB_JC.getId() == pId) {
			    // 查询最新赔率及盘口
			    OddsJc oddsJc = bbJcDao.selectByContest(contestId);
			    curHandicap = oddsJc.getHandicap();
			    contest.setPlay(PlayType.BB_JC.getName());
			    // 判断是否已下单
			    BetJc betJc = bbBetJcDao.selectByBet(contestId, userId, support);
			    if (betJc == null) {
				betFlag = false;
			    }
			    if (BetStatus.HOME == support) {
				contest.setSupport("主胜");
				contest.setOdds(betObj.getDouble("r1"));
				curOdds = oddsJc.getHomeRoi();
			    } else if (BetStatus.AWAY == support) {
				contest.setSupport("客胜");
				if (betObj.has("r3")) {
				    contest.setOdds(betObj.getDouble("r3"));
				}
				curOdds = oddsJc.getAwayRoi();
			    }

			} else if (PlayType.BB_SIZE.getId() == pId) {
			    // 查询最新赔率及盘口
			    OddsSize oddsSize = bbOddsSizeDao.selectByContest(contestId);
			    curHandicap = oddsSize.getHandicap();
			    contest.setPlay(PlayType.BB_SIZE.getName());
			    // 判断是否已下单
			    BetSize betSize = bbBetSizeDao.selectByBet(contestId, userId, support);
			    if (betSize == null) {
				betFlag = false;
			    }
			    if (BetStatus.HOME == support) {
				contest.setSupport("大于" + contest.getHandicap() + "分");
				contest.setOdds(betObj.getDouble("r1"));
				curOdds = oddsSize.getBigRoi();
			    } else if (BetStatus.AWAY == support) {
				contest.setSupport("小于" + contest.getHandicap() + "分");
				if (betObj.has("r3")) {
				    contest.setOdds(betObj.getDouble("r3"));
				}
				curOdds = oddsSize.getTinyRoi();
			    }
			} else if (PlayType.BB_ODDEVEN.getId() == pId) {
			    // 查询最新赔率及盘口
			    OddsDssResponse oddsDssResponse = BbOddsDssDateUtil.getInstance().getDssResponse();
			    contest.setPlay(PlayType.BB_ODDEVEN.getName());
			    // 判断是否已下单
			    BetOddeven betOddeven = bbBetOddevenDao.selectByBet(contestId, userId, support);
			    if (betOddeven == null) {
				betFlag = false;
			    }

			    if (BetStatus.HOME == support) {
				contest.setSupport("单数");
				contest.setOdds(betObj.getDouble("r1"));
				curOdds = oddsDssResponse.getOdd_roi();
			    } else if (BetStatus.AWAY == support) {
				contest.setSupport("双数");
				if (betObj.has("r3")) {
				    contest.setOdds(betObj.getDouble("r3"));
				}
				curOdds = oddsDssResponse.getEven_roi();
			    }
			}
		    }
		} else if (contestType == ContestType.YAYA) { // 押押赛事
		    contest.setPlay("押押");
		    contest.setSupport(String.valueOf(support));
		    contest.setOdds(betObj.optDouble("roi"));
		    contest.setTitle(betObj.optString("title"));
		    contest.setOption(betObj.optString("opn"));
		    contest.setLongbi(betObj.optBoolean("isLongbi", false));
		    YyContest yyContest = yyContestDao.selectById(contest.getContestId());
		    // 查询最新赔率及盘口
		    List<YyOptionResponse> options = YyContestTransformUtil.transformYyOption(yyContest.getOptions(), null);
		    // 获取押押选项
		    YyOptionResponse option = options.get(support - 1);
		    curOdds = option.getRoi();
		    // 判断是否已下单
		    YyBet yyBet = yyBetDao.selectByBet(contestId, userId, support);
		    if (yyBet == null) {
			betFlag = false;
		    }

		    if (betObj.has("winner")) {
			contest.setWinner(betObj.getInt("winner"));
		    }
		    if (betObj.has("opw")) {
			contest.setWinner_name(betObj.getString("opw"));
		    }
		}
		contest.setCurOdds(curOdds);
		contest.setCurHandicap(curHandicap);
		boolean followFlag = false;
		// 盘口、赔率有变动时followFlag设置为true
		if (contest.getCurHandicap().doubleValue() != (contest.getHandicap() == null ? 0.00d : contest.getHandicap()
		        .doubleValue())
		        || contest.getCurOdds().doubleValue() != (contest.getOdds() == null ? 0.00d : contest.getOdds()
		                .doubleValue())) {
		    followFlag = true;
		}
		contest.setFollowFlag(followFlag);
		contest.setBetFlag(betFlag);
		resp.setContest(contest);
	    }

	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    @Override
    public Long postContent(Long userId, Integer type, String content, String images, Long audioId, Long contestId,
	    Integer contestType, String params, String client, Boolean permission, Integer coupon)
	    throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(userId);
	ParamemeterAssert.assertDataNotNull(type);

	// 过滤type
	type = PostType.filtType(type);

	// 是否有内容
	boolean hasContent = false;
	Boolean imgFlag = false;

	// 检查用户是否不存在或被拉入黑名单
	try {
	    CbsUserResponse user = cbsUserService.getCbsUserByUserId(userId, false);
	    if (user == null) {
		throw new L99IllegalParamsException(UserMsg.CODE_USER_ACCOUNT_NOT_FOUND, UserMsg.KEY_USER_ACCOUNT_NOT_FOUND);
	    } else if (user.getStatus() == 1) {
		throw new L99IllegalParamsException(UserMsg.CODE_USER_ACCOUNT_BLOCK, UserMsg.KEY_USER_ACCOUNT_BLOCK);
	    }
	} catch (Exception e) {
	    throw new L99IllegalParamsException(UserMsg.CODE_USER_ACCOUNT_NOT_FOUND, UserMsg.KEY_USER_ACCOUNT_NOT_FOUND);
	}

	JSONObject paramObj = new JSONObject();
	// 竞猜 、PK和押押必须传递param参数
	if (type == PostType.GUESS || type == PostType.PK || type == PostType.YY) {
	    if (StringUtils.isBlank(params)) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    try {
		JSONObject contestObj = new JSONObject(params);
		paramObj.put("bet", contestObj);
		hasContent = true;
	    } catch (Exception e) {
		LOG.error(e.getMessage(), e);
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	}

	if (type == ContentConstants.PostType.PK) {
	    hasContent = true;
	}

	if (StringUtils.isNotBlank(content)) {
	    // replace html tag
	    try {
		if (content.length() > 511) {
		    throw new L99IllegalParamsException(ContentMsg.CODE_CONTENT_POST_FAIL, ContentMsg.KEY_CONTENT_POST_FAIL);
		}
		content = StringUtil.filterDescriptionWithShielded(content, false, null);
	    } catch (L99ContentRejectedException e1) {
		throw new L99IllegalParamsException(ContentMsg.CODE_CONTENT_SENSITIVE, ContentMsg.KEY_CONTENT_SENSITIVE);
	    }

	    // 过滤内容（内容和长度）
	    if (content.length() > ContestConstants.Content.MAX_CONTENT_LENGTH) {
		content = content.substring(0, ContestConstants.Content.MAX_CONTENT_LENGTH);
	    }
	    hasContent = true;
	} else {
	    // 不存储空格
	    content = null;
	}

	// 处理照片
	if (StringUtils.isNotBlank(images)) {
	    String imgArray = fillContentImage(images);
	    if (imgArray.length() == 0) {
		LOG.error("User " + userId + " post photo fail. Params is " + images);
		throw new L99IllegalDataException(ContentMsg.CODE_CONTENT_PHOTO_NOT_FOUND,
		        ContentMsg.KEY_CONTENT_PHOTO_NOT_FOUND);
	    }
	    try {
		paramObj.put("images", imgArray);
		hasContent = true;
		imgFlag = true;
	    } catch (JSONException e) {
		LOG.error(e.getMessage(), e);
	    }
	}

	// 转换附加参数
	if (paramObj.length() > 0) {
	    params = paramObj.toString();
	}
	// 都为空校验
	if (!hasContent) {
	    throw new L99IllegalParamsException(ContentMsg.CODE_CONTENT_TEXT_EMPTY, ContentMsg.KEY_CONTENT_TEXT_EMPTY);
	}
	FriendCircle friendCircle = new FriendCircle();
	friendCircle.setUserId(userId);
	friendCircle.setType(type);
	friendCircle.setContent(content);
	friendCircle.setParams(params);
	// 设置是否有内容字段，没有理由＆没有图片＆没有音频，那么该字段为false
	friendCircle.setHasContent(StringUtils.isNotBlank(content) || StringUtils.isNotBlank(images) || (audioId != null));
	if (contestId != null) {
	    friendCircle.setContestId(contestId);
	}
	friendCircle.setContestType(contestType);
	friendCircle.setDeleteFlag(false);
	friendCircle.setCreateTime(new Date());
	friendCircle.setClient(CbsClientType.getClient(client).getName());
	friendCircle.setCoupon(coupon);
	Integer insert = friendCircleDao.insert(friendCircle);
	if (insert < 1) {
	    throw new L99IllegalDataException(ContentMsg.CODE_CONTENT_POST_FAIL, ContentMsg.KEY_CONTENT_POST_FAIL);
	}
	// List<Long> mes = relationshipService.getAllAttentionMe(userId);
	List<UserFriendCircle> userCircles = new ArrayList<UserFriendCircle>();
	UserFriendCircle circle = new UserFriendCircle();
	circle.setFriendCircleId(friendCircle.getId());
	circle.setUserId(userId);
	circle.setDeleteFlag(friendCircle.getDeleteFlag());
	circle.setHasContent(friendCircle.getHasContent());
	circle.setType(friendCircle.getType());
	userCircles.add(circle);

	int sum = userCircles.size();
	int j = sum / SUM_INSERT;
	List<UserFriendCircle> rightList = null;
	for (int i = 0; i <= j; i++) {
	    if (sum > SUM_INSERT) {
		rightList = userCircles.subList(SUM_INSERT, sum);
		userCircles = userCircles.subList(0, SUM_INSERT);
		sum = rightList.size();
	    }
	    userFriendCircleDao.insert(userCircles);
	    userCircles = rightList;
	}

	// 发内容成就
	try {
	    ContentAchieveBO bo = new ContentAchieveBO();
	    bo.setBehavior_type(BehaviorConstants.BehaviorType.CONTENT_TYPE);
	    bo.setUser_id(userId);
	    // bo.setAudio_flag(audioFlag);
	    bo.setImg_flag(imgFlag);
	    // achieveService.addAchieveData(bo.generateData(), false);
	} catch (Throwable t) {
	    LOG.error("userId=" + userId + ", content achieve data analysis failed: " + t.getMessage(), t);
	}

	return friendCircle.getId();
    }

    private String fillContentImage(String images) {

	// 解析出photoId
	String[] photoIdStr = images.split(",");
	JSONArray imgArray = new JSONArray();
	for (String idStr : photoIdStr) {
	    try {
		JSONObject photoObj = new JSONObject();
		photoObj.put("path", idStr);
		imgArray.put(photoObj);
	    } catch (Exception e) {
		LOG.error(e.getMessage(), e);
	    }
	}

	return images;

    }

    @SuppressWarnings("unchecked")
    @Override
    public void editContentContest(Long id, String result, Boolean repair) throws L99IllegalDataException,
	    L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(id);
	JSONObject resultObj = null;
	if (!repair) {
	    ParamemeterAssert.assertDataNotNull(result);
	    // 传递过来的结果参数必须有内容，且符合json格式
	    try {
		resultObj = new JSONObject(result);
	    } catch (Exception e) {
		LOG.error(e.getMessage(), e);
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    if (resultObj == null || resultObj.length() == 0) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	}

	FriendCircle cont = friendCircleDao.findById(id);
	if (cont == null || cont.getDeleteFlag()) {
	    LOG.info("editContentContest" + id + "猜友圈不存在或被删除");
	    return;
	}
	if (cont.getType() == PostType.MOOD) {
	    throw new L99IllegalDataException(ContentMsg.CODE_CONTENT_MOOD_EDIT, ContentMsg.KEY_CONTENT_MOOD_EDIT);
	}

	try {
	    JSONObject paramObj = new JSONObject(cont.getParams());
	    JSONObject betObj = paramObj.getJSONObject("bet");
	    if (betObj == null) {
		throw new L99IllegalOperateException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
	    }
	    // 获取比赛Id
	    Long contestId = cont.getContestId();
	    // 比赛类型
	    Integer contestType = cont.getContestType();
	    // 下单id
	    Long bId = betObj.getLong("bId");

	    if (ContestType.FOOTBALL == contestType.intValue()) {// 足球
		// 玩法类型
		Integer playId = betObj.getInt("playId");
		FbContest contest = fbContestDao.selectById(contestId);
		if (contest != null) {
		    // 写入球队比分
		    betObj.put("hs", contest.getHomeScores());
		    betObj.put("as", contest.getAwayScores());
		}

		if (repair) {
		    if (PlayType.FB_SPF.getId() == playId.intValue()) {
			BetOp op = fbBetOpDao.selectById(bId);
			betObj.put("back", roundedUpBack(op.isLongbi(), op.getBack()));
			betObj.put("status", op.getStatus());
		    } else if (PlayType.FB_RQSPF.getId() == playId.intValue()) {
			BetJc jc = fbBetJcDao.selectById(bId);
			betObj.put("back", roundedUpBack(jc.isLongbi(), jc.getBack()));
			betObj.put("status", jc.getStatus());
		    } else if (PlayType.FB_SIZE.getId() == playId.intValue()) {
			BetSize size = fbBetSizeDao.selectById(bId);
			betObj.put("back", roundedUpBack(size.isLongbi(), size.getBack()));
			betObj.put("status", size.getStatus());
		    } else if (PlayType.FB_ODDEVEN.getId() == playId.intValue()) {
			BetOddeven oe = fbBetOddevenDao.selectById(bId);
			betObj.put("back", roundedUpBack(oe.isLongbi(), oe.getBack()));
			betObj.put("status", oe.getStatus());
		    }
		    cont.setSettle(true);
		}

	    } else if (ContestType.BASKETBALL == contestType.intValue()) {// 篮球
		// 玩法类型
		Integer playId = betObj.getInt("playId");
		BbContest contest = bbContestDao.selectById(contestId);
		if (contest != null) {
		    // 写入球队比分
		    betObj.put("hs", contest.getHomeScores());
		    betObj.put("as", contest.getAwayScores());
		}

		if (repair) {
		    if (PlayType.BB_SPF.getId() == playId.intValue()) {
			BetOp op = bbBetOpDao.selectById(bId);
			betObj.put("back", roundedUpBack(op.isLongbi(), op.getBack()));
			betObj.put("status", op.getStatus());
		    } else if (PlayType.BB_JC.getId() == playId.intValue()) {
			BetJc jc = bbBetJcDao.selectById(bId);
			betObj.put("back", roundedUpBack(jc.isLongbi(), jc.getBack()));
			betObj.put("status", jc.getStatus());
		    } else if (PlayType.BB_SIZE.getId() == playId.intValue()) {
			BetSize size = bbBetSizeDao.selectById(bId);
			betObj.put("back", roundedUpBack(size.isLongbi(), size.getBack()));
			betObj.put("status", size.getStatus());
		    } else if (PlayType.BB_ODDEVEN.getId() == playId.intValue()) {
			BetOddeven oe = bbBetOddevenDao.selectById(bId);
			betObj.put("back", roundedUpBack(oe.isLongbi(), oe.getBack()));
			betObj.put("status", oe.getStatus());
		    }
		    cont.setSettle(true);
		}
	    } else if (ContestType.YAYA == contestType.intValue()) {// 押押
		YyContest contest = yyContestDao.selectById(contestId);
		if (contest != null) {
		    // 写入球队比分
		    betObj.put("winner", contest.getWinner());
		    // 写入获胜选项描述和用户选项描述
		    List<YyOptionResponse> options = YyContestTransformUtil.transformYyOption(contest.getOptions(), null);
		    YyOptionResponse winOption = options.get(contest.getWinner() - 1);
		    if (winOption != null) {
			betObj.put("opw", winOption.getName());
		    }
		}
		if (repair) {
		    YyBet yyBet = yyBetDao.selectById(bId);
		    betObj.put("back", roundedUpBack(yyBet.isLongbi(), yyBet.getBack()));
		    betObj.put("status", yyBet.getStatus());
		    cont.setSettle(true);
		}
	    }
	    if (!repair) {
		// 将比赛结果写入内容
		Iterator<String> it = resultObj.keys();
		while (it.hasNext()) {
		    String key = it.next();
		    betObj.put(key, resultObj.get(key));
		}
	    }
	    cont.setParams(paramObj.toString());
	    boolean flag = friendCircleDao.updateFriendCircle(cont);
	    if (!flag) {
		throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    @Override
    public void publishContent(Long userId, String content, String images, Long contentId, Integer priceId, String client)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException, L99NetworkException,
	    L99ContentRejectedException {

	ParamemeterAssert.assertDataNotNull(userId);
	ParamemeterAssert.assertDataOrNotNull(content, images);

	CbsUserResponse user = cbsUserService.getCbsUserByUserId(userId, false);
	if (user.getStatus() == UserConstants.BLOCK) {
	    throw new L99IllegalDataException(UserMsg.CODE_USER_ACCOUNT_BLOCK, UserMsg.KEY_USER_ACCOUNT_BLOCK);
	}

	int type = 0;

	if (priceId != null && priceId != 0) {
	    if (priceId > 0 && priceId < 10) {
		if (contentId == null) {
		    type = FrontPage.TYPE_CONTENT_REMAKES;
		} else {
		    type = FrontPage.TYPE_CONTENT_REASON;
		}

	    } else if (priceId > 10) {
		if (contentId == null) {
		    type = FrontPage.TYPE_AD_REMAKES;
		} else {
		    type = FrontPage.TYPE_AD_REASON;
		}
	    } else {
		throw new L99IllegalDataException(BroadcastMsg.CODE_Broadcast_NOT_PRICE,
		        BroadcastMsg.KEY_Broadcast_NOT_PRICE);
	    }
	}

	FriendCircle circle;
	if (StringUtils.isNotEmpty(content) && content.length() > 511) {
	    throw new L99IllegalParamsException(ContentMsg.CODE_CONTENT_POST_FAIL, ContentMsg.KEY_CONTENT_POST_FAIL);
	}

	int circleType = PostType.MOOD;
	if (contentId != null) { // 发表有内容的竞猜
	    circle = friendCircleDao.findById(contentId);
	    if (circle == null) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    circleType = PostType.GUESS;
	    JSONObject paramObj = new JSONObject(circle.getParams());
	    if (!StringUtils.isEmpty(images)) {
		paramObj.put("images", fillContentImage(images));
		circle.setParams(paramObj.toString());
	    }
	    circle.setHasContent(true);
	    circle.setContent(content);
	    circle.setType(circleType);
	    circle.setSourceId((long) type);
	    friendCircleDao.updateFriendCircle(circle);

	    // 发表下单理由后不同步到评论中 edit by lhx on 16-05-16
	    // commentService.postComment(circle.getContestId(),
	    // circle.getContestType(), userId, null, content, images, null,
	    // client, 0L);

	    // add by lhx on 16-06-23 每日发表下单理由 start
	    missionUserService.validate(userId, Mission.SEND_RESON);
	    // add by lhx on 16-06-23 每日发表下单理由 end

	} else { // 发表吐槽
	    circle = new FriendCircle();
	    circle.setContent(content);
	    circle.setCreateTime(new Date());
	    circle.setDeleteFlag(false);
	    circle.setHasContent(true);
	    circle.setSourceId((long) type);
	    if (!StringUtils.isEmpty(images)) {
		JSONObject betObj = new JSONObject();
		betObj.put("images", fillContentImage(images));
		circle.setParams(betObj.toString());
	    }
	    circle.setContestType(ContestType.TUCAO);
	    circle.setUserId(userId);
	    circle.setType(circleType);
	    circle.setContestId(0L);
	    circle.setClient(CbsClientType.getClient(client).getName());
	    friendCircleDao.insert(circle);
	    contentId = circle.getId();
	}

	UserFriendCircle ufc = new UserFriendCircle();
	ufc.setFriendCircleId(contentId);
	ufc.setHasContent(true);
	ufc.setDeleteFlag(false);
	ufc.setType(circleType);
	ufc.setUserId(userId);
	userFriendCircleDao.update(ufc);

	// 发表头版，广告区
	if (type != 0) {
	    frontPageService.addFrontPage(circle.getContestId().intValue() == 0 ? null : circle.getContestId(), priceId,
		    circle.getContestType(), userId, circle.getContent(), images, null, type, circle.getId(), null, null,
		    null);
	}

    }

    @Override
    public void deleteContentByid(Long id) throws L99IllegalParamsException {
	Integer i = friendCircleDao.deleteById(id);
	if (i != null && i > 0) {
	    userFriendCircleDao.deleteByfriendCircleId(id);
	    friendCircleDao.deleteById(id);
	    FriendCircle circle = friendCircleDao.findById(id);
	    if (circle.getSourceId().intValue() != 0) {
		frontPageService.deleteFrontPagesByContentId(id, circle.getSourceId().intValue());
	    }
	}
    }

    @Override
    public void deleteContentByids(String ids) throws L99IllegalParamsException {
	if (StringUtils.isEmpty(ids)) {
	    return;
	}

	for (String s : ids.split(",")) {
	    Long id = Long.parseLong(s);
	    Integer i = friendCircleDao.deleteById(id);
	    if (i != null && i > 0) {
		userFriendCircleDao.deleteByfriendCircleId(id);
		try {
		    FriendCircle circle = friendCircleDao.findById(id);
		    if (circle.getSourceId().intValue() != 0) {
			frontPageService.deleteFrontPagesByContentId(id, circle.getSourceId().intValue());
		    }

		} catch (Exception e) {
		}

	    }
	}

    }

    @Override
    public void deleteInnerContentByid(Long id) throws L99IllegalParamsException {
	Integer i = friendCircleDao.deleteById(id);
	if (i != null && i > 0) {
	    userFriendCircleDao.deleteByfriendCircleId(id);
	}

    }

    @Override
    public void deleteByContestId(Integer contestType, Long contestId) throws L99IllegalParamsException {
	List<FriendCircle> friendCircles = friendCircleDao.findByContestId(contestType, contestId);
	for (FriendCircle friendCircle : friendCircles) {
	    if (friendCircle != null) {
		Long id = friendCircle.getId();
		Integer i = friendCircleDao.deleteById(id);
		if (i > 0) {
		    userFriendCircleDao.deleteByfriendCircleId(id);
		    if (friendCircle.getSourceId().intValue() != 0) {
			frontPageService.deleteFrontPagesByContentId(id, friendCircle.getSourceId().intValue());
		    }
		}
	    }
	}
    }

    /**
     * 获得单条吐槽信息
     */
    @Override
    public FriendCircleResponse getSingleContentResponse(Long contentId) throws L99IllegalParamsException,
	    L99NetworkException, JSONException {
	FriendCircle fc = friendCircleDao.findById(contentId);
	FriendCircleResponse friendCircle = new FriendCircleResponse();
	// 获得用户信息
	CbsUserResponse user = cbsUserService.getSimpleCbsUserByUserId(fc.getUserId());
	friendCircle = getFriendCircleInfo(user, friendCircle, fc);
	return friendCircle;
    }

    @Override
    public Map<Long, FriendCircleResponse> getMapContentResponse(List<Long> contentIds) throws L99IllegalParamsException,
	    L99NetworkException, JSONException {
	Map<Long, FriendCircleResponse> map = new HashMap<Long, FriendCircleResponse>();

	List<FriendCircle> circles = friendCircleDao.getFriendCircleByIds(contentIds, null, null);
	FriendCircleResponse friendCircle = null;
	for (FriendCircle fc : circles) {
	    // 获得用户信息
	    CbsUserResponse user = cbsUserService.getCbsUserByUserId(fc.getUserId(), false);
	    friendCircle = getFriendCircleInfo(user, friendCircle, fc);
	    map.put(fc.getId(), friendCircle);
	}
	return map;
    }

    @Override
    public Map<Long, FriendCircleResponse> getMapContentResponseWithNoComments(List<Long> contentIds)
	    throws L99IllegalParamsException {
	ParamemeterAssert.assertCollectionNotNull(contentIds);

	Map<Long, FriendCircleResponse> map = new HashMap<Long, FriendCircleResponse>();

	List<FriendCircle> circles = friendCircleDao.getFriendCircleByIds(contentIds, null, null);
	List<Long> userIds = new ArrayList<Long>(circles.size());
	for (FriendCircle fc : circles) {
	    userIds.add(fc.getUserId());
	}
	Map<Long, CbsUserResponse> userMap = cbsUserService.findCsAccountMapByIds(userIds);
	FriendCircleResponse friendCircle = null;
	for (FriendCircle fc : circles) {
	    // 获得用户信息
	    CbsUserResponse user = userMap.get(fc.getUserId());
	    friendCircle = getFriendCircleInfo(user, friendCircle, fc);
	    map.put(fc.getId(), friendCircle);
	}
	return map;
    }

    @Override
    public Map<Long, Double> getMapContentWinOdds(List<Long> contentIds) {
	Map<Long, Double> map = new HashMap<Long, Double>();
	if (contentIds == null || contentIds.isEmpty())
	    return map;
	List<FriendCircle> circles = friendCircleDao.getFriendCircleByIds(contentIds, null, null);
	for (FriendCircle fc : circles) {
	    String params = fc.getParams();
	    if (StringUtils.isNotBlank(params)) {
		try {
		    JSONObject obj = new JSONObject(params);
		    // 下单信息
		    if (obj.has("bet")) {
			Double odds = null;
			JSONObject betObj = obj.getJSONObject("bet");
			// 玩法id
			int pId = betObj.getInt("playId");
			// 比赛类型
			int contestType = fc.getContestType();
			int support = betObj.getInt("support");
			// 足球比赛
			if (contestType == ContestType.FOOTBALL) {
			    if (PlayType.FB_SPF.getId() == pId) {
				if (BetStatus.HOME == support) {
				    odds = betObj.getDouble("r1");
				} else if (BetStatus.DRAW == support) {
				    odds = betObj.getDouble("r2");
				} else if (BetStatus.AWAY == support) {
				    odds = betObj.getDouble("r3");
				}
			    } else if (PlayType.FB_RQSPF.getId() == pId) {
				if (BetStatus.HOME == support) {
				    odds = betObj.getDouble("r1");
				} else if (BetStatus.DRAW == support) {
				    odds = betObj.getDouble("r2");
				} else if (BetStatus.AWAY == support) {
				    odds = betObj.getDouble("r3");
				}
			    } else if (PlayType.FB_SIZE.getId() == pId) {
				if (BetStatus.HOME == support) {
				    odds = betObj.getDouble("r1");
				} else if (BetStatus.AWAY == support) {
				    odds = betObj.getDouble("r3");
				}
			    } else if (PlayType.FB_ODDEVEN.getId() == pId) {
				if (BetStatus.HOME == support) {
				    odds = betObj.getDouble("r1");
				} else if (BetStatus.AWAY == support) {
				    odds = betObj.getDouble("r3");
				}
			    }
			} else if (contestType == ContestType.BASKETBALL) {
			    if (PlayType.BB_SPF.getId() == pId) {
				if (BetStatus.HOME == support) {
				    odds = betObj.getDouble("r1");
				} else if (BetStatus.AWAY == support) {
				    odds = betObj.getDouble("r3");
				}
			    } else if (PlayType.BB_JC.getId() == pId) {
				if (BetStatus.HOME == support) {
				    odds = betObj.getDouble("r1");
				} else if (BetStatus.AWAY == support) {
				    odds = betObj.getDouble("r3");
				}
			    } else if (PlayType.BB_SIZE.getId() == pId) {
				if (BetStatus.HOME == support) {
				    odds = betObj.getDouble("r1");
				} else if (BetStatus.AWAY == support) {
				    odds = betObj.getDouble("r3");
				}
			    } else if (PlayType.BB_ODDEVEN.getId() == pId) {
				if (BetStatus.HOME == support) {
				    odds = betObj.getDouble("r1");
				} else if (BetStatus.AWAY == support) {
				    odds = betObj.getDouble("r3");
				}
			    }
			}
			int status = betObj.optInt("status");
			if (BetResultStatus.WIN == status) {
			    // 下单获胜
			    map.put(fc.getId(), odds);
			} else if (BetResultStatus.LOSS == status) {
			    // 下单失败
			    map.put(fc.getId(), 0d);
			} else if (BetResultStatus.DRAW == status) {
			    // 走盘
			    map.put(fc.getId(), 0d);
			}
		    }
		} catch (Exception e) {
		    LOG.error(e.getMessage(), e);
		}
	    }
	}
	return map;
    }

    @Override
    public int hashCode() {
	return super.hashCode();
    }

    @Override
    public void publishContentInner(Long userId, String content, String images, Long contentId, Integer type)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException, L99NetworkException,
	    L99ContentRejectedException {

	ParamemeterAssert.assertDataNotNull(userId, type);
	ParamemeterAssert.assertDataOrNotNull(content, images);

	CbsUserResponse user = cbsUserService.getCbsUserByUserId(userId, false);
	if (user.getStatus() == UserConstants.BLOCK) {
	    throw new L99IllegalDataException(UserMsg.CODE_USER_ACCOUNT_BLOCK, UserMsg.KEY_USER_ACCOUNT_BLOCK);
	}

	FriendCircle circle = null;
	if (StringUtils.isNotEmpty(content) && content.length() > 511) {
	    throw new L99IllegalParamsException(ContentMsg.CODE_CONTENT_POST_FAIL, ContentMsg.KEY_CONTENT_POST_FAIL);
	}

	int circleType = PostType.MOOD;
	if (contentId != null) { // 发表有内容的竞猜
	    circle = friendCircleDao.findById(contentId);
	    if (circle == null) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    circleType = PostType.GUESS;
	    JSONObject paramObj = new JSONObject(circle.getParams());
	    if (!StringUtils.isEmpty(images)) {
		paramObj.put("images", fillContentImage(images));
		circle.setParams(paramObj.toString());
	    }
	    circle.setHasContent(true);
	    circle.setContent(content);
	    circle.setType(circleType);
	    circle.setSourceId(Long.valueOf(type));
	    friendCircleDao.updateFriendCircle(circle);

	    commentService.postComment(circle.getContestId(), circle.getContestType(), userId, null, content, images, null,
		    "key:roi_web", 0L, false);

	} else { // 发表吐槽
	    circle = new FriendCircle();
	    circle.setContent(content);
	    circle.setCreateTime(new Date());
	    circle.setDeleteFlag(false);
	    circle.setHasContent(true);
	    circle.setSourceId(Long.valueOf(type));
	    if (!StringUtils.isEmpty(images)) {
		JSONObject betObj = new JSONObject();
		betObj.put("images", fillContentImage(images));
		circle.setParams(betObj.toString());
	    }
	    circle.setContestType(ContestType.TUCAO);
	    circle.setUserId(userId);
	    circle.setType(circleType);
	    circle.setContestId(0L);
	    circle.setClient(CbsClientType.getClient(null).getName());
	    friendCircleDao.insert(circle);
	    contentId = circle.getId();
	}

	UserFriendCircle ufc = new UserFriendCircle();
	ufc.setFriendCircleId(contentId);
	ufc.setHasContent(true);
	ufc.setDeleteFlag(false);
	ufc.setType(circleType);
	ufc.setUserId(userId);
	userFriendCircleDao.update(ufc);

	// 发表头版，广告区
	frontPageService.addFrontPage(circle.getContestId().intValue() == 0 ? null : circle.getContestId(), null,
	        circle.getContestType(), userId, circle.getContent(), images, null, type, circle.getId(), null, null, null);

    }

    /**
     * 清理未添加结果的战绩
     */
    @Override
    public CustomResponse settleCircle(String circleIds) {
	int all = 0, settle = 0;
	Long startId = null;
	List<FriendCircle> circles = null;

	do {
	    if (StringUtils.isEmpty(circleIds)) { // 清理所有
		circles = friendCircleDao.findNotSettles(startId, 100);
	    } else { // 清理指定Ids
		List<Long> circleIdList = new ArrayList<>();
		for (String circleId : circleIds.split(",")) {
		    if (RegexUtil.hasMatche(circleId, RegexUtil.DOMAIN_REG2)) {
			circleIdList.add(Long.valueOf(circleId));
		    }
		}
		circles = friendCircleDao.getFriendCircleByIds(circleIdList, null, null);
	    }

	    for (FriendCircle circle : circles) {
		try {
		    all++;
		    startId = circle.getId();
		    JSONObject circleObj = new JSONObject(circle.getParams());
		    int playType = circleObj.getJSONObject("bet").getInt("playId");
		    Double back = null;
		    Integer status = null;
		    if (playType == PlayType.FB_SPF.getId()) {
			BetOp fbOp = fbBetOpDao.selectByContent(circle.getId());
			if (fbOp != null && fbOp.getStatus() != BetResultStatus.INIT) {
			    back = fbOp.getBack();
			    status = fbOp.getStatus();
			}
		    } else if (playType == PlayType.FB_RQSPF.getId()) {
			BetJc fbJc = fbBetJcDao.selectByContent(circle.getId());
			if (fbJc != null && fbJc.getStatus() != BetResultStatus.INIT) {
			    back = fbJc.getBack();
			    status = fbJc.getStatus();
			}
		    } else if (playType == PlayType.FB_SIZE.getId()) {
			BetSize betSize = fbBetSizeDao.selectByContent(circle.getId());
			if (betSize != null && betSize.getStatus() != BetResultStatus.INIT) {
			    back = betSize.getBack();
			    status = betSize.getStatus();
			}
		    } else if (playType == PlayType.FB_ODDEVEN.getId()) {
			BetOddeven betOddeven = fbBetOddevenDao.selectByContent(circle.getId());
			if (betOddeven != null && betOddeven.getStatus() != BetResultStatus.INIT) {
			    back = betOddeven.getBack();
			    status = betOddeven.getStatus();
			}
		    } else if (playType == PlayType.BB_SPF.getId()) {
			BetOp bbOp = bbBetOpDao.selectByContent(circle.getId());
			if (bbOp != null && bbOp.getStatus() != BetResultStatus.INIT) {
			    back = bbOp.getBack();
			    status = bbOp.getStatus();
			}
		    } else if (playType == PlayType.BB_JC.getId()) {
			BetJc bbJc = bbBetJcDao.selectByContent(circle.getId());
			if (bbJc != null && bbJc.getStatus() != BetResultStatus.INIT) {
			    back = bbJc.getBack();
			    status = bbJc.getStatus();
			}
		    } else if (playType == PlayType.BB_SIZE.getId()) {
			BetSize betSize = bbBetSizeDao.selectByContent(circle.getId());
			if (betSize != null && betSize.getStatus() != BetResultStatus.INIT) {
			    back = betSize.getBack();
			    status = betSize.getStatus();
			}
		    } else if (playType == PlayType.BB_ODDEVEN.getId()) {
			BetOddeven betOddeven = bbBetOddevenDao.selectByContent(circle.getId());
			if (betOddeven != null && betOddeven.getStatus() != BetResultStatus.INIT) {
			    back = betOddeven.getBack();
			    status = betOddeven.getStatus();
			}
		    }
		    if (back != null && status != null) {
			JSONObject resultObj = new JSONObject();
			resultObj.put("back", back);
			resultObj.put("status", status);
			editContentContest(circle.getId(), resultObj.toString(), true);
			LOG.info(String.format("repair circle ok %d", circle.getId()));
			settle++;
		    }

		} catch (Exception e) {
		    LOG.error(String.format("repair circle failed %d - %s", circle.getId(), e.getMessage()));
		}
		// 更新结算状态
		circle.setSettle(true);
		friendCircleDao.updateFriendCircle(circle);
	    }
	} while (StringUtils.isEmpty(circleIds) && circles != null && circles.size() > 0);

	CustomResponse data = new CustomResponse();
	data.put("all", all);
	data.put("settle", settle);
	return data;
    }

    /**
     * 龙筹券返回向上取整
     * 
     * @param back
     * @return
     */
    private double roundedUpBack(boolean longbi, Double back) {
	if (!longbi) {// 纯龙筹场向上取整
	    back = BetConstants.getCouponPriceByBack(back) * 1D;
	}
	return back;
    }

    @Override
    public FriendCircleListResponse getInnerReasonList(Long startId, Long endId, Integer limit, Integer skip, int type)
	    throws L99NetworkException, L99IllegalParamsException, JSONException {
	FriendCircleListResponse rep = new FriendCircleListResponse();
	List<FriendCircleResponse> friendCircles = new ArrayList<>();
	rep.setContents(friendCircles);
	List<FriendCircle> circles = friendCircleDao.getInnerReasonList(startId, endId, limit, skip, type);
	rep.setNumber(friendCircleDao.getInnerReasonNum(type));
	if (startId != null) {
	    Collections.reverse(circles);
	}
	// 根据circles活得猜友圈记录
	for (FriendCircle cs : circles) {
	    FriendCircleResponse friendCircle = new FriendCircleResponse();
	    // 活得用户信息
	    friendCircle.setUser(cbsUserService.getCbsUserByUserId(cs.getUserId(), false));// 设置用户对象
	    // friendCircle.setClient(cs.getClient());
	    friendCircle.setContent(cs.getContent());
	    friendCircle.setFriend_circle_id(cs.getId());
	    friendCircle.setContest_type(cs.getContestType());
	    // int friendTypeTmp = getFriendType(cs.getType(),
	    // cs.getHasContent());
	    // friendCircle.setFriendType(friendTypeTmp);
	    friendCircle.setCreate_time(CbsTimeUtils.getUtcTimeForDate(cs.getCreateTime()));
	    friendCircle.setData_flag(cs.getDeleteFlag() ? 1 : 0);
	    friendCircle.setCoupon(cs.getCoupon());
	    friendCircle.setHasContent(cs.getHasContent());
	    friendCircle.setParams(cs.getParams());
	    friendCircles.add(friendCircle);
	}

	if (!circles.isEmpty()) {
	    rep.setStartId(circles.get(0).getId());
	    rep.setEndId(circles.get(circles.size() - 1).getId());
	}
	return rep;
    }

    @Override
    public FriendCircleListResponse getReasonsList(Long contestId, Integer contestType, Long startId, Integer limit)
	    throws L99IllegalParamsException, JSONException, L99NetworkException {
	ParamemeterAssert.assertDataNotNull(contestId, contestType);
	FriendCircleListResponse rep = new FriendCircleListResponse();
	List<FriendCircleResponse> friendCircles = new ArrayList<>();
	rep.setContents(friendCircles);
	List<FriendCircle> circles = friendCircleDao.getReasonList(contestId, contestType, startId, limit);
	// 根据circles活得猜友圈记录
	for (FriendCircle cs : circles) {
	    FriendCircleResponse friendCircle = new FriendCircleResponse();
	    // 获得用户信息
	    friendCircle.setUser(cbsUserService.getCbsUserByUserId(cs.getUserId(), false));// 设置用户对象
	    friendCircle.setContent(cs.getContent());
	    friendCircle.setFriend_circle_id(cs.getId());
	    friendCircle.setContest_type(cs.getContestType());
	    friendCircle.setCreate_time(CbsTimeUtils.getUtcTimeForDate(cs.getCreateTime()));
	    // friendCircle.setData_flag(cs.getDeleteFlag() ? 1 : 0);
	    friendCircle.setCoupon(cs.getCoupon());
	    // friendCircle.setHasContent(cs.getHasContent());
	    String params = cs.getParams();
	    if (StringUtils.isNotBlank(params)) {
		JSONObject jsonObject = new JSONObject(params);
		// String bet = jsonObject.optString("bet");
		// if (StringUtils.isNotBlank(bet)){
		// Map<Object,Object> map = new HashMap<>();
		// JSONObject betObject = new JSONObject(bet);
		// Iterator iterator = betObject.keys();
		// while (iterator.hasNext()){
		// Object o = iterator.next();
		// map.put(o,betObject.get(o.toString()));
		// }
		// friendCircle.setBets(map);
		// }
		fillContentContest(cs, friendCircle, jsonObject);
		String image = jsonObject.optString("images");
		if (StringUtils.isNotBlank(image)) {
		    friendCircle.setImages(image.split(","));
		}

	    }
	    friendCircles.add(friendCircle);
	    rep.setStartId(cs.getId());
	}
	return rep;
    }

    @Override
    public boolean isHasReason(long contestId, int contestType) {
	return friendCircleDao.isHasReason(contestId, contestType);
    }

    /**
     * 后台查询未结算战绩
     * 
     * @throws JSONException
     * @throws L99NetworkException
     * @throws L99IllegalParamsException
     */
    @Override
    public FriendCircleListResponse getInnerFirCirNoSettleList(Long startId, Integer limit)
	    throws L99IllegalParamsException, L99NetworkException, JSONException {
	FriendCircleListResponse rep = new FriendCircleListResponse();
	List<FriendCircleResponse> friendCircles = new ArrayList<FriendCircleResponse>();
	List<FriendCircle> circles = friendCircleDao.findInnerNotSettles(startId, limit);
	rep.setContents(friendCircles);
	for (FriendCircle cs : circles) {
	    FriendCircleResponse friendCircle = new FriendCircleResponse();
	    friendCircle.setUser(cbsUserService.getCbsUserByUserId(cs.getUserId(), false));
	    friendCircle.setClient(cs.getClient());
	    friendCircle.setContent(cs.getContent());
	    friendCircle.setFriend_circle_id(cs.getId());
	    friendCircle.setType(cs.getType());
	    int friendTypeTmp = getFriendType(cs.getType(), cs.getHasContent());
	    friendCircle.setFriendType(friendTypeTmp);
	    friendCircle.setCreate_time(CbsTimeUtils.getUtcTimeForDate(cs.getCreateTime()));
	    friendCircle.setData_flag(cs.getDeleteFlag() ? 1 : 0);
	    friendCircle.setCoupon(cs.getCoupon());
	    String params = cs.getParams();
	    friendCircle.setHasContent(cs.getHasContent());
	    if (StringUtils.isNotBlank(params)) {
		try {
		    JSONObject obj = new JSONObject(params);
		    if (obj.has("images")) {
			String photoIdStr = obj.getString("images");
			if (com.alibaba.dubbo.common.utils.StringUtils.isNotEmpty(photoIdStr)) {
			    friendCircle.setImages(photoIdStr.split(","));
			}
		    }
		} catch (Exception e) {
		    LOG.error(e.getMessage(), e);
		}
	    }
	    friendCircle.setParams(params);
	    friendCircle.setContest_type(cs.getContestType());

	    friendCircles.add(friendCircle);
	}
	if (!circles.isEmpty()) {
	    rep.setStartId(circles.get(circles.size() - 1).getId());
	}
	rep.setNumber(friendCircles.size());
	return rep;
    }

    @Override
    public FriendCircleResponse findOneYayaCircle(Long id) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(id);
	FriendCircle circle = friendCircleDao.findById(id);
	FriendCircleResponse friendCircle = null;
	if (circle != null) {
	    CbsUserResponse user = cbsUserService.selectById(circle.getUserId());
	    friendCircle = getFriendCircleInfo(user, friendCircle, circle);
	}
	return friendCircle;
    }

}
