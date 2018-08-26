package com.lifeix.cbs.message.bean.placard;

import java.lang.reflect.Type;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * Created by lhx on 15-10-19 下午3:44
 * 
 * @Description
 */
public class PlacardTempletResponse implements JsonSerializer<PlacardTempletResponse>, Response {

    private static final long serialVersionUID = 4770783911268903960L;

    private Long templet_id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 结束时间
     */
    private String end_time;

    /**
     * 关闭标识
     */
    private Boolean disable_flag;

    /**
     * 公告统计
     */
    private Integer placard_count;

    /**
     * 链接类型
     * 
     * 1.网页 2.内容 3.足球赛事 4.篮球赛事 5.锦标赛 6.送龙筹券
     */
    private Integer link_type;

    /**
     * 链接数据
     */
    private String link_data;

    /**
     * 是否已读 true 已读 false 未读
     */
    private boolean read_flag;
    private Set<String> text_images;

    public Set<String> getText_images() {
	return text_images;
    }

    public void setText_images(Set<String> text_images) {
	this.text_images = text_images;
    }

    public Long getTemplet_id() {
	return templet_id;
    }

    public void setTemplet_id(Long templet_id) {
	this.templet_id = templet_id;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    public String getEnd_time() {
	return end_time;
    }

    public void setEnd_time(String end_time) {
	this.end_time = end_time;
    }

    public Boolean getDisable_flag() {
	return disable_flag;
    }

    public void setDisable_flag(Boolean disable_flag) {
	this.disable_flag = disable_flag;
    }

    public Integer getPlacard_count() {
	return placard_count;
    }

    public void setPlacard_count(Integer placard_count) {
	this.placard_count = placard_count;
    }

    public Integer getLink_type() {
	return link_type;
    }

    public void setLink_type(Integer link_type) {
	this.link_type = link_type;
    }

    public String getLink_data() {
	return link_data;
    }

    public void setLink_data(String link_data) {
	this.link_data = link_data;
    }

    public boolean isRead_flag() {
	return read_flag;
    }

    public void setRead_flag(boolean read_flag) {
	this.read_flag = read_flag;
    }

    @Override
    public JsonElement serialize(PlacardTempletResponse placardTempletResponse, Type type,
	    JsonSerializationContext jsonSerializationContext) {
	return null;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
