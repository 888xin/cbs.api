package com.lifeix.cbs.content.bean.contest;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by lhx on 16-4-14 下午5:41
 *
 * @Description
 */
public class ContestNewsResponse implements JsonSerializer<ContestNewsResponse>, Response {

    private static final long serialVersionUID = -787262332296111048L;
    private Long id ;
    /**
     * 标题
     */
    private String title ;
    /**
     * 文本摘要
     */
    private String desc ;

    /**
     * 照片路径
     */
    private String image ;

    private String create_time;

    private Integer status ;

    /**
     * 资讯id
     */
    private Long content_id ;

    /**
     * 赛事类型: 1---足球, 2---篮球
     */
    private Integer contest_type;

    private Long contest_id ;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getContent_id() {
        return content_id;
    }

    public void setContent_id(Long content_id) {
        this.content_id = content_id;
    }

    public Integer getContest_type() {
        return contest_type;
    }

    public void setContest_type(Integer contest_type) {
        this.contest_type = contest_type;
    }

    public Long getContest_id() {
        return contest_id;
    }

    public void setContest_id(Long contest_id) {
        this.contest_id = contest_id;
    }

    @Override
    public JsonElement serialize(ContestNewsResponse contestNewsResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
