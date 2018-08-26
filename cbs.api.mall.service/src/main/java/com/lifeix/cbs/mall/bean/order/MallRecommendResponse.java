/**
 * 
 */
package com.lifeix.cbs.mall.bean.order;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 商品导航vo
 * 
 * @author lifeix
 * 
 */
public class MallRecommendResponse implements JsonSerializer<MallRecommendResponse>, Response {

    /**
     * 
     */
    private static final long serialVersionUID = 1590143070432863213L;

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
    private String create_time;


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

    public String getCreate_time() {
        return create_time;
    }


    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public JsonElement serialize(MallRecommendResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return null;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
