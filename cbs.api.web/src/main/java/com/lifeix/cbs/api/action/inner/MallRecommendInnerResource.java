package com.lifeix.cbs.api.action.inner;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.mall.bean.order.MallRecommendResponse;
import com.lifeix.cbs.mall.service.order.MallRecommendService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 商品导航接口
 * 
 * @author lifeix
 * 
 */
@Controller
@Path("/inner/recommend")
public class MallRecommendInnerResource extends BaseAction {

    @Autowired
    private MallRecommendService mallRecommendService;

    /**
     * 查询单个商品导航
     *
     * @param id
     * @return
     * @throws org.json.JSONException
     */
    @GET
    @Path("/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String view(@QueryParam("id") Long id) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            MallRecommendResponse data = mallRecommendService.findMallRecommend(id);
            ret.setCode(DataResponse.OK);
            ret.setData(data);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        }  catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }
    
    
    /**
     * 保存商品导航
     * 
     * @param id
     * @param image
     * @param title
     * @param type
     * @param link
     * @param sort
     * @return
     */
    @Path("/add")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public String add(@FormParam("image") String image, @FormParam("title") String title,
	    @FormParam("type") Integer type, @FormParam("link") String link, @FormParam("sort") Integer sort) {

	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    mallRecommendService.insert(image, title, type, link, sort);
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
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	}

    }

    /**
     * 修改商品导航
     * 
     * @param id
     * @param image
     * @param title
     * @param type
     * @param link
     * @param sort
     * @return
     */
    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@FormParam("id") Long id, @FormParam("image") String image, @FormParam("title") String title,
	    @FormParam("type") Integer type, @FormParam("link") String link, @FormParam("sort") Integer sort) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    mallRecommendService.updateRecommend(id, image, title, type, link, sort);
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

    /**
     * 删除商品导航
     * 
     * @param id
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@FormParam("id") Long id) throws JSONException {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    mallRecommendService.deleteMallRecommendByid(id);
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