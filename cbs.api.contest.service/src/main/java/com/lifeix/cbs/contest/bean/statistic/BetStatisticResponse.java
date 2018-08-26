package com.lifeix.cbs.contest.bean.statistic;

import com.lifeix.user.beans.Response;

/**
 * Created by lhx on 15-12-22 上午9:39
 *
 * @Description
 */
public class BetStatisticResponse implements Response {
    //日期
    String[] categories ;
    //数据
    int[] bets ;
    int[] people ;
    int[] op ;
    int[] jc ;
    int[] fb ;
    int[] bb ;

    public int[] getBets() {
        return bets;
    }

    public void setBets(int[] bets) {
        this.bets = bets;
    }

    public int[] getPeople() {
        return people;
    }

    public void setPeople(int[] people) {
        this.people = people;
    }

    public int[] getOp() {
        return op;
    }

    public void setOp(int[] op) {
        this.op = op;
    }

    public int[] getJc() {
        return jc;
    }

    public void setJc(int[] jc) {
        this.jc = jc;
    }

    public int[] getFb() {
        return fb;
    }

    public void setFb(int[] fb) {
        this.fb = fb;
    }

    public int[] getBb() {
        return bb;
    }

    public void setBb(int[] bb) {
        this.bb = bb;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
