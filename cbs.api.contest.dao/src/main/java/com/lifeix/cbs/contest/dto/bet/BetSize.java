package com.lifeix.cbs.contest.dto.bet;

/**
 * Created by lhx on 16-4-27 上午9:56
 *
 * @Description
 */
public class BetSize extends Bet {

    private static final long serialVersionUID = 5759120946776903861L;
    /**
     * 盘口
     */
    private Double handicap;

    /**
     * 大球赔率
     */
    private Double bigRoi;

    /**
     * 小球赔率
     */
    private Double tinyRoi;

    public Double getHandicap() {
        return handicap;
    }

    public void setHandicap(Double handicap) {
        this.handicap = handicap;
    }

    public Double getBigRoi() {
        return bigRoi;
    }

    public void setBigRoi(Double bigRoi) {
        this.bigRoi = bigRoi;
    }

    public Double getTinyRoi() {
        return tinyRoi;
    }

    public void setTinyRoi(Double tinyRoi) {
        this.tinyRoi = tinyRoi;
    }
}
