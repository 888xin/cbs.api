package com.lifeix.cbs.contest.bean.yy;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 押押：赛事选项信息
 * 
 * @author peter
 * 
 */
public class YyOptionResponse implements JsonSerializer<YyOptionResponse>, Response {

    private static final long serialVersionUID = -6116753460374791005L;

    /**
     * 索引
     */
    private Integer index;

    /**
     * 标题
     */
    private String name;

    /**
     * 赔率
     */
    private Double roi;

    /**
     * 下单统计
     */
    private int count = 0;

    /**
     * 展示图片
     */
    private String image ;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getIndex() {
	return index;
    }

    public void setIndex(Integer index) {
	this.index = index;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Double getRoi() {
	return roi;
    }

    public void setRoi(Double roi) {
	this.roi = roi;
    }

    public int getCount() {
	return count;
    }

    public void setCount(int count) {
	this.count = count;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(YyOptionResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
