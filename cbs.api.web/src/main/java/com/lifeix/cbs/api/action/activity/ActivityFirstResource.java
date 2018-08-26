package com.lifeix.cbs.api.action.activity;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.cbs.activity.bean.first.ActivityFirstResponse;
import com.lifeix.cbs.activity.service.first.ActivityFirstLogService;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.commons.beans.DataResponse;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/activity/first")
public class ActivityFirstResource extends BaseAction {

    @Autowired
    private ActivityFirstLogService activityFirstLogService;

    /**
     * 查询有误抽奖机会
     * 
     * @return
     */
    @GET
    @Path("/check")
    @Produces(MediaType.APPLICATION_JSON)
    public String check() {
	start();
	DataResponse<ActivityFirstResponse> ret = new DataResponse<ActivityFirstResponse>();
	try {
	    ActivityFirstResponse response = activityFirstLogService.check(getSessionAccount(request));
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
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
     * 首充活动中奖列表
     * 
     * @return
     */
    @GET
    @Path("/rewardlog")
    @Produces(MediaType.APPLICATION_JSON)
    public String rewardlog() {
	start();
	DataResponse<List<String>> ret = new DataResponse<List<String>>();
	try {
	    List<String> list = activityFirstLogService.getRewardLogList();
	    ret.setCode(DataResponse.OK);
	    ret.setData(list);
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
     * 首充活动抽奖
     * 
     * @return
     */
    @GET
    @Path("/lottery")
    @Produces(MediaType.APPLICATION_JSON)
    public String firstLottery() {
	start();
	DataResponse<ActivityFirstResponse> ret = new DataResponse<ActivityFirstResponse>();
	try {
	    ActivityFirstResponse response = activityFirstLogService.addFirstLog(getSessionAccount(request));
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
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
}
