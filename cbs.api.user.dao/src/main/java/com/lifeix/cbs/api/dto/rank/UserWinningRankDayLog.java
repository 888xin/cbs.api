package com.lifeix.cbs.api.dto.rank;

import java.io.Serializable;

/**
 * 用户胜率排行记录
 * @author pengkw
 */
public class UserWinningRankDayLog implements Serializable{
	
	private static final long serialVersionUID = 8992292832336856912L;

	/**
	 * 年份
	 */
	private Integer year;
	
	/**
	 * 一年中的第几周
	 */
	private Integer day;
	
	/**
	 * 排名靠前的用户id集合
	 */
	private String firstIds;
	
	/**
	 * 排名最后的用户id集合
	 */
	private String lastIds;
	
	private Boolean isLongbi;
	private String table= "cbs_user_winning_rank_day_log";
	
	public UserWinningRankDayLog() {
		super();
	}

	public UserWinningRankDayLog(Integer year, Integer day, String firstIds,
			String lastIds) {
		super();
		this.year = year;
		this.day = day;
		this.firstIds = firstIds;
		this.lastIds = lastIds;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getFirstIds() {
		return firstIds;
	}

	public Integer getDay() {
	    return day;
	}

	public void setDay(Integer day) {
	    this.day = day;
	}

	public Boolean getIsLongbi() {
	    return isLongbi;
	}

	public void setIsLongbi(Boolean isLongbi) {
	    this.isLongbi = isLongbi;
	}

	public String getTable() {
	    return table;
	}

	public void setTable(String table) {
	    this.table = table;
	}

	public void setFirstIds(String firstIds) {
		this.firstIds = firstIds;
	}

	public String getLastIds() {
		return lastIds;
	}

	public void setLastIds(String lastIds) {
		this.lastIds = lastIds;
	}
	
}
