package com.lifeix.cbs.contest.dto.bet;

public class BetOp extends Bet {

    private static final long serialVersionUID = -6880809221612162119L;

    /**
     * 主胜赔率
     */
    private Double homeRoi;

    /**
     * 平赔率
     */
    private Double drawRoi;

    /**
     * 客胜赔率
     */
    private Double awayRoi;
    
    /**
     * 龙筹券
     */
    private Integer coupon;

    public Double getHomeRoi() {
	return homeRoi;
    }

    public void setHomeRoi(Double homeRoi) {
	this.homeRoi = homeRoi;
    }

    public Double getDrawRoi() {
	return drawRoi;
    }

    public void setDrawRoi(Double drawRoi) {
	this.drawRoi = drawRoi;
    }

    public Double getAwayRoi() {
	return awayRoi;
    }

    public Integer getCoupon() {
        return coupon;
    }

    public void setCoupon(Integer coupon) {
        this.coupon = coupon;
    }

    public void setAwayRoi(Double awayRoi) {
	this.awayRoi = awayRoi;
    }

}
