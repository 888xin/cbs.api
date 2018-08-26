package com.lifeix.cbs.mall.dto.goods;

import java.io.Serializable;
import java.util.Date;

/**
 * 商城商品
 *
 * @author lifeix
 *
 */
public class MallGoods implements Serializable {

    private static final long serialVersionUID = 3099240755985333004L;

    /**
     * 商品Id
     */
    private Long id;

    /**
     * 商品分类ID
     */
    private Long categoryId;

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
    private String pathMore;

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
    private String exProp;

    /**
     * 商品排序
     */
    private Integer sortIndex;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public String getPathMore() {
        return pathMore;
    }

    public void setPathMore(String pathMore) {
        this.pathMore = pathMore;
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

    public String getExProp() {
        return exProp;
    }

    public void setExProp(String exProp) {
        this.exProp = exProp;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
