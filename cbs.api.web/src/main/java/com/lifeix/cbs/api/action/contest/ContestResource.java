package com.lifeix.cbs.api.action.contest;

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
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.contest.ContestAdListResponse;
import com.lifeix.cbs.contest.service.contest.ContestAdService;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/all/contest")
public class ContestResource extends BaseAction {

    @Autowired
    private ContestAdService contestAdService;

    /**
     * 赛事广告列表
     * 
     * @param contestType
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/ad/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listContest(@QueryParam("contest_type") @DefaultValue("10") Integer contestType,
	    @QueryParam("limit") @DefaultValue("2") Integer limit) throws JSONException {
	DataResponse<ContestAdListResponse> ret = new DataResponse<ContestAdListResponse>();
	try {
	    start();
	    ContestAdListResponse response = contestAdService.findContestsAd(contestType, limit);
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
}
