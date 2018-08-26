package com.lifeix.cbs.contest.bean.fb;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 赛事信息
 * 
 * @author peter
 * 
 */
public class ContestResponse implements JsonSerializer<ContestResponse>, Response {

    private static final long serialVersionUID = -8786081619452791224L;

    /**
     * 主键
     */
    private Long contest_id;

    /**
     * 第三方id
     */
    private Long target_id;

    /**
     * 联赛id
     */
    private Long cup_id;

    /**
     * 名称
     */
    private String cup_name;

    /**
     * 名称
     */
    private String color;

    /**
     * 名称
     */
    private Integer contest_type;

    /**
     * 主场球队id
     */
    private Long home_team;

    /**
     * 主场分数
     */
    private int home_scores;

    /**
     * 主队排名
     */
    private String home_rank;

    /**
     * 客场球队id
     */
    private Long away_team;

    /**
     * 客场分数
     */
    private int away_scores;

    /**
     * 客队排名
     */
    private String away_rank;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 开始时间
     */
    private String start_time;

    /**
     * 比赛状态
     */
    private int status;

    /**
     * 小节剩余时间
     */
    private String remain_time;

    /**
     * 结算状态，默认为0--未结算
     */
    private int settle;

    /**
     * 赔率玩法
     */
    private int odds_type;

    /**
     * 联赛层级
     */
    private int level = 0;

    /**
     * 下单统计
     */
    private int bet_count = 0;

    /**
     * 比分锁定
     */
    private Boolean lock_flag;

    /**
     * 是否输入五大联赛
     */
    private boolean belong_five;

    /**
     * 开场时间
     */
    private String open_time;

    /**
     * 是否龙币
     */
    private boolean longbi;

    /**
     * 主场球队
     */
    private TeamResponse h_t;

    /**
     * 客场球队
     */
    private TeamResponse a_t;

    /**
     * 赛事结算状态
     */
    private int settle_statu;

    /**
     * 赛事相关数据持有标志位
     */
    private int ext_flag = 0;

    /**
     * im房间Id
     */
    private Long room_id;

    /**
     * 比赛最终信息
     */
    private ContestResultResponse final_result;

    /**
     * 初始统计数
     */
    private int init_count;
    /**
     * 主队初始下注人数
     */
    private int home_count;
    /**
     * 客队初始下注人数
     */
    private int away_count;
    /**
     * 主队下注比例
     */
    private double home_ratio = 50;
    /**
     * 客队下注比例
     */
    private double away_ratio = 50;

    public double getHome_ratio() {
        return home_ratio;
    }

    public void setHome_ratio(double home_ratio) {
        this.home_ratio = home_ratio;
    }

    public double getAway_ratio() {
        return away_ratio;
    }

    public void setAway_ratio(double away_ratio) {
        this.away_ratio = away_ratio;
    }

    public int getHome_count() {
        return home_count;
    }

    public void setHome_count(int home_count) {
        this.home_count = home_count;
    }

    public int getAway_count() {
        return away_count;
    }

    public void setAway_count(int away_count) {
        this.away_count = away_count;
    }

    public String getHome_rank() {
	return home_rank;
    }

    public void setHome_rank(String home_rank) {
	this.home_rank = home_rank;
    }

    public String getAway_rank() {
	return away_rank;
    }

    public void setAway_rank(String away_rank) {
	this.away_rank = away_rank;
    }

    public TeamResponse getH_t() {
	return h_t;
    }

    public void setH_t(TeamResponse h_t) {
	this.h_t = h_t;
    }

    public TeamResponse getA_t() {
	return a_t;
    }

    public void setA_t(TeamResponse a_t) {
	this.a_t = a_t;
    }

    public Long getContest_id() {
	return contest_id;
    }

    public void setContest_id(Long contest_id) {
	this.contest_id = contest_id;
    }

    public Long getTarget_id() {
	return target_id;
    }

    public boolean isLongbi() {
	return longbi;
    }

    public void setLongbi(boolean isLongbi) {
	this.longbi = isLongbi;
    }

    public void setTarget_id(Long target_id) {
	this.target_id = target_id;
    }

    public Long getCup_id() {
	return cup_id;
    }

    public void setCup_id(Long cup_id) {
	this.cup_id = cup_id;
    }

    public String getCup_name() {
	return cup_name;
    }

    public void setCup_name(String cup_name) {
	this.cup_name = cup_name;
    }

    public String getColor() {
	return color;
    }

    public void setColor(String color) {
	this.color = color;
    }

    public Long getHome_team() {
	return home_team;
    }

    public void setHome_team(Long home_team) {
	this.home_team = home_team;
    }

    public int getHome_scores() {
	return home_scores;
    }

    public void setHome_scores(int home_scores) {
	this.home_scores = home_scores;
    }

    public Long getAway_team() {
	return away_team;
    }

    public void setAway_team(Long away_team) {
	this.away_team = away_team;
    }

    public int getAway_scores() {
	return away_scores;
    }

    public void setAway_scores(int away_scores) {
	this.away_scores = away_scores;
    }

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    public String getStart_time() {
	return start_time;
    }

    public void setStart_time(String start_time) {
	this.start_time = start_time;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public String getRemain_time() {
	return remain_time;
    }

    public void setRemain_time(String remain_time) {
	this.remain_time = remain_time;
    }

    public int getSettle() {
	return settle;
    }

    public void setSettle(int settle) {
	this.settle = settle;
    }

    public int getOdds_type() {
	return odds_type;
    }

    public void setOdds_type(int odds_type) {
	this.odds_type = odds_type;
    }

    public int getLevel() {
	return level;
    }

    public void setLevel(int level) {
	this.level = level;
    }

    public boolean isBelong_five() {
	return belong_five;
    }

    public void setBelong_five(boolean belong_five) {
	this.belong_five = belong_five;
    }

    public String getOpen_time() {
	return open_time;
    }

    public void setOpen_time(String open_time) {
	this.open_time = open_time;
    }

    public int getBet_count() {
	return bet_count;
    }

    public void setBet_count(int bet_count) {
	this.bet_count = bet_count;
    }

    public Boolean getLock_flag() {
	return lock_flag;
    }

    public void setLock_flag(Boolean lock_flag) {
	this.lock_flag = lock_flag;
    }

    public Integer getContest_type() {
	return contest_type;
    }

    public void setContest_type(Integer contest_type) {
	this.contest_type = contest_type;
    }

    public int getSettle_statu() {
	return settle_statu;
    }

    public void setSettle_statu(int settle_statu) {
	this.settle_statu = settle_statu;
    }

    public int getExt_flag() {
	return ext_flag;
    }

    public void setExt_flag(int ext_flag) {
	this.ext_flag = ext_flag;
    }

    public Long getRoom_id() {
	return room_id;
    }

    public void setRoom_id(Long room_id) {
	this.room_id = room_id;
    }

    public ContestResultResponse getFinal_result() {
	return final_result;
    }

    public void setFinal_result(ContestResultResponse final_result) {
	this.final_result = final_result;
    }

    public int getInit_count() {
	return init_count;
    }

    public void setInit_count(int init_count) {
	this.init_count = init_count;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(ContestResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
