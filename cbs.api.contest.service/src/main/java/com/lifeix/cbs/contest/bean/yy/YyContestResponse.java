package com.lifeix.cbs.contest.bean.yy;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 押押：赛事信息
 * 
 * @author peter
 * 
 */
public class YyContestResponse implements JsonSerializer<YyContestResponse>, Response {

    private static final long serialVersionUID = -2692055001101101172L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片集
     */
    private String images;

    /**
     * 描述
     */
    private String text;

    /**
     * 选项
     */
    private String options;

    /**
     * 分类Id
     */
    private Long cup_id;

    /**
     * 分类名称
     */
    private String cup_name;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 开始时间
     */
    private String start_time;

    /**
     * 开场时间
     */
    private String end_time;

    /**
     * 比赛状态
     */
    private int status;

    /**
     * 胜出方
     */
    private int winner;

    /**
     * 结算状态
     */
    private boolean settle;

    /**
     * 是否龙币
     */
    private boolean is_longbi;

    /**
     * 隐藏标识
     */
    private Boolean hide_flag;

    /**
     * 押押选项数据
     */
    private List<YyOptionResponse> option_data;

    /**
     * 参与人数
     */
    private int total;

    /**
     * 初始统计数
     */
    private int init_count;

    /**
     * 我的下单数量
     */
    private int user_bet = 0;

    /**
     * 展示方式
     */
    private int show_type;

    /**
     * 是否精选
     */
    private Boolean good;

    /**
     * 是否活动
     */
    private Boolean activity_flag;

    /**
     * 列表图片
     */
    private String list_image ;

    public String getList_image() {
        return list_image;
    }

    public void setList_image(String list_image) {
        this.list_image = list_image;
    }

    public Boolean getActivity_flag() {
        return activity_flag;
    }

    public void setActivity_flag(Boolean activity_flag) {
        this.activity_flag = activity_flag;
    }

    public Boolean getGood() {
        return good;
    }

    public void setGood(Boolean good) {
        this.good = good;
    }

    public int getShow_type() {
        return show_type;
    }

    public void setShow_type(int show_type) {
        this.show_type = show_type;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getImages() {
	return images;
    }

    public void setImages(String images) {
	this.images = images;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getOptions() {
	return options;
    }

    public void setOptions(String options) {
	this.options = options;
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

    public String getEnd_time() {
	return end_time;
    }

    public void setEnd_time(String end_time) {
	this.end_time = end_time;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public int getWinner() {
	return winner;
    }

    public void setWinner(int winner) {
	this.winner = winner;
    }

    public boolean isSettle() {
	return settle;
    }

    public void setSettle(boolean settle) {
	this.settle = settle;
    }

    public boolean isIs_longbi() {
	return is_longbi;
    }

    public void setIs_longbi(boolean is_longbi) {
	this.is_longbi = is_longbi;
    }

    public Boolean getHide_flag() {
	return hide_flag;
    }

    public void setHide_flag(Boolean hide_flag) {
	this.hide_flag = hide_flag;
    }

    public List<YyOptionResponse> getOption_data() {
	return option_data;
    }

    public void setOption_data(List<YyOptionResponse> option_data) {
	this.option_data = option_data;
    }

    public int getTotal() {
	return total;
    }

    public void setTotal(int total) {
	this.total = total;
    }

    public int getUser_bet() {
	return user_bet;
    }

    public void setUser_bet(int user_bet) {
	this.user_bet = user_bet;
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
    public JsonElement serialize(YyContestResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
