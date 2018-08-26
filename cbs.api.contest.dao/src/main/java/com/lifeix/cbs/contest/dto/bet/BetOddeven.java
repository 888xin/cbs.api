package com.lifeix.cbs.contest.dto.bet;

/**
 * Created by lhx on 16-5-4 下午3:40
 *
 * @Description
 */
public class BetOddeven extends Bet {

    private static final long serialVersionUID = 5589644806339581019L;
    /**
     * 单数赔率
     */
    private Double oddRoi;

    /**
     * 双数赔率
     */
    private Double evenRoi;

    public Double getOddRoi() {
        return oddRoi;
    }

    public void setOddRoi(Double oddRoi) {
        this.oddRoi = oddRoi;
    }

    public Double getEvenRoi() {
        return evenRoi;
    }

    public void setEvenRoi(Double evenRoi) {
        this.evenRoi = evenRoi;
    }
}
