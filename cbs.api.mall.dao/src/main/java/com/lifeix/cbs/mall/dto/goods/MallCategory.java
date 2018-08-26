package com.lifeix.cbs.mall.dto.goods;

import java.io.Serializable;
import java.util.Date;

/**
 * 商城商品分类
 * 
 * @author lifeix
 * 
 */
public class MallCategory implements Serializable {

    private static final long serialVersionUID = 3414701760286876421L;

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
    private Integer sortIndex;

    /**
     * 商品分类状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
