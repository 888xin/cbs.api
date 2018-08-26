package com.lifeix.cbs.api.action.inner;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.bean.money.MoneyUserStatisticListResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.money.MoneyStatisticService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.statistic.BetStatisticResponse;
import com.lifeix.cbs.contest.service.spark.statistic.BetStatisticDubbo;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 15-12-22 下午4:40
 *
 * @Description
 */
@Controller
@Path("/inner/statistic")
public class StatisticInnerResource extends BaseAction {

    @Autowired
    BetStatisticDubbo betStatisticDubbo;

    @Autowired
    protected MoneyStatisticService moneyStatisticService;

    @GET
    @Path("/bet/get/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String time(@QueryParam("date") String date, @QueryParam("limit") @DefaultValue("20") Integer limit)
            throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            BetStatisticResponse betStatisticResponse = betStatisticDubbo.getStatistic(date, limit);
            ret.setData(betStatisticResponse);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException e) {
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
     * 初始化下单数据统计
     *
     * @param date 日期
     * @param type 1 每日下单数; 2 每日下单人数; 3 胜负下单数; 4 让球下单数;5 足球下单数;6 篮球下单数;
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/push")
    @Produces(MediaType.APPLICATION_JSON)
    public String push(@FormParam("date") String date, @FormParam("type") @DefaultValue("1") Integer type)
            throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            if (type == 1) {
                betStatisticDubbo.betsStatistic(date);
            } else if (type == 2) {
                betStatisticDubbo.peopleStatistic(date);
            } else if (type == 3) {
                betStatisticDubbo.opStatistic(date);
            } else if (type == 4) {
                betStatisticDubbo.jcStatistic(date);
            } else if (type == 5) {
                betStatisticDubbo.fbStatistic(date);
            } else if (type == 6) {
                betStatisticDubbo.bbStatistic(date);
            }
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
     * 获取用户的龙币消费记录
     *
     * @param date  yyMMdd
     * @param type  0收入 1支出 null全部
     * @param order 0 add desc 1 add asc 2 minus desc 3 minus asc
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/user/money")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserMoneyStatistic(@QueryParam("date") String date, @QueryParam("type") Integer type,
                                        @QueryParam("order") @DefaultValue("0") Integer order) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            MoneyUserStatisticListResponse moneyUserStatisticListResponse = moneyStatisticService.getUsersDayConsumer(date,
                    type, order);
            ret.setCode(DataResponse.OK);
            ret.setData(moneyUserStatisticListResponse);
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
     * 创建指定日期的龙币支出与收入记录
     *
     * @param date
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/build/money/day")
    @Produces(MediaType.APPLICATION_JSON)
    public String buildMoneyDay(@FormParam("date") String date) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            moneyStatisticService.buildMoneyDayStatistic(date);
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
}
