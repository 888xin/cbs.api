package com.lifeix.cbs.content.bean.contest;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;

/**
 * Created by lhx on 16-4-15 上午10:48
 *
 * @Description
 */
public class ContestNewsContentResponse implements JsonSerializer<ContestNewsContentResponse>, Response {

    private static final long serialVersionUID = 4803481159259916025L;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    @Override
    public JsonElement serialize(ContestNewsContentResponse contestNewsContentResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
