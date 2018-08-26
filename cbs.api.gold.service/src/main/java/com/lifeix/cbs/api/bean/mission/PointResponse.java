package com.lifeix.cbs.api.bean.mission;

import java.io.Serializable;

/**
 * Created by lhx on 16-6-20 下午2:50
 *
 * @Description
 */
public class PointResponse implements Serializable {

    private static final long serialVersionUID = -6017886298244089420L;

    private long id ;
    /**
     * 积分
     */
    private int point;
    /**
     * 龙筹面值
     */
    private int gold;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
