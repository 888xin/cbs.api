package com.lifeix.cbs.mall.bean.goods;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * 商城商品分类
 * 
 * @author lifeix
 * 
 */
public class MallCategoryResponse implements JsonSerializer<MallCategoryResponse>, Response {

    private static final long serialVersionUID = 1286203382778102249L;

    /**
     * 商品分类Id
     */
    private Long id;

    /**
     * 商品分类名称
     */
    private String name;

    /**
     * 商品分类描述
     */
    private String descr;

    /**
     * 商品分类图片
     */
    private String path;

    /**
     * 商品数量
     */
    private Integer num;

    /**
     * 商品分类排序
     */
    private Integer sort_index;

    /**
     * 商品分类状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 分类下的推荐商品
     */
    private List<MallGoodsResponse> goods ;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<MallGoodsResponse> getGoods() {
        return goods;
    }

    public void setGoods(List<MallGoodsResponse> goods) {
        this.goods = goods;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getSort_index() {
        return sort_index;
    }

    public void setSort_index(Integer sort_index) {
        this.sort_index = sort_index;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public JsonElement serialize(MallCategoryResponse mallCategoryResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
