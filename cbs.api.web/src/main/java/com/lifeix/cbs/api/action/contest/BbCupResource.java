package com.lifeix.cbs.api.action.contest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.fb.CupListResponse;
import com.lifeix.cbs.contest.bean.fb.CupResponse;
import com.lifeix.cbs.contest.service.spark.cup.BbCupDubbo;

@Controller
@Path("/bb/cup")
public class BbCupResource extends BaseAction {

    @Autowired
    private BbCupDubbo bbCupDubbo;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String bbPlayerList() throws JSONException {
	DataResponse<CupListResponse> ret = new DataResponse<CupListResponse>();
	try {
	    start();
	    CupListResponse cupListResponse = new CupListResponse();

	    Map<Long, CupResponse> cups = bbCupDubbo.getAllCup();
	    if (!cups.isEmpty()) {
		List<CupResponse> cupList = new ArrayList<CupResponse>(cups.values());
		cupListResponse.setCups(cupList);
		ret.setCode(DataResponse.OK);
		ret.setData(cupListResponse);
	    }
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
     * 获取指定时间范围内赛事类型
     */
    @GET
    @Path("/valid")
    @Produces(MediaType.APPLICATION_JSON)
    public String valid() throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    CupListResponse data = bbCupDubbo.getCupByContestDate();
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    public BbCupDubbo getBbCupDubbo() {
	return bbCupDubbo;
    }

    public void setBbCupDubbo(BbCupDubbo bbCupDubbo) {
	this.bbCupDubbo = bbCupDubbo;
    }

}
