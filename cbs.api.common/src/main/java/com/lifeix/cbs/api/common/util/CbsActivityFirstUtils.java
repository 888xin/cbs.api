package com.lifeix.cbs.api.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lifeix.framwork.util.JsonUtils;

public class CbsActivityFirstUtils {

    public static final String REWARD = "reward";
    public static final String RULE = "rule";

    public static void main(String args[]) {
	for (int i = 0; i < 100; i++) {
	    System.out.println(lottery(20D));
	}

    }

    public static Map<String, Object> lottery(Double money) {
	Map<String, Object> resut = new HashMap<String, Object>();
	Integer reward = 0;
	List<Lottery> list = initLotterys(money);
	int total = 0;// 总概率
	for (Lottery bean : list) {
	    total += bean.getPriority();
	}

	int random = new Random().nextInt(total);
	int prizeRate = 0;// 当前中奖率
	Iterator<Lottery> it = list.iterator();
	while (it.hasNext()) {
	    Lottery lottery = it.next();
	    prizeRate += lottery.getPriority();
	    if (random < prizeRate) {
		reward = lottery.getMoney();
		break;
	    }
	}
	resut.put(REWARD, reward);
	resut.put(RULE, JsonUtils.toJsonString(list));
	return resut;
    }

    public static List<Lottery> initLotterys(Double money) {
	List<Lottery> lotterys = new ArrayList<Lottery>();

	if (20 == money) {
	    lotterys.add(new Lottery(5, 70D));
	    lotterys.add(new Lottery(10, 30D));
	} else if (50 == money) {
	    lotterys.add(new Lottery(10, 70D));
	    lotterys.add(new Lottery(20, 30D));
	} else if (100 == money) {
	    lotterys.add(new Lottery(20, 70D));
	    lotterys.add(new Lottery(50, 30D));
	} else if (500 <= money) {
	    lotterys.add(new Lottery(50, 70D));
	    lotterys.add(new Lottery(100, 30D));
	}
	return lotterys;
    }
}

class Lottery {
    /** 中奖金额 */
    private Integer money;
    /** 奖品概率 */
    private Double priority;

    public Lottery() {
	super();
    }

    public Lottery(Integer money, Double priority) {
	super();
	this.money = money;
	this.priority = priority;
    }

    public Integer getMoney() {
	return money;
    }

    public void setMoney(Integer money) {
	this.money = money;
    }

    public Double getPriority() {
	return priority;
    }

    public void setPriority(Double priority) {
	this.priority = priority;
    }

}