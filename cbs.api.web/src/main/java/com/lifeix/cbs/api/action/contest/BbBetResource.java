package com.lifeix.cbs.api.action.contest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
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
import com.lifeix.cbs.api.util.IPUtil;
import com.lifeix.cbs.contest.service.bet.BbBetService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

@Controller
@Path("/bb/bet")
public class BbBetResource extends BaseAction {

    @Autowired
    private BbBetService bbBetService;

    /**
     * 篮球下单接口
     * 
     * @param format
     * @param contestId
     * @param playId
     * @param r1
     * @param r2
     * @param r3
     * @param bet
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String add(@FormParam("format") @DefaultValue("json") String format, @FormParam("contest_id") Long contestId,
	    @FormParam("longbi") @DefaultValue("false") Boolean longbi, @FormParam("p_id") Integer playId,
	    @FormParam("support") Integer support, @FormParam("r1") Double r1, @FormParam("r2") Double r2,
	    @FormParam("r3") Double r3, @FormParam("bet") Double bet, @FormParam("content") String content,
	    @FormParam("client") String client, @FormParam("pk") @DefaultValue("false") Boolean pk,
	    @FormParam("target_id") Long targetId, @FormParam("coupon_id") Long couponId) throws JSONException {
	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	try {
	    start();
	    CustomResponse response = bbBetService.addBBBet(getSessionAccount(request), contestId, longbi, playId, support,
		    r1, r2, r3, bet, content, client, IPUtil.getIpAddr(request), true, couponId);

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

    public void setBbBetService(BbBetService bbBetService) {
	this.bbBetService = bbBetService;
    }

}
