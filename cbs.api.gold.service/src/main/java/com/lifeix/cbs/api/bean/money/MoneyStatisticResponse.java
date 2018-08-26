package com.lifeix.cbs.api.bean.money;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class MoneyStatisticResponse implements JsonSerializer<MoneyStatisticResponse>, Response {

    private static final long serialVersionUID = 1919111141166356098L;
    
    /**
     * id
     */
    private long id;
    
    /**
     * 年份
     */
    private int year;
    
    /**
     * 天数
     */
    private int day;
    
    /**
     * 收入
     */
    private double income;
   
    /**
     * 支出
     */
    private double outlay;
    
    /**
     * 合计
     */
    private double total;
    
    /**
     * 收入记录数
     */
    private long in_counts;
    
    /**
     * 支出记录数
     */
    private long out_counts;
    
    /**
     * 生成时间
     */
    private String create_time;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public long getIn_counts() {
        return in_counts;
    }

    public void setIn_counts(long in_counts) {
        this.in_counts = in_counts;
    }

    public long getOut_counts() {
        return out_counts;
    }

    public void setOut_counts(long out_counts) {
        this.out_counts = out_counts;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(MoneyStatisticResponse src, Type typeOfSrc, JsonSerializationContext context) {

	return context.serialize(src);
    }

}
