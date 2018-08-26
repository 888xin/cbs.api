package com.lifeix.cbs.api.action.inner;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.fb.TeamListResponse;
import com.lifeix.cbs.contest.service.bb.BbContestService;
import com.lifeix.cbs.contest.service.fb.FbContestService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * team inner interface
 * 
 * @author lifeix
 * 
 */
@Controller
@Path("/inner/team")
public class TeamInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(TeamInnerResource.class);

    @Autowired
    private FbContestService fbContestService;

    @Autowired
    private BbContestService bbContestService;

    /**
     * 查询球队
     * 
     * @param type
     * @param key
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public String search(@QueryParam("type") @DefaultValue("0") Integer type, @QueryParam("key") String key)
	    throws JSONException {
	DataResponse<TeamListResponse> ret = new DataResponse<TeamListResponse>();
	try {
	    start();
	    TeamListResponse teams = null;
	    if (type == ContestType.FOOTBALL) {
		teams = fbContestService.findFbTeams(key);
	    } else {
		teams = bbContestService.findBbTeams(key);
	    }
	    ret.setCode(DataResponse.OK);
	    ret.setData(teams);
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

    /**
     * 修改球队
     * 
     * @param type
     * @param id
     * @param name
     * @return
     */
    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@FormParam("type") Integer type, @FormParam("id") Long id, @FormParam("name") String name,
	    @FormParam("logo") String logo) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    ParamemeterAssert.assertDataNotNull(type);

	    if (type == ContestType.FOOTBALL) {
		fbContestService.updateTeam(id, name, logo);
	    } else {
		bbContestService.updateTeam(id, name, logo);
	    }
	    ret.setCode(DataResponse.OK);
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
}
