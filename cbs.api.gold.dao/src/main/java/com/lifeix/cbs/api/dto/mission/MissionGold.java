package com.lifeix.cbs.api.dto.mission;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhx on 16-6-17 下午5:52
 *
 * @Description 积分兑换龙筹
 */
public class MissionGold implements Serializable {

    private static final long serialVersionUID = -287558716334862008L;

    private Long id;

    /**
     * 积分
     */
    private Integer point;

    /**
     * 筹码面值
     */
    private Integer price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
