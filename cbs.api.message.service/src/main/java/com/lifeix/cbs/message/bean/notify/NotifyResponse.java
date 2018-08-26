package com.lifeix.cbs.message.bean.notify;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class NotifyResponse implements JsonSerializer<NotifyResponse>, Response {
    private static final long serialVersionUID = 7063498247615281583L;
    /**
     * 主键
     */
    private Long notify_id;

    private Long ref_id;
    /**
     * 接受者
     */
    private Long user_id;
    /**
     * 触发者
     */
    private Long target_id;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 模板ID
     */
    private Long template_id;
    /**
     * 模板
     */
    private String template;
    /**
     * 参数
     */
    private String template_data;
    /**
     * 创建时间
     */
    private String create_time;
    /**
     * 是否已读
     */
    private boolean read_flag;
    /**
     * 跳转路径（包括猜友圈/押押/成就）
     */
    private String skip_path;

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(NotifyResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public String getSkip_path() {
        return skip_path;
    }

    public void setSkip_path(String skip_path) {
        this.skip_path = skip_path;
    }

    public Long getNotify_id() {
	return this.notify_id;
    }

    public void setNotify_id(Long notify_id) {
	this.notify_id = notify_id;
    }

    public Long getUser_id() {
	return this.user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public Long getTarget_id() {
	return this.target_id;
    }

    public void setTarget_id(Long target_id) {
	this.target_id = target_id;
    }

    public Long getRef_id() {
        return ref_id;
    }

    public void setRef_id(Long ref_id) {
        this.ref_id = ref_id;
    }

    public Integer getType() {
	return this.type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Long getTemplate_id() {
	return this.template_id;
    }

    public void setTemplate_id(Long template_id) {
	this.template_id = template_id;
    }

    public String getTemplate() {
	return this.template;
    }

    public void setTemplate(String template) {
	this.template = template;
    }

    public String getTemplate_data() {
	return this.template_data;
    }

    public void setTemplate_data(String template_data) {
	this.template_data = template_data;
    }

    public String getCreate_time() {
	return this.create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    public boolean getRead_flag() {
	return this.read_flag;
    }

    public void setRead_flag(boolean read_flag) {
	this.read_flag = read_flag;
    }
}
