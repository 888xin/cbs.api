package com.lifeix.cbs.api.action.mall;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.mall.bean.order.MallRecommendListResponse;
import com.lifeix.cbs.mall.service.order.MallRecommendService;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 商品导航接口
 * 
 * @author lifeix
 * 
 */
@Controller
@Path("/mall/recommend")
public class MallRecommendResource extends BaseAction {

    @Autowired
    private MallRecommendService mallRecommendService;

    /**
     * 商品导航列表
     * 
     * @return
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list() {

	DataResponse<MallRecommendListResponse> ret = new DataResponse<MallRecommendListResponse>();
	try {
	    MallRecommendListResponse resp = mallRecommendService.findMallRecommends();
	    ret.setCode(DataResponse.OK);
	    ret.setData(resp);
	    return DataResponseFormat.response(ret);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	}
    }
}