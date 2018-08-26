package com.lifeix.cbs.api.action.game;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.content.bean.game.ZodiacAnimalListResponse;
import com.lifeix.cbs.content.bean.game.ZodiacAnimalResponse;
import com.lifeix.cbs.content.bean.game.ZodiacPlayListResponse;
import com.lifeix.cbs.content.bean.game.ZodiacPlayResponse;
import com.lifeix.cbs.content.service.game.ZodiacAnimalIssueService;
import com.lifeix.cbs.content.service.spark.game.ZodiacAnimalDubbo;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by lhx on 15-12-14 上午9:45
 *
 * @Description
 */
@Controller
@Path("/zodiac")
public class ZodiacAnimalResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(ZodiacAnimalResource.class);

    @Autowired
    ZodiacAnimalDubbo zodiacAnimalDubbo;

    @Autowired
    private ZodiacAnimalIssueService zodiacAnimalIssueService;

    @GET
    @Path("/index")
    @Produces(MediaType.APPLICATION_JSON)
    public String index() throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            Long userId = getSessionAccount(request);
            ZodiacAnimalResponse zodiacAnimalResponse = zodiacAnimalIssueService.getNowIssue(userId, false);
            ret.setCode(DataResponse.OK);
            ret.setData(zodiacAnimalResponse);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (L99IllegalDataException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    /**
     * 真正对下单的生肖进行扣钱等操作
     */
    @POST
    @Path("/bet")
    @Produces(MediaType.APPLICATION_JSON)
    public String bet(@FormParam("bets") String bets, @FormParam("game_id") Integer gameId,
                      @FormParam("user_coupon_ids") String userCouponIds) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            Long userId = getSessionAccount(request);
            ZodiacPlayResponse zodiacPlayResponse = zodiacAnimalIssueService.bet(userId, gameId, bets, userCouponIds, false);
            ret.setCode(DataResponse.OK);
            ret.setData(zodiacPlayResponse);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (L99IllegalDataException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getList(@QueryParam("limit") @DefaultValue("10") Integer limit, @QueryParam("start_id") Integer startId)
            throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            Long userId = getSessionAccount(request);
            ZodiacPlayListResponse zodiacPlayListResponses = zodiacAnimalIssueService.getUserPlayHistorys(userId, startId,
                    limit);
            ret.setCode(DataResponse.OK);
            ret.setData(zodiacPlayListResponses);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    @GET
    @Path("/createZodiacAnimalIssue")
    @Produces(MediaType.APPLICATION_JSON)
    public String createZodiacAnimalIssue() throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            zodiacAnimalDubbo.createZodiacAnimalIssue();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    /**
     * 派奖（开奖）
     */
    @GET
    @Path("/toPrize")
    @Produces(MediaType.APPLICATION_JSON)
    public String toPize() throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            zodiacAnimalDubbo.toPrize();
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
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    @POST
    @Path("/open")
    @Produces(MediaType.APPLICATION_JSON)
    public String open(@FormParam("game_id") Integer gameId) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            ZodiacAnimalResponse zodiacAnimalResponse = zodiacAnimalIssueService.toPrize(gameId);
            ret.setCode(DataResponse.OK);
            ret.setData(zodiacAnimalResponse);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (L99IllegalDataException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    @GET
    @Path("/openGames")
    @Produces(MediaType.APPLICATION_JSON)
    public String openGames(@QueryParam("start_time") String startTime, @QueryParam("end_time") String endTime)
            throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            zodiacAnimalIssueService.toPrize(startTime, endTime);
            ret.setCode(DataResponse.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    /**
     * 该期游戏未开奖前统计每个生肖被下单多少次
     * （用户对生肖下单没有真正进入扣钱，只有在停止下单的时候才算真正下单成功）
     */
    @POST
    @Path("/change")
    @Produces(MediaType.APPLICATION_JSON)
    public String changeBetNum(@FormParam("num") Long num, @FormParam("game_id") Integer gameId, @FormParam("no") String no)
            throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            zodiacAnimalIssueService.changeBetNum(gameId, no, num);
            ret.setCode(DataResponse.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    /**
     * 获得每个生肖的下单数
     */
    @GET
    @Path("/get/number")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNumber(@QueryParam("game_id") Integer gameId) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            ZodiacAnimalResponse zodiacAnimalResponse = zodiacAnimalIssueService.getBetNum(gameId);
            ret.setCode(DataResponse.OK);
            ret.setData(zodiacAnimalResponse);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    /**
     * 获取当前期中奖号码
     */
    @GET
    @Path("/get/win")
    @Produces(MediaType.APPLICATION_JSON)
    public String getWinNumber(@QueryParam("game_id") Integer gameId) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            ZodiacAnimalResponse zodiacAnimalResponse = zodiacAnimalIssueService.getCurrentWinner(gameId);
            ret.setCode(DataResponse.OK);
            ret.setData(zodiacAnimalResponse);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    /**
     * 获得往期的中奖记录
     */
    @GET
    @Path("/get/past")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPostNumber(@QueryParam("limit") @DefaultValue("10") Integer limit,
                                @QueryParam("start_id") Integer startId) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            ZodiacAnimalListResponse zodiacAnimalListResponse = zodiacAnimalIssueService.getPlayHistorys(startId, limit);
            ret.setCode(DataResponse.OK);
            ret.setData(zodiacAnimalListResponse);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

}