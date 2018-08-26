package com.lifeix.cbs.api.action.inner;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.lifeix.cbs.mall.bean.goods.MallCategoryListResponse;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.api.util.IPUtil;
import com.lifeix.cbs.mall.bean.goods.MallCategoryResponse;
import com.lifeix.cbs.mall.bean.goods.MallGoodsListResponse;
import com.lifeix.cbs.mall.bean.goods.MallGoodsResponse;
import com.lifeix.cbs.mall.bean.order.MallExpressResponse;
import com.lifeix.cbs.mall.bean.order.MallOrderListResponse;
import com.lifeix.cbs.mall.bean.order.MallOrderResponse;
import com.lifeix.cbs.mall.service.goods.MallCategoryService;
import com.lifeix.cbs.mall.service.goods.MallGoodsService;
import com.lifeix.cbs.mall.service.order.MallExpressService;
import com.lifeix.cbs.mall.service.order.MallOrderService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * @author lhx
 * @function 商城管理内部后台接口
 */
@Controller
@Path("/inner/mall")
public class MallInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(MallInnerResource.class);

    @Autowired
    private MallGoodsService mallGoodsService;

    @Autowired
    private MallCategoryService mallCategoryService;

    @Autowired
    private MallOrderService mallOrderService;

    @Autowired
    private MallExpressService mallExpressService;

    /**
     * 添加商品或修改商品
     *
     * @param id
     * @param categoryId
     * @param name
     * @param price
     * @param path
     * @param sales
     * @param stock
     * @param descr
     * @param pathMore
     * @param status
     * @param type
     * @param exProp
     * @param sortIndex
     * @param originalPrice
     * @param postage
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/goods/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String edit(@FormParam("id") Long id, @FormParam("category_id") Long categoryId, @FormParam("name") String name,
                       @FormParam("price") Double price, @FormParam("path") String path, @FormParam("sales") Integer sales,
                       @FormParam("stock") Integer stock, @FormParam("descr") String descr, @FormParam("path_more") String pathMore,
                       @FormParam("status") Integer status, @FormParam("type") Integer type, @FormParam("ex_prop") String exProp,
                       @FormParam("sort_index") Integer sortIndex, @FormParam("original") Double originalPrice,
                       @FormParam("postage") Double postage) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            if (id == null) {
                MallGoodsResponse mallGoodsResponse = mallGoodsService.insert(categoryId, name, descr, path, pathMore,
                        price, originalPrice, sales, stock, postage, status, type, exProp, sortIndex);
                ret.setData(mallGoodsResponse);
            } else {
                mallGoodsService.updateMallGoods(id, categoryId, name, descr, path, pathMore, price, originalPrice, sales,
                        stock, postage, status, type, exProp, sortIndex);
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
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    @POST
    @Path("/goods/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@FormParam("id") Long id) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            mallGoodsService.delete(id);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
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
     * 查询单个商品
     *
     * @param goodsId
     * @return
     * @throws org.json.JSONException
     */
    @GET
    @Path("/goods/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String view(@QueryParam("id") Long goodsId) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            MallGoodsResponse data = mallGoodsService.getOneMallGoods(goodsId, true);
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
     * 获取商品集合
     *
     * @param categoryId
     * @param startId
     * @param status
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/goods/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCategoryData(@QueryParam("category_id") Long categoryId, @QueryParam("start_id") Long startId,
                                  @QueryParam("status") Integer status, @QueryParam("limit") @DefaultValue("20") Integer limit)
            throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            MallGoodsListResponse data = mallGoodsService.getMallGoodsInner(categoryId, status, startId, limit);
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
     * 获取所有的分类
     */
    @GET
    @Path("/category/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCategoryAll() throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            MallCategoryListResponse data = mallCategoryService.getAllCategoryListInner();
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
     * 根据名称来查询单个
     *
     * @param name
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/category/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewCategoryData(@QueryParam("name") String name) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            MallCategoryResponse data = mallCategoryService.view(name);
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
     * 增加商品类别或修改
     *
     * @param id
     * @param name
     * @param path
     * @param descr
     * @param sortIndex
     * @param num
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/category/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String editCategory(@FormParam("id") Long id, @FormParam("name") String name, @FormParam("path") String path,
                               @FormParam("descr") String descr, @FormParam("sort_index") Integer sortIndex, @FormParam("num") Integer num,
                               @FormParam("status") Integer status) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            if (id == null) {
                MallCategoryResponse mallCategoryResponse = mallCategoryService.insert(name, descr, path, num, sortIndex);
                ret.setData(mallCategoryResponse);
            } else {
                mallCategoryService.update(id, name, descr, path, num, sortIndex, status);
            }
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
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

    @POST
    @Path("/category/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteCategory(@FormParam("id") Long id) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            mallCategoryService.delete(id);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
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
     * 后台查看订单列表
     *
     * @return
     */
    @GET
    @Path("/order/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listOrder(@QueryParam("user_id") Long userId, @QueryParam("status") @DefaultValue("1") Integer status,
                            @QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit) {

        DataResponse<MallOrderListResponse> ret = new DataResponse<MallOrderListResponse>();
        try {
            MallOrderListResponse resp = mallOrderService.findMallOrderList(userId, true, true, status, startId, limit);
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

    /**
     * 后台单个订单列表
     *
     * @return
     */
    @GET
    @Path("/order/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String oneOrder(@QueryParam("order_id") Long orderId) {

        DataResponse<MallOrderResponse> ret = new DataResponse<MallOrderResponse>();
        try {
            MallOrderResponse resp = mallOrderService.findOneOrder(orderId);
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

    /**
     * 订单后台确认发货
     *
     * @param orderId
     * @return
     */
    @Path("/order/send")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public String sendOrder(@FormParam("order_id") Long orderId, @FormParam("express_type") Integer expressType,
                            @FormParam("express_no") String expressNO) {

        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            mallOrderService.sendMallOrder(orderId, expressType, expressNO);
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
     * 订单后台取消订单
     *
     * @param orderId
     * @return
     */
    @Path("/order/cancel")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public String cancelOrder(@FormParam("order_id") Long orderId) {

        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            mallOrderService.cancelOrder(orderId, IPUtil.getIpAddr(request));
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
     * 存储商品规格（存储到redis中。用hash存储，key为key，value为value）
     *
     * @param key   规格名称，比如尺寸，颜色等
     * @param value 规格值，比如红色，黄色等
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/spec/save")
    @Produces(MediaType.APPLICATION_JSON)
    public String saveSpecifications(@FormParam("key") String key, @FormParam("value") String value) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            mallGoodsService.saveSpecifications(key, value);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
            return DataResponseFormat.response(ret);
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
     * 获得商品规格keys
     */
    @GET
    @Path("/spec/keys")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpecificationsKeys() throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            MallGoodsResponse mallGoodsResponse = mallGoodsService.getSpecificationsKeys();
            ret.setCode(DataResponse.OK);
            ret.setData(mallGoodsResponse);
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
     * 获得商品规格value（根据key来返回value）
     */
    @GET
    @Path("/spec/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpecifications(@QueryParam("key") String key) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            MallGoodsResponse mallGoodsResponse = mallGoodsService.getSpecifications(key);
            ret.setCode(DataResponse.OK);
            ret.setData(mallGoodsResponse);
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
     * 存储推荐商品Id（存储到redis中。用zset存储，value为id，score为存储时间）
     */
    @POST
    @Path("/recommendId/save")
    @Produces(MediaType.APPLICATION_JSON)
    public String saveRecommendId(@FormParam("id") String id, @FormParam("top") boolean top) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            mallGoodsService.saveRecommendId(id, top);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
            return DataResponseFormat.response(ret);
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
     * 删除推荐商品Id
     *
     * @param id
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/recommendId/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteRecommendId(@FormParam("id") String id) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            mallGoodsService.deleteRecommendId(id);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
            return DataResponseFormat.response(ret);
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
     * 根据订单号查询物流信息
     *
     * @param orderId
     * @return
     */
    @GET
    @Path("/express/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getExpress(@QueryParam("order_id") Long orderId) {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            MallExpressResponse mallExpressResponse = mallExpressService.findExpressByOrder(orderId);
            ret.setData(mallExpressResponse);
            ret.setCode(DataResponse.OK);
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
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }
}
