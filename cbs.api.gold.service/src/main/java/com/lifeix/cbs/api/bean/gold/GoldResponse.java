package com.lifeix.cbs.api.bean.gold;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class GoldResponse implements JsonSerializer<GoldResponse>, Response {

    private static final long serialVersionUID = 1919111141166356098L;
    
    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 账户金额
     */
    private double balance;

    /**
     * 冻结金额
     */
    private double frozen;

    /**
     * 收入金额
     */
    private double income;

    /**
     * 支出金额
     */
    private double outlay;

    /**
     * 账户状态 0 正常 1 停用
     */
    private int status;
    
    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getFrozen() {
        return frozen;
    }

    public void setFrozen(double frozen) {
        this.frozen = frozen;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getOutlay() {
        return outlay;
    }

    public void setOutlay(double outlay) {
        this.outlay = outlay;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(GoldResponse src, Type typeOfSrc, JsonSerializationContext context) {

	return context.serialize(src);
    }

}
