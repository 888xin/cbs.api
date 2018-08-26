package com.lifeix.cbs.mall.bean.goods;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * 商城商品
 * 
 * @author lifeix
 * 
 */
public class MallGoodsResponse implements JsonSerializer<MallGoodsResponse>, Response {


    private static final long serialVersionUID = 8520632837058999959L;
    /**
     * 商品Id
     */
    private Long id;

    /**
     * 商品分类ID
     */
    private Long category_id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String descr;

    /**
     * 商品图片
     */
    private String path;

    /**
     * 商品全部图片
     */
    private String path_more;

    /**
     * 商品单价
     */
    private Double price;

    /**
     * 商品原价
     */
    private Double original;

    /**
     * 商品售出
     */
    private Integer sales;

    /**
     * 商品库存
     */
    private Integer stock;

    /**
     * 商品邮费
     */
    private Double postage;

    /**
     * 商品状态 0:下架 1:上架 2:删除
     */
    private Integer status;

    /**
     * 商品类型 0实物 1充值卡
     */
    private Integer type;

    /**
     * 商品属性
     */
    private String ex_prop;

    /**
     * 商品排序
     */
    private Integer sort_index;

    /**
     * 创建时间
     */
    private String create_time;
    /**
     * 更新时间
     */
    private String update_time;

    /**
     * 是否被推荐到主页
     */
    private Boolean recommend;

    /**
     * 规格keys
     */
    private Object[] spec_keys ;

    /**
     * 规格值
     */
    private String[] spec_values ;

    public Object[] getSpec_keys() {
        return spec_keys;
    }

    public void setSpec_keys(Object[] spec_keys) {
        this.spec_keys = spec_keys;
    }

    public String[] getSpec_values() {
        return spec_values;
    }

    public void setSpec_values(String[] spec_values) {
        this.spec_values = spec_values;
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
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

    public String getPath_more() {
        return path_more;
    }

    public void setPath_more(String path_more) {
        this.path_more = path_more;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getOriginal() {
        return original;
    }

    public void setOriginal(Double original) {
        this.original = original;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getPostage() {
        return postage;
    }

    public void setPostage(Double postage) {
        this.postage = postage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEx_prop() {
        return ex_prop;
    }

    public void setEx_prop(String ex_prop) {
        this.ex_prop = ex_prop;
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

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public JsonElement serialize(MallGoodsResponse mallGoodsResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
