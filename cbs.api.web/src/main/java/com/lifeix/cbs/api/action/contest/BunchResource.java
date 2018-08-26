package com.lifeix.cbs.api.action.contest;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.bunch.BunchBetListResponse;
import com.lifeix.cbs.contest.bean.bunch.BunchContestListResponse;
import com.lifeix.cbs.contest.bean.bunch.BunchContestResponse;
import com.lifeix.cbs.contest.service.bunch.BunchBetService;
import com.lifeix.cbs.contest.service.bunch.BunchContestService;
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
 * Created by lhx on 16-5-17 下午3:28
 *
 * @Description
 */
@Controller
@Path("/bunch")
public class BunchResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(BunchResource.class);

    @Autowired
    private BunchContestService bunchContestService;

    @Autowired
    private BunchBetService bunchBetService;

    /**
     * 获得赛事串列表（客户端用）
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("status") Integer status,@QueryParam("start_id") Long startId,@QueryParam("limit") @DefaultValue("20") Integer limit) {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            BunchContestListResponse bunchContestListResponse = bunchContestService.getList(status, startId, limit);
            ret.setCode(DataResponse.OK);
            ret.setData(bunchContestListResponse);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(MsgCode.BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    /**
     * 浏览单个串，包括玩法对应的赔率和用户是否下注和下注的选项
     */
    @GET
    @Path("/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String view(@QueryParam("id") Long id) {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            BunchContestResponse bunchContestResponse = bunchContestService.view(id, getSessionAccount(request));
            ret.setCode(DataResponse.OK);
            ret.setData(bunchContestResponse);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(MsgCode.BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }


    /**
     * 下注
     * @param bunchId 串id
     * @param supports 下注支持，用数组表示，如：[0,1,2]，数组下标对应赛事详情里面的index，这个index从0开始
     *                 index表示赛事的序号，support表示支持项，值0表示篮球足球的主胜（包括让球主胜，单数，大球），值2表示足球的平局，
     *                 值1表示篮球足球的客胜（包括让球客胜，双数，小球）
     */
    @POST
    @Path("/bet")
    @Produces(MediaType.APPLICATION_JSON)
    public String add(@FormParam("bunch_id") Long bunchId, @FormParam("supports") String supports,
                      @FormParam("coupon_id") Long userCouponId) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            bunchBetService.insert(getSessionAccount(request), bunchId, supports, userCouponId);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
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
     * 获取该期中奖记录（客户端用）
     */
    @GET
    @Path("/awards")
    @Produces(MediaType.APPLICATION_JSON)
    public String awardsList(@QueryParam("bunch_id") Long bunchId, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("limit") @DefaultValue("20") int limit) {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            BunchBetListResponse bunchBetListResponse = bunchBetService.getAwardsList(bunchId, ContestConstants.BunchBetStatus.GET_REWARD, page, limit);
            ret.setCode(DataResponse.OK);
            ret.setData(bunchBetListResponse);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(MsgCode.BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    /**
     * 获取个人下注记录列表
     */
    @GET
    @Path("/bet/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String betList(@QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") int limit) {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            BunchBetListResponse bunchBetListResponse = bunchBetService.getList(getSessionAccount(request), startId, limit);
            ret.setCode(DataResponse.OK);
            ret.setData(bunchBetListResponse);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(MsgCode.BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }
}
