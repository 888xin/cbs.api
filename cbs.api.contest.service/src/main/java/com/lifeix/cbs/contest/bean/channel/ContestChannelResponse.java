package com.lifeix.cbs.contest.bean.channel;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class ContestChannelResponse implements JsonSerializer<ContestChannelResponse>, Response {
    
    /**
     * 
     */
    private static final long serialVersionUID = 5205221084968877663L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;
    
    /**
     * 赛事id
     */
    private List<Long> cup_id;
    
    /**
     * 排序
     */
    private int sort;

    /**
     * 创建时间
     */
    private String create_time;
    /**
     * 分类下的赛事类型（0足球|1篮球）
     */
    private Integer contest_type;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public List<Long> getCup_id() {
        return cup_id;
    }

    public void setCup_id(List<Long> cup_id) {
        this.cup_id = cup_id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getContest_type() {
        return contest_type;
    }

    public void setContest_type(Integer contest_type) {
        this.contest_type = contest_type;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(ContestChannelResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
