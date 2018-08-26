package com.lifeix.cbs.contest.bean.odds;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;


public class OddsDssResponse implements JsonSerializer<OddsDssResponse>, Response{
	
	/**
	 * 赛事ID
	 */
	private Long contest_id;
	
	/**
	 * 单数赔率
	 */
	private Double odd_roi;
	
	/**
	 * 双数赔率
	 */
	private Double even_roi;
	
	/**
	 * 是否封盘
	 */
	private Boolean bet_flag;
	
	/**
	 * 是否走地
	 */
	private Boolean play_flag;
	
	/**
	 * 对应比赛是否结束
	 */
	private Boolean close_flag;
	
	
	public Long getContest_id() {
		return contest_id;
	}

	public void setContest_id(Long contest_id) {
		this.contest_id = contest_id;
	}

	public Double getOdd_roi() {
		return odd_roi;
	}

	public void setOdd_roi(Double odd_roi) {
		this.odd_roi = odd_roi;
	}

	public Double getEven_roi() {
		return even_roi;
	}

	public void setEven_roi(Double even_roi) {
		this.even_roi = even_roi;
	}

	public Boolean getBet_flag() {
		return bet_flag;
	}

	public void setBet_flag(Boolean bet_flag) {
		this.bet_flag = bet_flag;
	}

	public Boolean getPlay_flag() {
		return play_flag;
	}

	public void setPlay_flag(Boolean play_flag) {
		this.play_flag = play_flag;
	}

	public Boolean getClose_flag() {
		return close_flag;
	}

	public void setClose_flag(Boolean close_flag) {
		this.close_flag = close_flag;
	}

	@Override
	public String getObjectName() {
		return "ft_dss";
	}

	@Override
	public JsonElement serialize(OddsDssResponse src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src);
	}
}
