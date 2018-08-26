package com.lifeix.cbs.api.dto.gold;

import java.io.Serializable;

/**
 * 龙币筹码账户
 * 
 * @author jacky
 *
 */
public class Gold implements Serializable {

    private static final long serialVersionUID = -1895354078650910253L;

    /**
     * 用户id
     */
    private Long userId;

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

    public Gold() {
	super();
    }

    public Gold(Long userId, double balance, double frozen, double income, double outlay, int status) {
	super();
	this.userId = userId;
	this.balance = balance;
	this.frozen = frozen;
	this.income = income;
	this.outlay = outlay;
	this.status = status;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
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
    public String toString() {
	return "Gold [userId=" + userId + ", balance=" + balance + ", frozen=" + frozen + ", income=" + income + ", outlay="
	        + outlay + ", status=" + status + "]";
    }
    
    
}
