package com.lifeix.cbs.api.action.notify;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.message.bean.message.PushCountResponse;
import com.lifeix.cbs.message.bean.notify.NotifyListResponse;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.cbs.message.service.spark.PushMessageTaskDubbo;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/notify")
public class NotifyAction extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(NotifyAction.class);
    @Autowired
    NotifyService notifyService;
    @Autowired
    PushMessageTaskDubbo pushMessageTaskService;

    /**
     * 外部系统调用添加消息提醒
     * 
     * @param addType
     * @param notifyData
     * @param format
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String addNotify(@FormParam("templet_id") Long templetId, @FormParam("user_id") Long userId,
	    @FormParam("start_id") Long startId, @FormParam("limit") @DefaultValue("20") Integer limit,
	    @FormParam("target_id") Long targetId, @FormParam("type") Integer type, @FormParam("params") String params,
	    @FormParam("for_web") Boolean forWeb) throws JSONException {
	DataResponse<NotifyListResponse> ret = new DataResponse<NotifyListResponse>();
	try {
	    start();
	    // notifyService.addNotify(templetId, userId, targetId, params);
	    // notifyService.getUnreadCount(userId);
	    NotifyListResponse rep = notifyService.getNotifies(startId, null, userId, type, limit, forWeb);
	    ret.setCode(DataResponse.OK);
	    ret.setData(rep);
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
     * 外部系统调用添加消息提醒
     * 
     * @param addType
     * @param notifyData
     * @param format
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/task")
    @Produces(MediaType.APPLICATION_JSON)
    public String task(@FormParam("templet_id") Long templetId, @FormParam("user_id") Long userId,
	    @FormParam("start_id") Long startId, @FormParam("limit") @DefaultValue("20") Integer limit,
	    @FormParam("target_id") Long targetId, @FormParam("type") Integer type, @FormParam("params") String params,
	    @FormParam("for_web") Boolean forWeb) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    List<PushCountResponse> r = pushMessageTaskService.findRoiPushs(limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(r.size());
	    return DataResponseFormat.response(ret);
	} /*catch (L99IllegalParamsException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	}*/ catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

}
