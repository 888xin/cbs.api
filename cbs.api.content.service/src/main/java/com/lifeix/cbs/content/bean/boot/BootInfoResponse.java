package com.lifeix.cbs.content.bean.boot;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 开机信息
 * 
 * @author lifeix-sz
 * 
 */
public class BootInfoResponse implements JsonSerializer<BootInfoResponse>, Response {

    /**
     * 
     */
    private static final long serialVersionUID = 1947309862748469103L;

    /**
     * 主键
     */
    private Long id;

    /**
     * key值
     */
    private String info_key;

    /**
     * 启用标识
     */
    private Boolean enable_flag;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 标识是否打开搜索
     */
    private boolean search_flag;

    /**
     * 跳转类型
     */
    private Integer type;

    /**
     * 跳转链接
     */
    private String data_link;

    /**
     * 连续登陆次数
     */
    private Integer login_times;

    /**
     * 是否获奖
     */
    private Boolean get_award;

    /**
     * 连续登陆奖励列表
     */
    private Long[] prizes;

    /**
     * 跳转时间
     */
    private Integer adv_time;

    /**
     * 跳转数据：type和data_link的合成体
     */
    private String scheme_link;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getInfo_key() {
	return info_key;
    }

    public void setInfo_key(String info_key) {
	this.info_key = info_key;
    }

    public Boolean getEnable_flag() {
	return enable_flag;
    }

    public void setEnable_flag(Boolean enable_flag) {
	this.enable_flag = enable_flag;
    }

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    @Override
    public JsonElement serialize(BootInfoResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    @Override
    public String getObjectName() {
	return null;
    }

    public boolean isSearch_flag() {
	return search_flag;
    }

    public void setSearch_flag(boolean search_flag) {
	this.search_flag = search_flag;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public String getData_link() {
	return data_link;
    }

    public void setData_link(String data_link) {
	this.data_link = data_link;
    }

    public Integer getLogin_times() {
	return login_times;
    }

    public void setLogin_times(Integer login_times) {
	this.login_times = login_times;
    }

    public Boolean getGet_award() {
	return get_award;
    }

    public void setGet_award(Boolean get_award) {
	this.get_award = get_award;
    }

    public Long[] getPrizes() {
	return prizes;
    }

    public void setPrizes(Long[] prizes) {
	this.prizes = prizes;
    }

    public Integer getAdv_time() {
	return adv_time;
    }

    public void setAdv_time(Integer adv_time) {
	this.adv_time = adv_time;
    }

    public String getScheme_link() {
	return scheme_link;
    }

    public void setScheme_link(String scheme_link) {
	this.scheme_link = scheme_link;
    }
}
