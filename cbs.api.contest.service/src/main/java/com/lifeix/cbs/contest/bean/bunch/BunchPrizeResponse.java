package com.lifeix.cbs.contest.bean.bunch;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * Created by lhx on 16-5-19 下午5:03
 *
 * @Description
 */
public class BunchPrizeResponse implements JsonSerializer<BunchPrizeResponse>, Response {

    private static final long serialVersionUID = 1859726511898633817L;

    private Long id;

    private Long bunchId;

    private String name ;

    private Integer price ;
    /**
     * 0龙筹 1龙币 2实物
     */
    private Integer type;
    /**
     * 对应要下对的场数
     */
    private Integer win_num;
    /**
     * 奖品数量
     */
    private Integer num;
    /**
     * 照片
     */
    private String image;

    /**
     * 全部可能获奖名单
     */
    private Set<Long> all;
    /**
     * 可能真正获奖名单
     */
    private Set<Long> maybe;

    public Set<Long> getAll() {
        return all;
    }

    public void setAll(Set<Long> all) {
        this.all = all;
    }

    public Set<Long> getMaybe() {
        return maybe;
    }

    public void setMaybe(Set<Long> maybe) {
        this.maybe = maybe;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public JsonElement serialize(BunchPrizeResponse bunchPrizeResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
