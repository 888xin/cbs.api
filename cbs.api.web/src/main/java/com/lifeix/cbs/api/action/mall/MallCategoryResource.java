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
import com.lifeix.cbs.mall.bean.goods.MallCategoryListResponse;
import com.lifeix.cbs.mall.service.goods.MallCategoryService;

/**
 * Created by lhx on 16-3-22 上午10:19
 *
 * @Description 新版商城商品分类
 */
@Controller
@Path("/mall/category")
public class MallCategoryResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(MallCategoryResource.class);

    @Autowired
    private MallCategoryService mallCategoryService;

    /**
     * 获取所有的分类
     *
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll() throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            MallCategoryListResponse data = mallCategoryService.getAllCategoryList();
            ret.setCode(DataResponse.OK);
            ret.setData(data);
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
     * 获取推荐的分类
     *
     * @param num 推荐类别的个数
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/recommend")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRecommend(@QueryParam("num") @DefaultValue("2") Integer num) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            MallCategoryListResponse data = mallCategoryService.getRecommendCategoryList(num);
            ret.setCode(DataResponse.OK);
            ret.setData(data);
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
