package com.lifeix.cbs.api.action.rank;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.bean.user.UserGraphListResponse;
import com.lifeix.cbs.api.bean.user.UserStatisticsListResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.spark.CbsRankDubbo;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/rank")
public class CbsRankAction extends BaseAction {

    @Autowired
    private CbsRankDubbo cbsRankDubbo;

    /**
     * true 总榜 false 周榜
     */
    static AtomicBoolean rankType = new AtomicBoolean(true);

    /**
     * 旧版本获取胜率或盈利榜接口
     * 
     * @param format
     * @param startId
     * @param type
     * @param limit
     * @return
     */
    @GET
    @Path("/top")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRankTop(@QueryParam("format") @DefaultValue("json") String format,
	    @QueryParam("start_id") Integer startId, @QueryParam("type") @DefaultValue("1") Integer type,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) {
	DataResponse<UserStatisticsListResponse> ret = new DataResponse<UserStatisticsListResponse>();
	try {
	    start();
	    UserStatisticsListResponse resp = null;
	    switch (type) {
	    case 1: {
		if (rankType.get()) {
		    resp = cbsRankDubbo.getWinningTop(startId, limit, getSessionAccount(request));
		} else {
		    resp = cbsRankDubbo.getWinningWeek(null, null, startId, limit, getSessionAccount(request));
		}
		break;
	    }
	    case 2: {
		if (rankType.get()) {
		    resp = cbsRankDubbo.getRoiTop(startId, limit, getSessionAccount(request));
		} else {
		    resp = cbsRankDubbo.getRoiWeek(null, null, startId, limit, getSessionAccount(request));
		}
		break;
	    }
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
     * 胜率总榜
     * 
     * @param nowPage
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/winning/top")
    @Produces(MediaType.APPLICATION_JSON)
    public String winningTop(@QueryParam("now_page") Integer nowPage, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {
	DataResponse<UserStatisticsListResponse> ret = new DataResponse<UserStatisticsListResponse>();
	try {
	    start();
	    UserStatisticsListResponse resp = cbsRankDubbo.getWinningTop(nowPage, limit, getSessionAccount(request));
	    ret.setCode(DataResponse.OK);
	    ret.setData(resp);
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 胜率周榜
     * 
     * @param nowPage
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/winning/week")
    @Produces(MediaType.APPLICATION_JSON)
    public String winningWeek(@QueryParam("year") Integer year, @QueryParam("week") Integer week,
	    @QueryParam("now_page") Integer nowPage, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {
	DataResponse<UserStatisticsListResponse> ret = new DataResponse<UserStatisticsListResponse>();
	try {
	    start();
	    UserStatisticsListResponse resp = cbsRankDubbo.getWinningWeek(year, week, nowPage, limit,
		    getSessionAccount(request));
	    ret.setCode(DataResponse.OK);
	    ret.setData(resp);
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 回报率总榜
     * 
     * @param nowPage
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/roi/top")
    @Produces(MediaType.APPLICATION_JSON)
    public String roiTop(@QueryParam("now_page") Integer nowPage, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {
	DataResponse<UserStatisticsListResponse> ret = new DataResponse<UserStatisticsListResponse>();
	try {
	    start();
	    UserStatisticsListResponse resp = cbsRankDubbo.getRoiTop(nowPage, limit, getSessionAccount(request));
	    ret.setCode(DataResponse.OK);
	    ret.setData(resp);
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 回报率周榜
     * 
     * @param nowPage
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/roi/week")
    @Produces(MediaType.APPLICATION_JSON)
    public String roiWeek(@QueryParam("year") Integer year, @QueryParam("week") Integer week,
	    @QueryParam("now_page") Integer nowPage, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {
	DataResponse<UserStatisticsListResponse> ret = new DataResponse<UserStatisticsListResponse>();
	try {
	    start();
	    UserStatisticsListResponse resp = cbsRankDubbo
		    .getRoiWeek(year, week, nowPage, limit, getSessionAccount(request));
	    ret.setCode(DataResponse.OK);
	    ret.setData(resp);
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 用户数据曲线图(按周)
     */
    @GET
    @Path("/user/graph")
    @Produces(MediaType.APPLICATION_JSON)
    public String userBetGraph(@QueryParam("user_id") Long userId, @QueryParam("start_time") String startTime,
	    @QueryParam("end_time") String endTime, @QueryParam("limit") @DefaultValue("31") Integer limit)
	    throws JSONException {
	DataResponse<UserGraphListResponse> ret = new DataResponse<UserGraphListResponse>();
	try {
	    start();
	    UserGraphListResponse resp = null;
	    resp = cbsRankDubbo.userBetGraph(userId, startTime, endTime, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(resp);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 设置用户可见榜单的类型
     * 
     * @param type
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/setting")
    @Produces(MediaType.APPLICATION_JSON)
    public String setting(@QueryParam("type") @DefaultValue("true") Boolean type) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    rankType.set(type);
	    ret.setCode(DataResponse.OK);
	    ret.setData(rankType.get());
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

}
