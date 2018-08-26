package com.lifeix.cbs.contest.dto.bunch;

import java.io.Serializable;

/**
 * Created by lhx on 16-5-19 下午4:16
 *
 * @Description
 */
public class BunchPrize implements Serializable {

    private static final long serialVersionUID = -9097003333240042659L;
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
    private Integer winNum;
    /**
     * 奖品数量
     */
    private Integer num;

    /**
     * 照片
     */
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getWinNum() {
        return winNum;
    }

    public void setWinNum(Integer winNum) {
        this.winNum = winNum;
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
}
