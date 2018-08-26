package com.lifeix.cbs.api.action.inner;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.bean.mission.MissionUserListResponse;
import com.lifeix.cbs.api.bean.mission.MissionUserResponse;
import com.lifeix.cbs.api.bean.mission.PointResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.misson.MissionUserService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.exception.service.L99IllegalParamsException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Created by lhx on 16-6-20 上午11:37
 *
 * @Description
 */
@Controller
@Path("/inner/mission")
public class MissionInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(MissionInnerResource.class);

    @Autowired
    private MissionUserService missionUserService ;

    /**
     * 暂时删除的每日任务或者恢复
     * @param value 枚举类的值 正数为添加，负数为去除
     * @return
     */
    @POST
    @Path("/oper")
    @Produces(MediaType.APPLICATION_JSON)
    public String oper(@FormParam("value") Integer value) {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            missionUserService.operDayMission(value);
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

    /**
     * 获取取消的每日任务
     */
    @GET
    @Path("/get/cancel")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCancel() throws JSONException {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            String rep = missionUserService.getCancelDayMission();
            ret.setCode(DataResponse.OK);
            ret.setData(rep);
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
     * 操作积分换龙筹
     */
    @POST
    @Path("/oper/reward")
    @Produces(MediaType.APPLICATION_JSON)
    public String saveReward(@FormParam("id") Long id, @FormParam("point") Integer point, @FormParam("gold") Integer gold) {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            PointResponse pointResponse = missionUserService.operReward(id, point, gold);
            ret.setCode(DataResponse.OK);
            ret.setData(pointResponse);
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
     * 删除积分换龙筹
     */
    @POST
    @Path("/delete/reward")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteReward(@FormParam("id") Long id) {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            missionUserService.deleteReward(id);
            ret.setCode(DataResponse.OK);
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
     * 修改用户积分和值
     * @param userId 用户id
     * @param amount 正数为添加，负数为减
     * @param value 积分值
     * @return
     */
    @POST
    @Path("/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String edit(@FormParam("user_id") Long userId, @FormParam("amount") Integer amount, @FormParam("value") Integer value) {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            missionUserService.edit(userId, amount, value);
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
     * 列表
     */
    @GET
    @Path("/user/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("page") Integer page, @QueryParam("limit") @DefaultValue("30") Integer limit) throws JSONException {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            MissionUserListResponse rep = missionUserService.getUserListInfo(page, limit);
            ret.setCode(DataResponse.OK);
            ret.setData(rep);
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
     * 用户每日任务完成情况
     */
    @GET
    @Path("/user/day")
    @Produces(MediaType.APPLICATION_JSON)
    public String day(@QueryParam("day") String day, @QueryParam("user_id") Long userId) throws JSONException {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            MissionUserResponse rep = missionUserService.getUserDayInfo(day, userId);
            ret.setCode(DataResponse.OK);
            ret.setData(rep);
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
     * 查询指定用户
     */
    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public String user(@QueryParam("user_id") Long userId) throws JSONException {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            MissionUserResponse rep = missionUserService.getUserInfoInner(userId);
            ret.setCode(DataResponse.OK);
            ret.setData(rep);
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
            MissionUserResponse rep = missionUserService.getPointListInner();
            ret.setCode(DataResponse.OK);
            ret.setData(rep);
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
