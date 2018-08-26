package com.lifeix.cbs.api.action.gold;

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
import com.lifeix.cbs.api.bean.gold.GoldLogListResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.gold.GoldLogService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/gold")
public class GoldLogResource extends BaseAction {

    @Autowired
    private GoldLogService goldLogService;

    @GET
    @Path("/log/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String logs(@QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {
	start();
	DataResponse<GoldLogListResponse> ret = new DataResponse<GoldLogListResponse>();
	GoldLogListResponse response;
	try {
	    response = goldLogService.findGoldLogList(getSessionAccount(request), startId, limit);
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

    public GoldLogService getGoldLogService() {
	return goldLogService;
    }

    public void setGoldLogService(GoldLogService goldLogService) {
	this.goldLogService = goldLogService;
    }

}
