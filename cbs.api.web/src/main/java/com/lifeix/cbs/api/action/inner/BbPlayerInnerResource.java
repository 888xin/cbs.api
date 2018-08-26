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
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.bb.ext.BbPlayerListResponse;
import com.lifeix.cbs.contest.service.bb.BbContestService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * player inner interface
 * 
 * @author lifeix
 * 
 */
@Controller
@Path("/inner/player/bb")
public class BbPlayerInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(BbPlayerInnerResource.class);

    @Autowired
    private BbContestService bbContestService;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit)
	    throws JSONException {
	DataResponse<BbPlayerListResponse> ret = new DataResponse<BbPlayerListResponse>();
	try {
	    start();
	    BbPlayerListResponse players = bbContestService.findBbPlayers(startId, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(players);
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
     * 搜索球员
     * 
     * @param name
     * @param teamId
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public String search(@QueryParam("name") String name, @QueryParam("team_id") Long teamId) throws JSONException {
	DataResponse<BbPlayerListResponse> ret = new DataResponse<BbPlayerListResponse>();
	try {
	    start();
	    BbPlayerListResponse players = bbContestService.searchPlayer(name, teamId);
	    ret.setCode(DataResponse.OK);
	    ret.setData(players);
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
     * 修改球员
     * 
     * @param type
     * @param id
     * @param name
     * @return
     */
    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@FormParam("id") Long id, @FormParam("name") String name,
	    @FormParam("first_name") String firstName, @FormParam("last_name") String lastName) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    bbContestService.updatePlayer(id, name, firstName, lastName);
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
