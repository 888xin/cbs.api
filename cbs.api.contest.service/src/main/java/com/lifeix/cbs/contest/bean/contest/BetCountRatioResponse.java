package com.lifeix.cbs.contest.bean.contest;

import java.io.Serializable;

/**
 * Created by lhx on 16-6-3 上午10:32
 *
 * @Description
 */
public class BetCountRatioResponse implements Serializable {

    private static final long serialVersionUID = -5705541773858835205L;

    /**
     * 主队初始下注人数
     */
    private Integer home_count;
    /**
     * 客队初始下注人数
     */
    private Integer away_count;

    public Integer getHome_count() {
        return home_count;
    }

    public void setHome_count(Integer home_count) {
        this.home_count = home_count;
    }

    public Integer getAway_count() {
        return away_count;
    }

    public void setAway_count(Integer away_count) {
        this.away_count = away_count;
    }
}
