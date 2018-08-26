package com.lifeix.cbs.contest.bean.robot;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.bean.user.UserContestStatisticsResponse;
import com.lifeix.user.beans.Response;

/**
 * 机器人信息
 * 
 * @author Peter
 * 
 */
public class BetRobotResponse implements JsonSerializer<BetRobotResponse>, Response {

    private static final long serialVersionUID = -4362239494586369108L;

    private Long user_id;

    /**
     * 用户信息
     */
    private String user_info;

    /**
     * 足球玩法
     */
    private int fb_odds;

    /**
     * 篮球玩法
     */
    private int bb_odds;

    /**
     * 玩法配置
     */
    private String setting;

    /**
     * 最近下单记录
     */
    private String history;

    /**
     * 可下单时间(小时)
     */
    private String bet_time;

    /**
     * 是否参与锦标赛
     */
    private boolean game_flag;

    /**
     * 锦标赛时间
     */
    private String game_time;

    /**
     * 是否参与押押
     */
    private boolean pk_flag;

    private String create_time;

    private String update_time;

    private boolean close_flag;

    /**
     * 唤醒次数
     */
    private int call_count;

    /**
     * 龙号
     */
    private Long long_no;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private String photo_path;

    /**
     * 配置解析
     */
    private List<BetRobotSetting> settings;

    /**
     * 时间解析
     */
    private String[] bet_times;

    /**
     * 统计信息
     */
    private UserContestStatisticsResponse stat;

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public int getFb_odds() {
	return fb_odds;
    }

    public void setFb_odds(int fb_odds) {
	this.fb_odds = fb_odds;
    }

    public int getBb_odds() {
	return bb_odds;
    }

    public void setBb_odds(int bb_odds) {
	this.bb_odds = bb_odds;
    }

    public String getSetting() {
	return setting;
    }

    public void setSetting(String setting) {
	this.setting = setting;
    }

    public String getHistory() {
	return history;
    }

    public void setHistory(String history) {
	this.history = history;
    }

    public boolean isGame_flag() {
	return game_flag;
    }

    public void setGame_flag(boolean game_flag) {
	this.game_flag = game_flag;
    }

    public String getGame_time() {
	return game_time;
    }

    public void setGame_time(String game_time) {
	this.game_time = game_time;
    }

    public boolean isPk_flag() {
	return pk_flag;
    }

    public void setPk_flag(boolean pk_flag) {
	this.pk_flag = pk_flag;
    }

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    public String getUpdate_time() {
	return update_time;
    }

    public void setUpdate_time(String update_time) {
	this.update_time = update_time;
    }

    public boolean isClose_flag() {
	return close_flag;
    }

    public void setClose_flag(boolean close_flag) {
	this.close_flag = close_flag;
    }

    public String getUser_info() {
	return user_info;
    }

    public void setUser_info(String user_info) {
	this.user_info = user_info;
    }

    public int getCall_count() {
	return call_count;
    }

    public void setCall_count(int call_count) {
	this.call_count = call_count;
    }

    public String getBet_time() {
	return bet_time;
    }

    public void setBet_time(String bet_time) {
	this.bet_time = bet_time;
    }

    public Long getLong_no() {
	return long_no;
    }

    public void setLong_no(Long long_no) {
	this.long_no = long_no;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<BetRobotSetting> getSettings() {
	return settings;
    }

    public void setSettings(List<BetRobotSetting> settings) {
	this.settings = settings;
    }

    public String[] getBet_times() {
	return bet_times;
    }

    public void setBet_times(String[] bet_times) {
	this.bet_times = bet_times;
    }

    public String getPhoto_path() {
	return photo_path;
    }

    public void setPhoto_path(String photo_path) {
	this.photo_path = photo_path;
    }

    public UserContestStatisticsResponse getStat() {
	return stat;
    }

    public void setStat(UserContestStatisticsResponse stat) {
	this.stat = stat;
    }

    @Override
    public String getObjectName() {
	return "robot";
    }

    @Override
    public JsonElement serialize(BetRobotResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }
}
