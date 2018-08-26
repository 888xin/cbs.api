package com.lifeix.cbs.contest.bean.comment;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class CbsCommentResponse implements JsonSerializer<CbsCommentResponse>, Response {
    /**
     * 
     */
    private static final long serialVersionUID = 7857114476050369792L;
    /**
     * 评论id
     */
    private Long id;
    /**
     * 赛事id
     */
    private Long contest_id;
    /**
     * 发布者id
     */
    private Long user_id;
    /**
     * 头像
     */
    private String user_avatar;
    /**
     * 评论发布者昵称
     */
    private String user_name;
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 多个图片
     */
    private String[] images;
    
    /**
     * 评论时间
     */
    private String create_time;    
    /**
     * ip地址
     */
    private String ipaddress;
    /**
     * 来源系统平台
     */
    private String client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContest_id() {
        return contest_id;
    }

    public void setContest_id(Long contest_id) {
        this.contest_id = contest_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
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

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(CbsCommentResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
