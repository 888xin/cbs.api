package com.lifeix.cbs.api.action.market;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.bean.market.CbsMarketMainResponse;
import com.lifeix.cbs.api.bean.market.CbsMarketStatListResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.market.CbsMarketService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 渠道接口
 * 
 * @author yis
 * 
 */
@Controller
@Path("/market")
public class CbsMarketAction extends BaseAction {

    @Autowired
    private CbsMarketService cbsMarketService;

    /**
     * 渠道日统计列表
     * 
     * @return
     */
    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("nickName") String nickName, @QueryParam("password") String password) {

	DataResponse<CbsMarketMainResponse> ret = new DataResponse<CbsMarketMainResponse>();
	try {
	    CbsMarketMainResponse resp = cbsMarketService.findMarket(nickName, password);
	    ret.setCode(DataResponse.OK);
	    ret.setData(resp);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	}
    }

    /**
     * 渠道日统计列表
     * 
     * @return
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("marketCode") String marketCode, @QueryParam("token") String token,
	    @QueryParam("queryDateBefore") String queryDateBefore, @QueryParam("queryDateAfter") String queryDateAfter,
	    @QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("10") Integer limit) {

	DataResponse<CbsMarketStatListResponse> ret = new DataResponse<CbsMarketStatListResponse>();
	try {
	    CbsMarketStatListResponse resp = cbsMarketService.findMarketStat(marketCode, token, queryDateBefore,
		    queryDateAfter, startId, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(resp);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	}
    }

}
