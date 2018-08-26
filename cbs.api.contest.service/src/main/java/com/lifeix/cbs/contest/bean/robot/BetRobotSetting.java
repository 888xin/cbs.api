package com.lifeix.cbs.contest.bean.robot;

/**
 * 机器人下单配置
 * 
 * @author Peter
 * 
 */
public class BetRobotSetting {

    /**
     * 玩法类型
     */
    private Integer oddsType;

    /**
     * 倾向值 0 随机 1 主 | 大 | 单 2 平 3 客 | 小| 双
     */
    private Integer type;

    /**
     * 水位边界值 0 随机 +n 大于等于n的水位 -n 小于等于n的水位
     */
    private double boder;

    /**
     * 数量
     */
    private int num;

    public Integer getOddsType() {
	return oddsType;
    }

    public void setOddsType(Integer oddsType) {
	this.oddsType = oddsType;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public double getBoder() {
	return boder;
    }

    public void setBoder(double boder) {
	this.boder = boder;
    }

    public int getNum() {
	return num;
    }

    public void setNum(int num) {
	this.num = num;
    }

    public String getJson() {
	return String.format("{\"type\":%d,\"boder\":%s,\"num\":%d}", type, boder, num);
    }

}
