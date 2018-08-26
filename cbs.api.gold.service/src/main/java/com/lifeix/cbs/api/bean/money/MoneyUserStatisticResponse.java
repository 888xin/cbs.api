package com.lifeix.cbs.api.bean.money;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;

/**
 * Created by lhx on 16-4-13 上午11:22
 *
 * @Description
 */
public class MoneyUserStatisticResponse implements JsonSerializer<MoneyUserStatisticResponse>, Response {


    private static final long serialVersionUID = 1123337947849169471L;
    /**
     * 用户
     */
    private CbsUserResponse user ;

    /**
     * 用户收入
     */
    private double income ;

    /**
     * 用户支出
     */
    private double outlay;

    /**
     * 系统结算收入
     */
    private double system_income ;

    /**
     * 系统结算支出
     */
    private double system_outlay;

    public double getSystem_income() {
        return system_income;
    }

    public void setSystem_income(double system_income) {
        this.system_income = system_income;
    }

    public double getSystem_outlay() {
        return system_outlay;
    }

    public void setSystem_outlay(double system_outlay) {
        this.system_outlay = system_outlay;
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

    public CbsUserResponse getUser() {
        return user;
    }

    public void setUser(CbsUserResponse user) {
        this.user = user;
    }

    @Override
    public JsonElement serialize(MoneyUserStatisticResponse moneyUserStatisticResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
