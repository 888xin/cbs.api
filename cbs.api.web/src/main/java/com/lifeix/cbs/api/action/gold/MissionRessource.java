package com.lifeix.cbs.api.action.gold;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.bean.mission.MissionUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.mission.Mission;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.misson.MissionUserService;
import com.lifeix.cbs.api.util.DataResponseFormat;
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
 * Created by lhx on 16-6-20 下午2:42
 *
 * @Description
 */
@Controller
@Path("/mission")
public class MissionRessource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(MissionRessource.class);

    @Autowired
    private MissionUserService missionUserService;

    /**
     * 用户任务完成情况列表
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list() throws JSONException {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            MissionUserResponse rep = missionUserService.getUserInfo(getSessionAccount(request));
            ret.setCode(DataResponse.OK);
            ret.setData(rep);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
            return DataResponseFormat.response(ret);
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
     * 积分兑换龙筹列表
     */
    @GET
    @Path("/reward/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String rewardList() throws JSONException {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            MissionUserResponse rep = missionUserService.getPointList(getSessionAccount(request));
            ret.setCode(DataResponse.OK);
            ret.setData(rep);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
            return DataResponseFormat.response(ret);
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
     * 积分换取龙筹
     */
    @POST
    @Path("/reward/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String oper(@FormParam("id") Long id) {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            missionUserService.consume(getSessionAccount(request), id);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
            return DataResponseFormat.response(ret);
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
     * 客户端验证
     */
    @POST
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    public String share(@FormParam("value") Integer value, @FormParam("type") Integer type) {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            MissionUserResponse missionUserResponse = missionUserService.validate(getSessionAccount(request), value, type);
            ret.setData(missionUserResponse);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
            return DataResponseFormat.response(ret);
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
     * 分享到微信调用
     */
    @POST
    @Path("/share/weixin")
    @Produces(MediaType.APPLICATION_JSON)
    public String share() {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            missionUserService.validate(getSessionAccount(request), Mission.SHARE_TO_WEIXIN);
            ret.setCode(DataResponse.OK);
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
