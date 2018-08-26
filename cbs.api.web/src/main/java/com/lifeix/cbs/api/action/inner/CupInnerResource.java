package com.lifeix.cbs.api.action.inner;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
import com.lifeix.cbs.contest.service.spark.cup.BbCupDubbo;
import com.lifeix.cbs.contest.service.spark.cup.FbCupDubbo;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 *  inner interface
 * 
 * @author lifeix
 * 
 */
@Controller
@Path("/inner/cup")
public class CupInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(CupInnerResource.class);

    @Autowired
    private BbCupDubbo bbCupDubbo;

    @Autowired
    private FbCupDubbo fbCupDubbo;

    /**
     * 修改联赛名字
     * 
     * @param type
     * @param id
     * @param name
     * @return
     */
    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@FormParam("type") Integer type, @FormParam("id") Long id, @FormParam("name") String name) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    ParamemeterAssert.assertDataNotNull(type);

	    if (type == ContestType.FOOTBALL) {
		fbCupDubbo.updateCupName(id, name);
	    } else {
		bbCupDubbo.updateCupName(id, name);
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
