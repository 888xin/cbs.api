/**
 * 
 */
package com.lifeix.cbs.content.bean.inform;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;
import com.lifeix.user.beans.account.AccountResponse;

/**
 * 举报信息
 * 
 * @author lifeix
 *
 */
public class InformResponse implements JsonSerializer<InformResponse>, Response {

    private static final long serialVersionUID = -6520962918401747112L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 举报类型
     */
    private Integer type;

    /**
     * 内容id
     */
    private Long contain_id;

    /**
     * 被举报者id
     */
    private Long user_id;

    /**
     * 被举报者名称
     */
    private String user_name;

    /**
     * 举报人id
     */
    private Long informer_id;

    /**
     * 举报者名称
     */
    private String informer_name;

    /**
     * 举报类型
     */
    private Integer inform_type;

    /**
     * 举报理由
     */
    private String inform_reason;

    /**
     * 状态 0：待处理，1：屏蔽，2：忽略
     */
    private Integer status;

    /**
     * 处理信息
     */
    private String dispose_info;

    /**
     * 被举报次数
     */
    private Integer total;

    /**
     * 被举报内容
     */
    private String contain;

    /**
     * 被举报图片
     */
    private String image;

    /**
     * 举报时间
     */
    private String update_time;

    /**
     * 解禁时间
     */
    private String remove_time;

    private AccountResponse userResponse;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Long getContain_id() {
	return contain_id;
    }

    public void setContain_id(Long contain_id) {
	this.contain_id = contain_id;
    }

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public Long getInformer_id() {
	return informer_id;
    }

    public void setInformer_id(Long informer_id) {
	this.informer_id = informer_id;
    }

    public Integer getInform_type() {
	return inform_type;
    }

    public void setInform_type(Integer inform_type) {
	this.inform_type = inform_type;
    }

    public String getInform_reason() {
	return inform_reason;
    }

    public void setInform_reason(String inform_reason) {
	this.inform_reason = inform_reason;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getDispose_info() {
	return dispose_info;
    }

    public void setDispose_info(String dispose_info) {
	this.dispose_info = dispose_info;
    }

    public Integer getTotal() {
	return total;
    }

    public void setTotal(Integer total) {
	this.total = total;
    }

    public String getUpdate_time() {
	return update_time;
    }

    public void setUpdate_time(String update_time) {
	this.update_time = update_time;
    }

    public String getContain() {
	return contain;
    }

    public void setContain(String contain) {
	this.contain = contain;
    }

    public String getImage() {
	return image;
    }

    public void setImage(String image) {
	this.image = image;
    }

    public String getUser_name() {
	return user_name;
    }

    public void setUser_name(String user_name) {
	this.user_name = user_name;
    }

    public String getInformer_name() {
	return informer_name;
    }

    public void setInformer_name(String informer_name) {
	this.informer_name = informer_name;
    }

    public String getRemove_time() {
	return remove_time;
    }

    public void setRemove_time(String remove_time) {
	this.remove_time = remove_time;
    }

    public AccountResponse getUserResponse() {
	return userResponse;
    }

    public void setUserResponse(AccountResponse userResponse) {
	this.userResponse = userResponse;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(InformResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
