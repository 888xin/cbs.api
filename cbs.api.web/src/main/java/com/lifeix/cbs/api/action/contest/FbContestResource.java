package com.lifeix.cbs.api.action.contest;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.fb.ContestFullResponse;
import com.lifeix.cbs.contest.bean.fb.ContestListResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.fb.FbFixedResponse;
import com.lifeix.cbs.contest.bean.fb.FbLiveWordsListResponse;
import com.lifeix.cbs.contest.bean.fb.FbStatistcsEventResponse;
import com.lifeix.cbs.contest.bean.fb.FbStatisticsResponse;
import com.lifeix.cbs.contest.bean.fb.ext.AboutRecordListResponse;
import com.lifeix.cbs.contest.bean.fb.ext.FbEventsListResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordListResponse;
import com.lifeix.cbs.contest.service.fb.FbContestEventService;
import com.lifeix.cbs.contest.service.fb.FbContestRecordService;
import com.lifeix.cbs.contest.service.fb.FbContestService;
import com.lifeix.cbs.contest.service.live.FbStatisticsService;
import com.lifeix.cbs.contest.service.spark.contest.FbContestDubbo;
import com.lifeix.cbs.contest.service.spark.live.FbLiveWordsDubbo;
import com.lifeix.cbs.contest.util.CbsContestSolrUtil;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.user.beans.CustomResponse;

@Controller
@Path("/fb/contest")
public class FbContestResource extends BaseAction {

    @Autowired
    private FbContestService fbContestService;

    @Autowired
    private FbLiveWordsDubbo fbLiveWordsDubbo;

    @Autowired
    private FbContestDubbo fbContestDubbo;

    @Autowired
    private FbStatisticsService fbStatisticsService;

    @Autowired
    private FbContestEventService fbContestEventService;

    @Autowired
    private FbContestRecordService fbContestRecordService;

    /**
     * 比赛信息
     * 
     * @param time
     * @param oddsType
     * @param format
     * @param client
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/time")
    @Produces(MediaType.APPLICATION_JSON)
    public String time(@QueryParam("time") String time, @QueryParam("full_flag") @DefaultValue("false") Boolean fullFlag,
	    @HeaderParam("client") String client) throws JSONException {

	DataResponse<ContestListResponse> ret = new DataResponse<ContestListResponse>();
	try {
	    start();
	    ContestListResponse response = fbContestService.findFbContestsByTime(time, fullFlag, client);
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
     * 根据开始结束日期，获取赛事列表
     * 
     * @param startTime
     * @param endTime
     * @return
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("start_time") String startTime, @QueryParam("end_time") String endTime) {
	DataResponse<ContestListResponse> ret = new DataResponse<ContestListResponse>();
	try {
	    start();
	    ContestListResponse response = fbContestDubbo.findFbContestsV2ForCbs(startTime, endTime);
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
     * 比赛分数
     * 
     * @param contestIds
     * @param format
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/score")
    @Produces(MediaType.APPLICATION_JSON)
    public String scorec(@QueryParam("contest_ids") String contestIds,
	    @QueryParam("format") @DefaultValue("json") String format) throws JSONException {

	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	try {
	    start();
	    CustomResponse response = fbContestService.findFbScoreByContestIds(contestIds);
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
     * 比赛最终分数
     * 
     * @param contestIds
     * @param format
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/score/final")
    @Produces(MediaType.APPLICATION_JSON)
    public String finalScorec(@QueryParam("contest_ids") String contestIds,
	    @QueryParam("format") @DefaultValue("json") String format) throws JSONException {

	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	try {
	    start();
	    CustomResponse response = fbContestService.findFbFinalScores(contestIds);
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
     * 根据赛事id获取文字直播(endId为空则返回全部，不为空则返回大于endId的值)
     * 
     * @param contestId
     * @param endId
     * @param format
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/livewords")
    @Produces(MediaType.APPLICATION_JSON)
    public String livewords(@QueryParam("contest_id") Long contestId, @QueryParam("end_id") Long endId,
	    @QueryParam("format") @DefaultValue("json") String format) throws JSONException {

	DataResponse<FbLiveWordsListResponse> ret = new DataResponse<FbLiveWordsListResponse>();
	try {
	    start();
	    FbLiveWordsListResponse response = fbLiveWordsDubbo.findLiveWordsByContestId(contestId, endId);
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
     * 赛事技术统计
     * 
     * @param contestId
     * @param format
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/statistics")
    @Produces(MediaType.APPLICATION_JSON)
    public String statistics(@QueryParam("contest_id") Long contestId,
	    @QueryParam("format") @DefaultValue("json") String format) throws JSONException {
	DataResponse<FbStatisticsResponse> ret = new DataResponse<FbStatisticsResponse>();
	try {
	    start();
	    FbStatisticsResponse response = fbStatisticsService.findStatisticsByContestId(contestId);
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(10001);
	    ret.setMsg(InternationalResources.getInstance().locale("error.basic.serviceerror", request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 赛况接口
     * 
     * @param contestId
     * @param format
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/events")
    @Produces(MediaType.APPLICATION_JSON)
    public String events(@QueryParam("contest_id") Long contestId, @QueryParam("format") @DefaultValue("json") String format)
	    throws JSONException {
	DataResponse<FbEventsListResponse> ret = new DataResponse<FbEventsListResponse>();
	try {
	    start();
	    FbEventsListResponse response = fbContestEventService.findEventsByContestId(contestId);
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(10001);
	    ret.setMsg(InternationalResources.getInstance().locale("error.basic.serviceerror", request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 获取赛况与技术统计
     * 
     * @param contestId
     * @param format
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/statisticsandevents")
    @Produces(MediaType.APPLICATION_JSON)
    public String statisticsEvents(@QueryParam("contest_id") Long contestId,
	    @QueryParam("format") @DefaultValue("json") String format) throws JSONException {
	DataResponse<FbStatistcsEventResponse> ret = new DataResponse<FbStatistcsEventResponse>();
	try {
	    start();
	    FbStatistcsEventResponse response = fbContestService.findStatisticsAndEventByContestId(contestId);
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(10001);
	    ret.setMsg(InternationalResources.getInstance().locale("error.basic.serviceerror", request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    @GET
    @Path("/fixed")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFixedData(@QueryParam("contest_id") Long contestId,
	    @QueryParam("format") @DefaultValue("json") String format) throws JSONException {
	DataResponse<FbFixedResponse> ret = new DataResponse<FbFixedResponse>();
	try {
	    start();
	    FbFixedResponse response = fbContestService.findFbFixedDataByContestId(contestId);
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(10001);
	    ret.setMsg(InternationalResources.getInstance().locale("error.basic.serviceerror", request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    @GET
    @Path("/fbing")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNum() {
	try {
	    List<Long> ids = fbContestDubbo.findFbContestIngNum();
	    System.out.println(ids.size() + "+" + ids.toString());
	    return ids.toString();
	} finally {
	    end();
	}
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public String search(@QueryParam("search_key") String searchKey, @QueryParam("start_time") String startTime,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) {
	DataResponse<ContestListResponse> ret = new DataResponse<ContestListResponse>();
	try {
	    ContestListResponse resp = CbsContestSolrUtil.getInstance().searchFbContests(searchKey, startTime, limit);
	    ret.setData(resp);
	    ret.setCode(200);
	} catch (L99NetworkException e) {
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

    @GET
    @Path("/fbone")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFb(@QueryParam("contest_id") Long contestId) throws JSONException {

	DataResponse<ContestFullResponse> ret = new DataResponse<ContestFullResponse>();
	try {
	    start();
	    ContestFullResponse response = fbContestDubbo.findContestInfo(contestId);
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
     * 根据房间id获取赛事
     * 
     * @param roomId
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/room/contest")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBb(@QueryParam("room_id") Long roomId) throws JSONException {
	DataResponse<ContestResponse> ret = new DataResponse<ContestResponse>();
	try {
	    start();
	    ContestResponse response = fbContestService.findFbContestsByRoomId(roomId);
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

    @GET
    @Path("/record")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFbContestRecords(@QueryParam("team_id") Long teamId,
	    @QueryParam("limit") @DefaultValue("5") Integer limit) throws JSONException {
	DataResponse<RecordListResponse> ret = new DataResponse<RecordListResponse>();
	try {
	    start();
	    RecordListResponse response = fbContestRecordService.selectTeamRecord(teamId, limit);
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
     * 根据主客队查找赛事
     * 
     * @param homeTeam
     * @param awayTeam
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/team")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFbByTeam(@QueryParam("home_team") Long homeTeam, @QueryParam("away_team") Long awayTeam,
	    @QueryParam("time") String time) throws JSONException {
	DataResponse<ContestResponse> ret = new DataResponse<ContestResponse>();
	try {
	    start();
	    ContestResponse response = fbContestService.findContestsByTeam(homeTeam, awayTeam, time);
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

    @GET
    @Path("/about/record")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBbPreRecords(@QueryParam("home_team_id") Long homeTeamId, @QueryParam("away_team_id") Long awayTeamId,
	    @QueryParam("limit") @DefaultValue("5") Integer limit) throws JSONException {
	DataResponse<AboutRecordListResponse> ret = new DataResponse<AboutRecordListResponse>();
	try {
	    start();
	    AboutRecordListResponse aboutResponse = fbContestRecordService.selectAboutRecord(homeTeamId, awayTeamId, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(aboutResponse);
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

    public void setFbContestService(FbContestService fbContestService) {
	this.fbContestService = fbContestService;
    }

}
