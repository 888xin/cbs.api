package com.lifeix.cbs.contest.bean.bunch;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by lhx on 16-5-17 上午10:32
 *
 * @Description
 */
public class BunchBetResponse implements JsonSerializer<BunchBetResponse>, Response {

    private static final long serialVersionUID = -1649179388561338458L;

    private Long id ;
    /**
     * 比赛串id
     */
    private Long bunchId ;
    /**
     * 状态（0未结算 -1已结算 1结果已出）
     */
    private Integer status ;
    /**
     * 该注赢的场数
     */
    private Integer win_num ;
    /**
     * 创建时间
     */
    private String create_time;
    /**
     * 下注选项（cbs_contest_bunch表里options对应的序号1：支持方1，options对应的序号2：支持方2）
     */
    private String support ;

    private CbsUserResponse user ;

    public CbsUserResponse getUser() {
        return user;
    }

    public void setUser(CbsUserResponse user) {
        this.user = user;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getWin_num() {
        return win_num;
    }

    public void setWin_num(Integer win_num) {
        this.win_num = win_num;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBunchId() {
        return bunchId;
    }

    public void setBunchId(Long bunchId) {
        this.bunchId = bunchId;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    @Override
    public JsonElement serialize(BunchBetResponse bunchBetResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
