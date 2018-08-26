package com.lifeix.cbs.api.action.relationship;

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
import com.lifeix.cbs.api.bean.relationship.RelationShipListResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.relationship.RelationshipService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/relationship")
public class RelationshipResource extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(RelationshipResource.class);

    @Autowired
    private RelationshipService relationshipService;

    /**
     * 获取用户的关注的人，粉丝，相互关注
     * 
     * @param targetId
     * @param startId
     * @param endId
     * @param type
     * @param limit
     * @return
     * @throws JSONException
     */
    @Path("/view")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRelationshipByType(@QueryParam("target_id") Long targetId, @QueryParam("start_id") Long startId,
	    @QueryParam("end_id") Long endId, @QueryParam("type") Integer type,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) {
	DataResponse<RelationShipListResponse> ret = new DataResponse<RelationShipListResponse>();
	try {
	    start();
	    RelationShipListResponse resp = relationshipService.getRelationshipByType(type, getSessionAccount(request),
		    targetId, startId, endId, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(resp);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 添加关注
     * 
     * @param targetId
     * @param accountId
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String addRelationship(@FormParam("target_id") Long targetId) {
	DataResponse<CbsUserResponse> ret = new DataResponse<CbsUserResponse>();
	try {
	    start();
        CbsUserResponse rep = relationshipService.addFollow(getSessionAccount(request), targetId);
	    ret.setCode(DataResponse.OK);
	    ret.setData(rep);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    /**
     * 取消关注
     * 
     * @param targetId
     * @param accountId
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteRelationship(@FormParam("target_id") Long targetId) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    relationshipService.deleteFollow(getSessionAccount(request), targetId);
	    ret.setCode(DataResponse.OK);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

}
