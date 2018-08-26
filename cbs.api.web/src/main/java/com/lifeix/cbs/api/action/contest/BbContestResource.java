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
import com.lifeix.cbs.contest.bean.bb.BbChangeDataResponse;
import com.lifeix.cbs.contest.bean.bb.BbFixedResponse;
import com.lifeix.cbs.contest.bean.bb.BbLiveWordsListResponse;
import com.lifeix.cbs.contest.bean.fb.ContestFullResponse;
import com.lifeix.cbs.contest.bean.fb.ContestListResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.fb.ext.AboutRecordListResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordListResponse;
import com.lifeix.cbs.contest.service.bb.BbContestRecordService;
import com.lifeix.cbs.contest.service.bb.BbContestService;
import com.lifeix.cbs.contest.service.spark.contest.BbContestDubbo;
import com.lifeix.cbs.contest.service.spark.live.BbLiveWordsDubbo;
import com.lifeix.cbs.contest.util.CbsContestSolrUtil;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.user.beans.CustomResponse;

@Controller
@Path("/bb/contest")
public class BbContestResource extends BaseAction {

    @Autowired
    private BbContestService bbContestService;

    @Autowired
    private BbContestDubbo bbContestDubbo;

    @Autowired
    private BbLiveWordsDubbo bbLiveWordsDubbo;

    @Autowired
    private BbContestRecordService bbContestRecordService;

    /**
     * 比赛信息
     * 
     * @param time
     * @param fullFlag
     * @param format
     * @param client
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/time")
    @Produces(MediaType.APPLICATION_JSON)
    public String time(@QueryParam("time") String time, @QueryParam("full_flag") @DefaultValue("false") Boolean fullFlag,
	    @QueryParam("format") @DefaultValue("json") String format, @HeaderParam("client") String client)
	    throws JSONException {
	DataResponse<ContestListResponse> ret = new DataResponse<ContestListResponse>();
	try {
	    start();
	    ContestListResponse response = bbContestService.findBbContestsByTime(time, fullFlag, client);
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
     * 大赢家赛事列表 返回重要比赛
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
	    ContestListResponse response = bbContestDubbo.findBbContestsV2ForCbs(startTime, endTime);
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
	    CustomResponse response = bbContestService.findBbScoreByContestIds(contestIds);
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
    @Path("/bbing")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNum() {
	try {
	    List<Long> ids = bbContestDubbo.findBbContestIngNum();
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
	    ContestListResponse resp = CbsContestSolrUtil.getInstance().searchBbContests(searchKey, startTime, limit);
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
	    @QueryParam("new_flag") @DefaultValue("false") Boolean newFlag,
	    @QueryParam("format") @DefaultValue("json") String format) throws JSONException {

	DataResponse<BbLiveWordsListResponse> ret = new DataResponse<BbLiveWordsListResponse>();
	try {
	    start();
	    BbLiveWordsListResponse response = bbLiveWordsDubbo.findLiveWordsByContestId(contestId, endId, newFlag);
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
     * 获取赛况与技术统计
     * 
     * @param contestId
     * @param format
     * @param lineupFlag
     *            是否返回阵容
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/change")
    @Produces(MediaType.APPLICATION_JSON)
    public String statisticsEvents(@QueryParam("contest_id") Long contestId,
	    @QueryParam("lineup_flag") @DefaultValue("false") Boolean lineupFlag,
	    @QueryParam("format") @DefaultValue("json") String format) throws JSONException {
	DataResponse<BbChangeDataResponse> ret = new DataResponse<BbChangeDataResponse>();
	try {
	    start();
	    BbChangeDataResponse response = bbContestService.findBbChangeDataResponseByContestId(contestId, lineupFlag);
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
	DataResponse<BbFixedResponse> ret = new DataResponse<BbFixedResponse>();
	try {
	    start();
	    BbFixedResponse response = bbContestService.findBbFixedDataByContestId(contestId);
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
     * web端H5调用接口
     * 
     * @param contestId
     * @param lineupFlag
     * @param format
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/bbone")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBb(@QueryParam("contest_id") Long contestId,
	    @QueryParam("lineup_flag") @DefaultValue("false") Boolean lineupFlag,
	    @QueryParam("new_flag") @DefaultValue("false") Boolean newFlag,
	    @QueryParam("format") @DefaultValue("json") String format) throws JSONException {
	DataResponse<ContestFullResponse> ret = new DataResponse<ContestFullResponse>();
	try {
	    start();
	    ContestFullResponse response = bbContestDubbo.findBbContestInfo(contestId, newFlag);
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
	    ContestResponse response = bbContestService.findBbContestsByRoomId(roomId);
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
    public String getBbContestRecords(@QueryParam("team_id") Long teamId,
	    @QueryParam("limit") @DefaultValue("5") Integer limit) throws JSONException {
	DataResponse<RecordListResponse> ret = new DataResponse<RecordListResponse>();
	try {
	    start();
	    RecordListResponse response = bbContestRecordService.selectTeamRecord(teamId, limit);
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
    public String getBbByTeam(@QueryParam("home_team") Long homeTeam, @QueryParam("away_team") Long awayTeam,
	    @QueryParam("time") String time) throws JSONException {
	DataResponse<ContestResponse> ret = new DataResponse<ContestResponse>();
	try {
	    start();
	    ContestResponse response = bbContestService.findBbContestsByTeam(homeTeam, awayTeam, time);
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
	    AboutRecordListResponse aboutResponse = bbContestRecordService.selectAboutRecord(homeTeamId, awayTeamId, limit);
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

    public void setBbContestService(BbContestService bbContestService) {
	this.bbContestService = bbContestService;
    }

}
