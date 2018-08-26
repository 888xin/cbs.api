package com.lifeix.cbs.api.action.inner;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.BetConstants;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.contest.ContestAdListResponse;
import com.lifeix.cbs.contest.bean.contest.ContestAdResponse;
import com.lifeix.cbs.contest.bean.fb.ContestListResponse;
import com.lifeix.cbs.contest.bean.fb.CupListResponse;
import com.lifeix.cbs.contest.bean.odds.*;
import com.lifeix.cbs.contest.service.bb.BbContestService;
import com.lifeix.cbs.contest.service.contest.ContestAdService;
import com.lifeix.cbs.contest.service.contest.ContestStatisticsService;
import com.lifeix.cbs.contest.service.fb.FbContestService;
import com.lifeix.cbs.contest.service.odds.BbOddsService;
import com.lifeix.cbs.contest.service.odds.FbOddsService;
import com.lifeix.cbs.contest.service.odds.OddsWarnService;
import com.lifeix.cbs.contest.service.rollback.ContestRollbackService;
import com.lifeix.cbs.contest.service.spark.cup.BbCupDubbo;
import com.lifeix.cbs.contest.service.spark.cup.FbCupDubbo;
import com.lifeix.cbs.contest.service.spark.odds.BbOddsDubbo;
import com.lifeix.cbs.contest.service.spark.odds.FbOddsDubbo;
import com.lifeix.cbs.contest.service.spark.settle.SettleTaskDubbo;
import com.lifeix.cbs.contest.service.spark.statistic.SameStatisticDubbo;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.ListResponse;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * lhx on 15-10-26 上午11:04
 * 
 * @Description
 */
@Controller
@Path("/inner/contest")
public class ContestInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(ContestInnerResource.class);

    @Autowired
    private FbContestService fbContestService;

    @Autowired
    private BbContestService bbContestService;

    @Autowired
    private BbCupDubbo bbCupDubbo;

    @Autowired
    private FbCupDubbo fbCupDubbo;

    @Autowired
    private FbOddsService fbOddsService;

    @Autowired
    private BbOddsService bbOddsService;

    @Autowired
    private FbOddsDubbo fbOddsDubbo;

    @Autowired
    private BbOddsDubbo bbOddsDubbo;

    @Autowired
    private ContestRollbackService contestRollbackService;

    @Autowired
    private SettleTaskDubbo settleTaskDubbo;

    @Autowired
    private OddsWarnService oddsWarnService;

    @Autowired
    private ContestAdService contestAdService;

	@Autowired
	private ContestStatisticsService contestStatisticsService;

    /**
     * 单个押押赛事
     * 
     * @param id
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/ad/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewContestAd(@QueryParam("id") Long id) throws JSONException {
	DataResponse<ContestAdResponse> ret = new DataResponse<ContestAdResponse>();
	try {
	    start();
	    ContestAdResponse response = contestAdService.viewAdContest(id);
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 赛事广告列表
     * 
     * @param hideFlag
     * @param contestType
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/ad/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String contestAdList(@QueryParam("hide_flag") Boolean hideFlag, @QueryParam("contest_type") Integer contestType,
	    @QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {
	DataResponse<ContestAdListResponse> ret = new DataResponse<ContestAdListResponse>();
	try {
	    start();
	    ContestAdListResponse response = contestAdService.findContestsAdInner(contestType, hideFlag, startId, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 修改赛事广告
     * 
     * @param id
     * @param hideFlag
     * @return
     */
    @POST
    @Path("/ad/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateContestFlag(@FormParam("id") Long id, @FormParam("hide_flag") Boolean hideFlag) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    contestAdService.updateHide(id, hideFlag);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 修改赛事广告
     * 
     * @param id
     * @param contestId
     * @param contestType
     * @param title
     * @param images
     * @param text
     * @return
     */
    @POST
    @Path("/ad/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateContestAd(@FormParam("id") Long id, @FormParam("contest_id") Long contestId,
	    @FormParam("contest_type") Integer contestType, @FormParam("title") String title,
	    @FormParam("images") String images, @FormParam("text") String text) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    contestAdService.updateContestAd(id, contestId, contestType, title, images, text);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 添加赛事广告
     * 
     * @param contestId
     * @param contestType
     * @param title
     * @param images
     * @param text
     * @return
     */
    @POST
    @Path("/ad/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String addContestAd(@FormParam("contest_id") Long contestId, @FormParam("contest_type") Integer contestType,
	    @FormParam("title") String title, @FormParam("images") String images, @FormParam("text") String text) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    contestAdService.insertContestAd(contestId, contestType, title, images, text);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 获得足球异常赛事列表
     * 
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/fb/abnormal/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAbFbList(@QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    ContestListResponse contestListResponse = fbContestService.findAbnormalContests(startId, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(contestListResponse);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 获得篮球异常赛事列表
     * 
     * @param format
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/bb/abnormal/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAbBbList(@QueryParam("format") @DefaultValue("json") String format,
	    @QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    ContestListResponse contestListResponse = bbContestService.findAbnormalContests(startId, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(contestListResponse);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 赛事结算列表
     * 
     * @param settleId
     * @param type
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/settle/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSettleList(@QueryParam("settle_id") Long settleId, @QueryParam("type") @DefaultValue("0") Integer type,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    ContestListResponse contestListResponse = settleTaskDubbo.settlesContest(settleId, type, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(contestListResponse);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 查找足球赔率
     * 
     * @param startId
     *            起始odd的Id值
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/fb/odds/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFbOddsList(@QueryParam("start_id") Long startId, @QueryParam("type") @DefaultValue("1") Integer type,
	    @QueryParam("is_five") Boolean isFive, @QueryParam("by_order") @DefaultValue("1") Integer byOrder,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    if (BetConstants.ContestContainOdds.FB_SPF == type) {
		OddsOpListResponse oddsOpListResponse = fbOddsService.findManageOpOdds(startId, limit, isFive, byOrder);
		ret.setCode(DataResponse.OK);
		ret.setData(oddsOpListResponse);
	    } else if (BetConstants.ContestContainOdds.FB_RQSPF == type) {
		OddsJcListResponse oddsJcListResponse = fbOddsService.findManageJcOdds(startId, limit, isFive, byOrder);
		ret.setCode(DataResponse.OK);
		ret.setData(oddsJcListResponse);
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 查找篮球赔率
     * 
     * @param startId
     * @param type
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/bb/odds/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBbOddsList(@QueryParam("start_id") Long startId, @QueryParam("type") @DefaultValue("1") Integer type,
	    @QueryParam("is_five") Boolean isFive, @QueryParam("by_order") @DefaultValue("1") Integer byOrder,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    if (BetConstants.ContestContainOdds_BB.BB_SPF == type) {
		OddsOpListResponse oddsOpListResponse = bbOddsService.findManageOpOdds(startId, limit, isFive, byOrder);
		ret.setCode(DataResponse.OK);
		ret.setData(oddsOpListResponse);
	    } else if (BetConstants.ContestContainOdds_BB.BB_RQSPF == type) {
		OddsJcListResponse oddsJcListResponse = bbOddsService.findManageJcOdds(startId, limit, isFive, byOrder);
		ret.setCode(DataResponse.OK);
		ret.setData(oddsJcListResponse);
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 更新让球胜负平赔率
     * 
     * @param oddsId
     * @param contestId
     * @param handicap
     * @param homeRoi
     * @param drawRoi
     * @param awayRoi
     * @param initHandicap
     * @param initHomeRoi
     * @param initDrawRoi
     * @param initAwayRoi
     * @param close
     * @param lock
     * @param type
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/jc/odds/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateFbOdds(@FormParam("odds_id") Long oddsId, @FormParam("contest_id") Long contestId,
	    @FormParam("handicap") Double handicap, @FormParam("home_roi") Double homeRoi,
	    @FormParam("draw_roi") Double drawRoi, @FormParam("away_roi") Double awayRoi,
	    @FormParam("init_handicap") Double initHandicap, @FormParam("init_home_roi") Double initHomeRoi,
	    @FormParam("init_draw_roi") Double initDrawRoi, @FormParam("init_away_roi") Double initAwayRoi,
	    @FormParam("close_flag") Boolean close, @FormParam("lock_flag") Boolean lock,
	    @FormParam("type") @DefaultValue("0") Integer type) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    OddsJcResponse oddsJcResponse = new OddsJcResponse();
	    oddsJcResponse.setOdds_id(oddsId);
	    oddsJcResponse.setContest_id(contestId);
	    oddsJcResponse.setHome_roi(homeRoi);
	    oddsJcResponse.setInit_home_roi(initHomeRoi);
	    oddsJcResponse.setDraw_roi(drawRoi);
	    oddsJcResponse.setInit_draw_roi(initDrawRoi);
	    oddsJcResponse.setAway_roi(awayRoi);
	    oddsJcResponse.setInit_away_roi(initAwayRoi);
	    oddsJcResponse.setClose_flag(close);
	    oddsJcResponse.setLock_flag(lock);
	    oddsJcResponse.setHandicap(handicap);
	    oddsJcResponse.setInit_handicap(initHandicap);
	    if (ContestConstants.ContestType.FOOTBALL == type) {
		fbOddsService.updateJcOdds(oddsJcResponse);
	    } else if (ContestConstants.ContestType.BASKETBALL == type) {
		bbOddsService.updateJcOdds(oddsJcResponse);
	    }
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException | L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 更新胜负平赔率
     * 
     * @param oddsId
     * @param contestId
     * @param homeRoi
     * @param drawRoi
     * @param awayRoi
     * @param initHomeRoi
     * @param initDrawRoi
     * @param initAwayRoi
     * @param close
     * @param lock
     * @param type
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/op/odds/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateOpOdds(@FormParam("odds_id") Long oddsId, @FormParam("contest_id") Long contestId,
	    @FormParam("home_roi") Double homeRoi, @FormParam("draw_roi") Double drawRoi,
	    @FormParam("away_roi") Double awayRoi, @FormParam("init_home_roi") Double initHomeRoi,
	    @FormParam("init_draw_roi") Double initDrawRoi, @FormParam("init_away_roi") Double initAwayRoi,
	    @FormParam("close_flag") Boolean close, @FormParam("lock_flag") Boolean lock,
	    @FormParam("type") @DefaultValue("0") Integer type) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    OddsOpResponse oddsOpResponse = new OddsOpResponse();
	    oddsOpResponse.setOdds_id(oddsId);
	    oddsOpResponse.setContest_id(contestId);
	    oddsOpResponse.setHome_roi(homeRoi);
	    oddsOpResponse.setInit_home_roi(initHomeRoi);
	    oddsOpResponse.setDraw_roi(drawRoi);
	    oddsOpResponse.setInit_draw_roi(initDrawRoi);
	    oddsOpResponse.setAway_roi(awayRoi);
	    oddsOpResponse.setInit_away_roi(initAwayRoi);
	    oddsOpResponse.setClose_flag(close);
	    oddsOpResponse.setLock_flag(lock);
	    if (ContestConstants.ContestType.FOOTBALL == type) {
		fbOddsService.updateOpOdds(oddsOpResponse);
	    } else if (ContestConstants.ContestType.BASKETBALL == type) {
		bbOddsService.updateOpOdds(oddsOpResponse);
	    }
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException | L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 更新大小球赔率
     */
    @POST
    @Path("/size/odds/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateSizeOdds(@FormParam("contest_id") Long contestId, @FormParam("odds_id") Long oddsId,
	    @FormParam("handicap") Double handicap, @FormParam("big_roi") Double bigRoi,
	    @FormParam("tiny_roi") Double tinyRoi, @FormParam("type") @DefaultValue("0") Integer type) throws JSONException {
	DataResponse<Object> ret = new DataResponse<>();
	try {
	    start();
	    OddsSizeResponse oddsSizeResponse = new OddsSizeResponse();
	    oddsSizeResponse.setContest_id(contestId);
	    oddsSizeResponse.setOdds_id(oddsId);
	    oddsSizeResponse.setBig_roi(bigRoi);
	    oddsSizeResponse.setTiny_roi(tinyRoi);
	    oddsSizeResponse.setHandicap(handicap);
	    if (ContestConstants.ContestType.FOOTBALL == type) {
		fbOddsService.updateSizeOdds(oddsSizeResponse);
	    } else if (ContestConstants.ContestType.BASKETBALL == type) {
		bbOddsService.updateSizeOdds(oddsSizeResponse);
	    }
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException | L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 更改足球信息
     * 
     * @param contestId
     * @param homeScore
     * @param awayScore
     * @param status
     * @param isLock
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/fb/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String fbUpdate(@FormParam("contest_id") Long contestId,
	    @FormParam("h_score") @DefaultValue("0") Integer homeScore,
	    @FormParam("a_score") @DefaultValue("0") Integer awayScore,
	    @FormParam("status") @DefaultValue("0") Integer status, @FormParam("level") @DefaultValue("4") Integer level,
	    @FormParam("cup_name") @DefaultValue("") String cupName, @FormParam("home_count") Integer homeCount,
	    @FormParam("away_count") Integer awayCount, @FormParam("is_lock") @DefaultValue("false") Boolean isLock)
	    throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    fbContestService.updateContests(contestId, homeScore, awayScore, status, isLock, level, cupName, homeCount,
		    awayCount);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 更改篮球信息
     * 
     * @param contestId
     * @param homeScore
     * @param awayScore
     * @param status
     * @param isLock
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/bb/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String bbUpdate(@FormParam("contest_id") Long contestId,
	    @FormParam("h_score") @DefaultValue("0") Integer homeScore,
	    @FormParam("a_score") @DefaultValue("0") Integer awayScore,
	    @FormParam("status") @DefaultValue("0") Integer status, @FormParam("level") @DefaultValue("4") Integer level,
	    @FormParam("cup_name") @DefaultValue("") String cupName, @FormParam("home_count") Integer homeCount,
	    @FormParam("away_count") Integer awayCount, @FormParam("is_lock") @DefaultValue("false") Boolean isLock)
	    throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    bbContestService.updateContests(contestId, homeScore, awayScore, status, isLock, level, cupName, homeCount,
		    awayCount);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 获取联赛名称
     */
    @GET
    @Path("/cup/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCupList(@QueryParam("type") @DefaultValue("0") Integer type,
	    @QueryParam("level") @DefaultValue("1") Integer level) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    CupListResponse cupListResponse = null;
	    if (type == ContestConstants.ContestType.FOOTBALL) {
		cupListResponse = fbCupDubbo.getLevelCup(level);
	    } else if (type == ContestConstants.ContestType.BASKETBALL) {
		cupListResponse = bbCupDubbo.getLevelCup(level);
	    }
	    ret.setCode(DataResponse.OK);
	    ret.setData(cupListResponse);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 回滚下单
     */
    @POST
    @Path("/rollback")
    @Produces(MediaType.APPLICATION_JSON)
    public String rollback(@FormParam("contest_id") Long contestId,
	    @FormParam("contest_type") @DefaultValue("0") Integer contestType) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    contestRollbackService.rollback(contestId, contestType);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 获取比赛赔率历史
     */
    @GET
    @Path("/odds/history/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getOddsHistory(@QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("30") Integer limit, @QueryParam("contest_id") Long contestId,
	    @QueryParam("odds_type") Integer oddsType, @QueryParam("type") @DefaultValue("0") Integer type)
	    throws JSONException {
	DataResponse<ListResponse> ret = new DataResponse<ListResponse>();
	try {
	    start();
	    ListResponse resp = null;
	    if (type == ContestConstants.ContestType.FOOTBALL) {
		resp = fbOddsService.findOddsHistory(contestId, oddsType, startId, limit);
	    } else if (type == ContestConstants.ContestType.BASKETBALL) {
		resp = bbOddsService.findOddsHistory(contestId, oddsType, startId, limit);
	    }
	    ret.setCode(DataResponse.OK);
	    ret.setData(resp);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 检查最近五天的赔率是否有异常
     * 
     * @param type
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/odds/check")
    @Produces(MediaType.APPLICATION_JSON)
    public String oddsCheck(@QueryParam("type") @DefaultValue("0") Integer type) throws JSONException {
	DataResponse<Integer> ret = new DataResponse<Integer>();
	try {
	    start();
	    int data = 0;
	    if (type == ContestConstants.ContestType.FOOTBALL) {
		data = fbOddsDubbo.checkWarnOdds(null);
	    } else if (type == ContestConstants.ContestType.BASKETBALL) {
		data = bbOddsDubbo.checkWarnOdds(null);
	    }
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 获取异常赔率列表
     * 
     * @param status
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/odds/warn/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String oddsWarnList(@QueryParam("status") Integer status, @QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) throws JSONException {
	DataResponse<OddsWarnListResponse> ret = new DataResponse<OddsWarnListResponse>();
	try {
	    start();
	    OddsWarnListResponse data = oddsWarnService.getOddsWarnList(status, startId, limit);
	    ret.setData(data);
	    ret.setCode(DataResponse.OK);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

	/**
	 * 同一天相同球队信息
     */
	@GET
	@Path("/same")
	@Produces(MediaType.APPLICATION_JSON)
	public String same(@QueryParam("type") Integer type) throws JSONException {
		DataResponse<Object> ret = new DataResponse<>();
		try {
			start();
			String[] results = contestStatisticsService.getContestsBySame(type);
			ret.setData(results);
			ret.setCode(DataResponse.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			ret.setCode(DataResponse.NO);
			ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
		} finally {
			end();
		}
		return DataResponseFormat.response(ret);
	}

	/**
	 * 同一天相同球队数量
	 * @return
	 * @throws JSONException
     */
	@GET
	@Path("/same/num")
	@Produces(MediaType.APPLICATION_JSON)
	public String sameNum() throws JSONException {
		DataResponse<Object> ret = new DataResponse<>();
		try {
			start();
			int[] num  = contestStatisticsService.getContestsBySameNum();
			ret.setData(num);
			ret.setCode(DataResponse.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			ret.setCode(DataResponse.NO);
			ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
		} finally {
			end();
		}
		return DataResponseFormat.response(ret);
	}

	@POST
	@Path("/same/refresh")
	@Produces(MediaType.APPLICATION_JSON)
	public String sameRefresh(@FormParam("type") Integer type) throws JSONException {
		DataResponse<Object> ret = new DataResponse<>();
		try {
			start();
			contestStatisticsService.refreshContestsSame(type);
			ret.setCode(DataResponse.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			ret.setCode(DataResponse.NO);
			ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
		} finally {
			end();
		}
		return DataResponseFormat.response(ret);
	}

}
