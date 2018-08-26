package com.lifeix.cbs.contest.dto.bet;

/**
 * 让球胜平负
 * 
 * @author Peter
 * 
 */
public class BetJc extends Bet {

    private static final long serialVersionUID = -5807271430564463318L;

    /**
     * 盘口
     */
    private Double handicap;

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

    public Double getHandicap() {
	return handicap;
    }

    public void setHandicap(Double handicap) {
	this.handicap = handicap;
    }

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

    public void setAwayRoi(Double awayRoi) {
	this.awayRoi = awayRoi;
    }

}
