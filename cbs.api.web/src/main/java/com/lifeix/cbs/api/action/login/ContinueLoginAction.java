package com.lifeix.cbs.api.action.login;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.bean.user.UserLoginResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.user.CbsUserLoginService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Controller
@Path("/login")
public class ContinueLoginAction extends BaseAction {

    @Autowired
    private CbsUserService cbsUserService;

    @Autowired
    private CbsUserLoginService cbsUserLoginService;

    /**
     * 旧接口
     * 获取用户已经连续登录天数，未领奖，领奖的接口在 CbsUserAction.continueLogin
     * @param userId
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/continue")
    @Produces(MediaType.APPLICATION_JSON)
    public String time(@QueryParam("user_id") Long userId) throws JSONException {
        start();
        DataResponse<CustomResponse> ret = new DataResponse<>();
        CustomResponse response;
        try {
            response = cbsUserService.continueLoginTimes(userId);
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
     * 版本4.1 新增
     * 登陆调用的接口，显示用户该天是否登陆领取和累计登陆天数
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/sum")
    @Produces(MediaType.APPLICATION_JSON)
    public String continueLogin() throws JSONException {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            UserLoginResponse userLoginResponse = cbsUserLoginService.loginDays(getSessionAccount(request));
            ret.setCode(DataResponse.OK);
            ret.setData(userLoginResponse);
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
     * 版本4.1 新增
     * 用户登录领取奖励
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/receive")
    @Produces(MediaType.APPLICATION_JSON)
    public String receive() throws JSONException {
        start();
        DataResponse<Object> ret = new DataResponse<>();
        try {
            cbsUserLoginService.receive(getSessionAccount(request));
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
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
