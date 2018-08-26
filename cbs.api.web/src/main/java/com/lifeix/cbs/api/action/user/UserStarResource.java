package com.lifeix.cbs.api.action.user;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.bean.user.CbsUserStarListResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.user.CbsUserStarService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by lhx on 16-2-16 下午3:26
 *
 * @Description
 */
@Controller
@Path("/user/star")
public class UserStarResource extends BaseAction {

    @Autowired
    private CbsUserStarService cbsUserStarService;

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String get() throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            CbsUserStarListResponse data = cbsUserStarService.findStars(getSessionAccount(request));
            ret.setCode(DataResponse.OK);
            ret.setData(data);
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
