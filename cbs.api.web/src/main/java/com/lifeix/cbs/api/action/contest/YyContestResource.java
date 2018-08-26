package com.lifeix.cbs.api.action.contest;

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
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.api.util.IPUtil;
import com.lifeix.cbs.contest.bean.circle.FriendCircleListResponse;
import com.lifeix.cbs.contest.bean.circle.FriendCircleResponse;
import com.lifeix.cbs.contest.bean.yy.YyBetListResponse;
import com.lifeix.cbs.contest.bean.yy.YyContestListResponse;
import com.lifeix.cbs.contest.bean.yy.YyCupListResponse;
import com.lifeix.cbs.contest.bean.yy.YyOddsResponse;
import com.lifeix.cbs.contest.service.cirle.FriendCircleService;
import com.lifeix.cbs.contest.service.yy.YyBetService;
import com.lifeix.cbs.contest.service.yy.YyContestService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

@Controller
@Path("/yy/contest")
public class YyContestResource extends BaseAction {

    @Autowired
    private YyContestService yyContestService;

    @Autowired
    private FriendCircleService friendCircleService;

    @Autowired
    private YyBetService yyBetService;

    /**
     * 押押分类列表
     * 
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/cups")
    @Produces(MediaType.APPLICATION_JSON)
    public String cups() throws JSONException {
	DataResponse<YyCupListResponse> ret = new DataResponse<YyCupListResponse>();
	try {
	    start();
	    YyCupListResponse response = yyContestService.findYyCups();
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
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
     * 押押赛事列表
     * 
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("longbi") @DefaultValue("false") Boolean longbi, @QueryParam("cup_id") Long cupId,
	    @QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {
	DataResponse<YyContestListResponse> ret = new DataResponse<YyContestListResponse>();
	try {
	    start();
	    YyContestListResponse response = yyContestService.findBetYyContests(longbi, cupId, getSessionAccount(request),
		    startId, limit);
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
     * 押押的下单列表
     * 
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/history")
    @Produces(MediaType.APPLICATION_JSON)
    public String history(@QueryParam("contest_id") Long contestId, @QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) throws JSONException {
	DataResponse<YyBetListResponse> ret = new DataResponse<YyBetListResponse>();
	try {
	    start();
	    YyBetListResponse response = yyContestService.findContestBets(contestId, startId, limit);
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
     * 押押的单个下注详情
     */
    @GET
    @Path("/bet/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String history(@QueryParam("id") Long id) throws JSONException {
	DataResponse<FriendCircleResponse> ret = new DataResponse<FriendCircleResponse>();
	try {
	    start();
	    FriendCircleResponse response = friendCircleService.findOneYayaCircle(id);
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
     * 我的押押赛事列表
     * 
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public String user(@QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {
	DataResponse<FriendCircleListResponse> ret = new DataResponse<FriendCircleListResponse>();
	try {
	    start();
	    FriendCircleListResponse response = friendCircleService.getMyYayaCircle(getSessionAccount(request), startId,
		    limit);
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
     * 我的下单记录
     * 
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/bets")
    @Produces(MediaType.APPLICATION_JSON)
    public String bets(@QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {
	DataResponse<YyBetListResponse> ret = new DataResponse<YyBetListResponse>();
	try {
	    start();
	    YyBetListResponse response = yyContestService.findUserYyContests(getSessionAccount(request), startId, limit);
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
     * 押押赛事与下单信息
     * 
     * @param id
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/odds")
    @Produces(MediaType.APPLICATION_JSON)
    public String odds(@QueryParam("id") Long id, @QueryParam("from") String from) throws JSONException {
	DataResponse<YyOddsResponse> ret = new DataResponse<YyOddsResponse>();
	try {
	    start();
	    YyOddsResponse response = yyContestService.oddsYyContest(id, getSessionAccount(request), from);
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
     * 下单押押
     * 
     * @param format
     * @param contestId
     * @param longbi
     * @param support
     * @param roi
     * @param bet
     * @param content
     * @param client
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/bet")
    @Produces(MediaType.APPLICATION_JSON)
    public String bet(@FormParam("format") @DefaultValue("json") String format, @FormParam("contest_id") Long contestId,
	    @FormParam("longbi") @DefaultValue("false") Boolean longbi, @FormParam("support") Integer support,
	    @FormParam("roi") Double roi, @FormParam("bet") Double bet, @FormParam("content") String content,
	    @FormParam("client") String client, @FormParam("coupon_id") Long couponId, @FormParam("from") String from)
	    throws JSONException {
	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	try {
	    start();
	    CustomResponse response = yyBetService.addYyBet(getSessionAccount(request), contestId, longbi, support, roi,
		    bet, content, couponId, client, IPUtil.getIpAddr(request), from);
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
}
