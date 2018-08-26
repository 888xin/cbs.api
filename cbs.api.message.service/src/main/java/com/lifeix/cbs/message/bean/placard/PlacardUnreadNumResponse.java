package com.lifeix.cbs.message.bean.placard;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * Created by lhx on 15-10-19 下午3:44
 *
 * @Description 个人未读广告数量返回bean
 */
public class PlacardUnreadNumResponse implements JsonSerializer<PlacardUnreadNumResponse>, Response {

    private Long user_id;
    private Integer unread_num ;

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Integer getUnread_num() {
        return unread_num;
    }

    public void setUnread_num(Integer unread_num) {
        this.unread_num = unread_num;
    }

    @Override
    public JsonElement serialize(PlacardUnreadNumResponse placardUnreadNumResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
