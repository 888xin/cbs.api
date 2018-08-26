package com.lifeix.cbs.api.action.inner;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.spark.CbsRankDubbo;
import com.lifeix.cbs.api.service.spark.UserContestTaskDubbo;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.service.cirle.FriendCircleService;
import com.lifeix.cbs.contest.service.odds.BbOddsService;
import com.lifeix.cbs.contest.service.odds.FbOddsService;
import com.lifeix.cbs.contest.service.yy.YyBetService;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

/**
 * 初始化各种数据接口
 * 
 * @author lifeix
 * 
 */
@Controller
@Path("/inner/init")
public class InitInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(InitInnerResource.class);

    @Autowired
    private FriendCircleService friendCircleService;

    @Autowired
    private FbOddsService fbOddsService;

    @Autowired
    private BbOddsService bbOddsService;

    @Autowired
    private YyBetService yyBetService;

    @Autowired
    private CbsRankDubbo cbsRankDubbo;

    @Autowired
    private UserContestTaskDubbo userContestTaskService;

    /**
     * 清理未结算的战绩
     * 
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/settle/circle")
    @Produces(MediaType.APPLICATION_JSON)
    public String settleCircle(@QueryParam("circle_ids") String circleIds) throws JSONException {
	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	try {
	    start();
	    CustomResponse data = friendCircleService.settleCircle(circleIds);
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
     * 重置下单支持统计
     * 
     * @param contestType
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/bet/count")
    @Produces(MediaType.APPLICATION_JSON)
    public String betCount(@QueryParam("contest_type") @DefaultValue("0") Integer contestType) throws JSONException {
	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	try {
	    start();
	    CustomResponse data = null;
	    if (contestType == ContestType.FOOTBALL) {
		data = fbOddsService.resetBetCount();
	    } else if (contestType == ContestType.BASKETBALL) {
		data = bbOddsService.resetBetCount();
	    } else if (contestType == ContestType.YAYA) {
		data = yyBetService.resetBetCount();
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
     * 重置赛事下单统计
     * 
     * @param contestType
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/contest/count")
    @Produces(MediaType.APPLICATION_JSON)
    public String contestCount(@QueryParam("contest_type") @DefaultValue("0") Integer contestType) throws JSONException {
	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	try {
	    start();
	    CustomResponse data = fbOddsService.resetContestCount(contestType);
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
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
     * 
     * 生成新的榜单
     * 
     * @param type
     *            1 总榜 2 周榜
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/user/statistics")
    @Produces(MediaType.APPLICATION_JSON)
    public String userStatistics(@QueryParam("type") @DefaultValue("1") Long type) throws JSONException {
	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	start();
	try {
	    CustomResponse data = null;
	    if (type == 1) {
		data = userContestTaskService.processUserContestStatisticsTask();
	    } else if (type == 2) {
		data = userContestTaskService.processUserContestStatisticsWeek(false);
	    }
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
	} catch (Exception e) {
	    ret.setCode(DataResponse.NO);
	    ret.setMsg("未知异常");
	    LOG.error(e.getMessage(), e);
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 清理总榜
     * 
     * @param type
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/clean/rank/top")
    @Produces(MediaType.APPLICATION_JSON)
    public String cleanTopRank(@QueryParam("type") Integer type) throws JSONException {
	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	try {
	    start();
	    CustomResponse data = cbsRankDubbo.resetTop(type);
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
     * 清理周榜的redis
     * 
     * @param type
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/clean/week/rank")
    @Produces(MediaType.APPLICATION_JSON)
    public String cleanWeekRank(@QueryParam("type") @DefaultValue("1") Integer type) throws JSONException {
	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	start();
	try {
	    CustomResponse data = userContestTaskService.clearWeekRankRedis(type);
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
	} catch (Exception e) {
	    ret.setCode(DataResponse.NO);
	    ret.setMsg("未知异常");
	    LOG.error(e.getMessage(), e);
	}
	return DataResponseFormat.response(ret);
    }
}
