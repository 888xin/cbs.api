package com.lifeix.cbs.api.action.contest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
import com.lifeix.cbs.contest.bean.odds.OddsResponse;
import com.lifeix.cbs.contest.service.odds.FbOddsService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/fb/odds")
public class FbOddsResource extends BaseAction {

    @Autowired
    private FbOddsService fbOddsService;

    /**
     * 足球赛事赔率
     * 
     * @param contestId
     * @param hasContest
     * @param oddsType
     * @param format
     * @param client
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/contest")
    @Produces(MediaType.APPLICATION_JSON)
    public String time(@QueryParam("contest_id") Long contestId,
	    @QueryParam("has_contest") @DefaultValue("true") boolean hasContest,
	    @QueryParam("odds_type") @DefaultValue("31") int oddsType,
	    @QueryParam("format") @DefaultValue("json") String format, @HeaderParam("client") String client)
	    throws JSONException {
	DataResponse<OddsResponse> ret = new DataResponse<OddsResponse>();
	try {
	    start();
	    OddsResponse response = fbOddsService.findFbOddsResponse(contestId, getSessionAccount(request), hasContest,
		    oddsType, client);
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
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

    public void setFbOddsService(FbOddsService fbOddsService) {
	this.fbOddsService = fbOddsService;
    }

}
