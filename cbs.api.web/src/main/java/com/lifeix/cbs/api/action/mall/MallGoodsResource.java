package com.lifeix.cbs.api.action.mall;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
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
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.mall.bean.goods.MallGoodsListResponse;
import com.lifeix.cbs.mall.bean.goods.MallGoodsResponse;
import com.lifeix.cbs.mall.bean.goods.MallIndexResponse;
import com.lifeix.cbs.mall.service.goods.MallGoodsService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 16-3-22 上午10:19
 * 
 * @Description 新版商城商品
 */
@Controller
@Path("/mall/goods")
public class MallGoodsResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(MallGoodsResource.class);

    @Autowired
    private MallGoodsService mallGoodsService;

    /**
     * 查询单个商品，只能查看上架的商品
     * 
     * @param goodsId
     * @return
     * @throws org.json.JSONException
     */
    @GET
    @Path("/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String view(@QueryParam("id") Long goodsId) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    MallGoodsResponse data = mallGoodsService.getOneMallGoods(goodsId, false);
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 商城主页
     * 
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/index")
    @Produces(MediaType.APPLICATION_JSON)
    public String index(@QueryParam("size") @DefaultValue("10") Integer recommendCategorySize) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    MallIndexResponse data = mallGoodsService.getMallIndex(recommendCategorySize);
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 获取分类下的商品
     * 
     * @param categoryId
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/category")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCategoryData(@QueryParam("category_id") Long categoryId, @QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    MallGoodsListResponse data = mallGoodsService.getCategoryGoodsList(categoryId, startId, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }
}
