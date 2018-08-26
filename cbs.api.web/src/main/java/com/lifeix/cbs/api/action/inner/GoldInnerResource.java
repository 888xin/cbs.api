package com.lifeix.cbs.api.action.inner;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.bean.gold.GoldLogListResponse;
import com.lifeix.cbs.api.bean.gold.GoldStatisticListResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.gold.GoldLogService;
import com.lifeix.cbs.api.service.gold.GoldStatisticService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/inner/gold")
public class GoldInnerResource extends BaseAction {

    @Autowired
    private GoldLogService goldLogService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private GoldStatisticService goldStatisticService;

    /**
     * 后台充值记录
     * 
     * @param userId
     * @param roleId
     * @param startId
     * @param limit
     * @param beginTime
     * @param endTime
     * @return
     */
    @POST
    @Path("/log/system/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String logSystemList(@FormParam("user_id") Long userId, @FormParam("role_id") Long roleId,
	    @FormParam("start_id") Long startId, @FormParam("limit") @DefaultValue("20") Integer limit,
	    @FormParam("begin_time") String beginTime, @FormParam("end_time") String endTime) {
	start();
	DataResponse<GoldLogListResponse> ret = new DataResponse<GoldLogListResponse>();
	try {

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date begin = null;
	    Date end = null;
	    if (beginTime != null) {
		begin = sdf.parse(beginTime);
	    }
	    if (endTime != null) {
		end = sdf.parse(endTime);
	    }
	    GoldLogListResponse resp = goldLogService.findSystemGoldLogList(userId, roleId, startId, limit, begin, end);
	    ret.setData(resp);
	    ret.setCode(DataResponse.OK);
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
     * 获取一段时间内的的系统收入支出统计
     * 
     * @param startTime
     * @param endTime
     * @return
     */
    @POST
    @Path("/log/statistic")
    @Produces(MediaType.APPLICATION_JSON)
    public String statistic(@FormParam("start_time") String startTime, @FormParam("end_time") String endTime) {
	start();
	DataResponse<GoldStatisticListResponse> ret = new DataResponse<GoldStatisticListResponse>();
	try {

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Date start = null;
	    Date end = null;
	    if (startTime != null) {
		start = sdf.parse(startTime);
	    }
	    if (endTime != null) {
		end = sdf.parse(endTime);
	    }
	    GoldStatisticListResponse resp = goldStatisticService.findByBetweenTime(start, end);
	    ret.setData(resp);
	    ret.setCode(DataResponse.OK);
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
     * 查询系统日志详情
     * 
     * @param time
     * @param startId
     * @param limit
     * @return
     */
    @POST
    @Path("/log/system/detail")
    @Produces(MediaType.APPLICATION_JSON)
    public String systemLogsDetail(@FormParam("log_id") Long logId, @FormParam("long_no") Long LongNo,
	    @FormParam("start_time") String startTime, @FormParam("end_time") String endTime,
	    @FormParam("start_id") Long startId, @FormParam("limit") @DefaultValue("20") Integer limit,
	    @FormParam("type") String types) {
	start();
	DataResponse<GoldLogListResponse> ret = new DataResponse<GoldLogListResponse>();
	try {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date startDate = null;
	    Date endDate = null;
	    Integer[] typeArray = null;
	    if (!StringUtils.isEmpty(startTime)) {
		startDate = sdf.parse(startTime);
	    }
	    if (!StringUtils.isEmpty(endTime)) {
		endDate = sdf.parse(endTime);
	    }
	    if (types != null && !types.trim().equals("")) {
		String[] typeStrArray = types.split(",");
		typeArray = new Integer[typeStrArray.length];
		for (int i = 0; i < typeStrArray.length; i++) {
		    typeArray[i] = Integer.parseInt(typeStrArray[i]);
		}
	    }
	    GoldLogListResponse resp = goldLogService.findSystemLogsDetail(logId, LongNo, startDate, endDate, startId,
		    limit, typeArray);
	    ret.setData(resp);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
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
