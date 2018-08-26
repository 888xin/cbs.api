package com.lifeix.cbs.contest.bean.circle;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 猜友圈评论
 * 
 * @author jacky
 *
 */
public class FriendCircleCommResponse implements JsonSerializer<FriendCircleCommResponse>, Response {

    private static final long serialVersionUID = -7444270937835461679L;

    private Long id;

    /**
     * 新闻Id
     */
    private Long content_id;

    private Long circle_userid;

    /**
     * 评论用户id
     */
    private Long comm_userid;

    /**
     * 评论者头像
     */
    private String user_avatar;

    /**
     * 评论者姓名
     */
    private String user_name;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论图片
     */
    private String image;

    /**
     * 被回复姓名
     */
    private String re_user;

    /**
     * 被回复用户的id
     */
    private Long re_userid;

    /**
     * 被回复者头像
     */
    private String re_user_avatar;

    /**
     * 被回复内容
     */
    private String re_content;

    /**
     * 被回复图片
     */
    private String re_image;

    /**
     * 评论时间
     */
    private String create_time;

    /**
     * ip地址
     */
    private String ipaddress;

    private String source;

    /**
     * 主题
     */
    private String theme;

    /**
     * 主题图片
     */
    private String theme_img;

    /**
     * 评论状态 0表示未读，1表示已读 , 2表示屏蔽
     */
    private Integer status;

    /**
     * 消息类型
     */
    private Integer type;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getContent_id() {
	return content_id;
    }

    public void setContent_id(Long content_id) {
	this.content_id = content_id;
    }

    public String getUser_avatar() {
	return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
	this.user_avatar = user_avatar;
    }

    public String getUser_name() {
	return user_name;
    }

    public void setUser_name(String user_name) {
	this.user_name = user_name;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public String getImage() {
	return image;
    }

    public void setImage(String image) {
	this.image = image;
    }

    public String getRe_user() {
	return re_user;
    }

    public void setRe_user(String re_user) {
	this.re_user = re_user;
    }

    public String getRe_content() {
	return re_content;
    }

    public void setRe_content(String re_content) {
	this.re_content = re_content;
    }

    public String getRe_image() {
	return re_image;
    }

    public void setRe_image(String re_image) {
	this.re_image = re_image;
    }

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    public String getIpaddress() {
	return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
	this.ipaddress = ipaddress;
    }

    public String getSource() {
	return source;
    }

    public void setSource(String source) {
	this.source = source;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getRe_user_avatar() {
	return re_user_avatar;
    }

    public void setRe_user_avatar(String re_user_avatar) {
	this.re_user_avatar = re_user_avatar;
    }

    public String getTheme() {
	return theme;
    }

    public void setTheme(String theme) {
	this.theme = theme;
    }

    public Long getComm_userid() {
	return comm_userid;
    }

    public void setComm_userid(Long comm_userid) {
	this.comm_userid = comm_userid;
    }

    public String getTheme_img() {
	return theme_img;
    }

    public void setTheme_img(String theme_img) {
	this.theme_img = theme_img;
    }

    public Long getCircle_userid() {
	return circle_userid;
    }

    public void setCircle_userid(Long circle_userid) {
	this.circle_userid = circle_userid;
    }

    public Long getRe_userid() {
	return re_userid;
    }

    public void setRe_userid(Long re_userid) {
	this.re_userid = re_userid;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    @Override
    public JsonElement serialize(FriendCircleCommResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
