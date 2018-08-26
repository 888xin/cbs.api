package com.lifeix.cbs.api.bean.mission;

import java.io.Serializable;

/**
 * Created by lhx on 16-6-20 下午2:50
 *
 * @Description
 */
public class MissionResponse implements Serializable {

    /**
     * 名称
     */
    private String name;
    /**
     * 积分
     */
    private int point;
    /**
     * 类型
     */
    private int type;
    /**
     * 是否完成
     */
    private boolean finish ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
}
