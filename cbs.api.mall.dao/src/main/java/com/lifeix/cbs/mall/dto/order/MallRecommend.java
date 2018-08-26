/**
 * 
 */
package com.lifeix.cbs.mall.dto.order;

import java.io.Serializable;
import java.util.Date;

/**
 * 商城商品导航
 * 
 * @author lifeix
 * 
 */
public class MallRecommend implements Serializable {

    private static final long serialVersionUID = -4413023920034203824L;

    /**
     * 订单Id
     */
    private Long id;
    
    /**
     * 图片
     */
    private String image;
    
    
    /**
     * 副标题
     */
    private String title;

    /**
     * 类型（0 商品 | 2 分类）
     */
    private Integer type;
    
    
    /**
     *logo地址
     */
    private String link;

    /**
     * 排序
     */
    private Integer sort;
    

    /**
     * 创建时间
     */
    private Date createTime;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getImage() {
        return image;
    }


    public void setImage(String image) {
        this.image = image;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public Integer getType() {
        return type;
    }


    public void setType(Integer type) {
        this.type = type;
    }


    public String getLink() {
        return link;
    }


    public void setLink(String link) {
        this.link = link;
    }


    public Integer getSort() {
        return sort;
    }


    public void setSort(Integer sort) {
        this.sort = sort;
    }


    public Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
