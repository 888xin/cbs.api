package com.lifeix.cbs.api.action.inner;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.bet.BetLogListResponse;
import com.lifeix.cbs.contest.bean.bet.BetLogResponse;
import com.lifeix.cbs.contest.service.bet.BbBetService;
import com.lifeix.cbs.contest.service.bet.FbBetService;
import com.lifeix.cbs.contest.service.contest.ContestStatisticsService;
import com.lifeix.cbs.contest.service.spark.settle.BbSettleDubbo;
import com.lifeix.cbs.contest.service.spark.settle.FbSettleDubbo;
import com.lifeix.cbs.contest.service.spark.settle.SettleTaskDubbo;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * 下注信息管理接口
 *
 * @author lifeix
 */
@Controller
@Path("/inner/bet")
public class BetInnerResource extends BaseAction {

    @Autowired
    private FbSettleDubbo fbSettleService;

    @Autowired
    private BbSettleDubbo bbSettleService;

    @Autowired
    private SettleTaskDubbo settleTaskDubbo;

    @Autowired
    private FbBetService fbBetService;

    @Autowired
    private BbBetService bbBetService;

    @Autowired
    private ContestStatisticsService contestStatisticsService;

    /**
     * 取消单个足球下注
     *
     * @param id
     * @param playType
     * @param reson
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/fb/cancle")
    @Produces(MediaType.APPLICATION_JSON)
    public String fbCancle(@FormParam("id") Long id, @FormParam("play_type") Integer playType,
                           @FormParam("reason") String reason, @FormParam("admin_name") String adminName) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            fbSettleService.cancleBet(playType, id, reason, adminName);
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
     * 取消单个篮球下注
     *
     * @param id
     * @param playType
     * @param reson
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/bb/cancle")
    @Produces(MediaType.APPLICATION_JSON)
    public String bbCancle(@FormParam("id") Long id, @FormParam("play_type") Integer playType,
                           @FormParam("reason") String reason, @FormParam("admin_name") String adminName) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            bbSettleService.cancleBet(playType, id, reason, adminName);
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
     * 后台立即结算赛事
     *
     * @param contestId
     * @param type
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/settle")
    @Produces(MediaType.APPLICATION_JSON)
    public String settle(@QueryParam("contest_id") Long contestId, @QueryParam("type") @DefaultValue("0") Integer type)
            throws JSONException {
        DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
        try {
            start();
            CustomResponse data;
            if (type == ContestConstants.ContestType.FOOTBALL) {
                data = fbSettleService.settleContest(contestId);
            } else {
                data = bbSettleService.settleContest(contestId);
            }
            ret.setCode(DataResponse.OK);
            ret.setData(data);
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
     * 结算任务添加
     *
     * @param contestId
     * @param type
     * @param immediate
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/settle/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String addSettleTask(@FormParam("contest_id") Long contestId, @FormParam("type") @DefaultValue("0") Integer type,
                                @FormParam("immediate") @DefaultValue("true") Boolean immediate) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            settleTaskDubbo.addSettleTask(type, contestId, immediate);
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
     * 获取足球下单列表
     *
     * @param startId
     * @param type
     * @param contestId
     * @param userId
     * @param settle
     * @param limit
     * @param startTime
     * @param endTime
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/fb/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFbBetList(@QueryParam("start_id") Long startId, @QueryParam("type") @DefaultValue("1") Integer type,
                               @QueryParam("contest_id") Long contestId, @QueryParam("user_id") Long userId,
                               @QueryParam("settle") boolean settle, @QueryParam("limit") @DefaultValue("20") Integer limit,
                               @QueryParam("start_time") String startTime, @QueryParam("end_time") String endTime) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            BetLogListResponse betLogListResponse = fbBetService.getFbBetList(startId, contestId, type, userId, settle,
                    limit, startTime, endTime);
            ret.setCode(DataResponse.OK);
            ret.setData(betLogListResponse);
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
     * 获取篮球下单列表
     *
     * @param startId
     * @param type
     * @param contestId
     * @param userId
     * @param settle
     * @param limit
     * @param startTime
     * @param endTime
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/bb/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBbBetList(@QueryParam("start_id") Long startId, @QueryParam("type") @DefaultValue("1") Integer type,
                               @QueryParam("contest_id") Long contestId, @QueryParam("user_id") Long userId,
                               @QueryParam("settle") boolean settle, @QueryParam("limit") @DefaultValue("20") Integer limit,
                               @QueryParam("start_time") String startTime, @QueryParam("end_time") String endTime) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            BetLogListResponse betLogListResponse = bbBetService.getBbBetList(startId, contestId, type, userId, settle,
                    limit, startTime, endTime);
            ret.setCode(DataResponse.OK);
            ret.setData(betLogListResponse);
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
     * 下注赔率修复
     */
    @POST
    @Path("/repair")
    @Produces(MediaType.APPLICATION_JSON)
    public String repair(@FormParam("id") Long id, @FormParam("play_type") Integer playType,
                         @FormParam("repair_roi") Double repairRoi,@FormParam("reason") String reason) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            BetLogResponse betLogResponse;
            if (playType < 6) {
                betLogResponse = fbBetService.repair(id, playType, repairRoi, reason);
            } else {
                betLogResponse = bbBetService.repair(id, playType, repairRoi, reason);
            }
            ret.setCode(DataResponse.OK);
            ret.setData(betLogResponse);
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
     * 超额下注
     */
    @GET
    @Path("/much/money")
    @Produces(MediaType.APPLICATION_JSON)
    public String muchMoney() throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            String[] results = contestStatisticsService.getMuchBetInfo();
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
     * 超额下注
     */
    @POST
    @Path("/much/money/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public String muchMoneyRefresh() throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            contestStatisticsService.refreshMuchBet();
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
