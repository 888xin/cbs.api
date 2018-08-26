package com.lifeix.cbs.contest.bean.circle;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;
import java.util.Map;

public class FriendCircleResponse implements JsonSerializer<ContestResponse>, Response {


    private static final long serialVersionUID = -8094262724668268252L;
    /**
     * 内容id
     */
    private Long friend_circle_id;

    /**
     * 发表类型
     */
    private Integer type;

    /**
     * 发表类型
     */
    private Integer friendType;

    /**
     * 内容
     */
    private String content;

    private Boolean hasContent;

    private String[] images;

    /**
     * 竞猜相关
     */
    private FriendCircleContestResponse contest;

    /**
     * 客户端标识
     */
    private String client;

    /**
     * 发表时间
     */
    private String create_time;

    /**
     * 处理后的时间
     */
    private String format_create_time;

    /**
     * 内容标志位 0 正常 | 1 被删除 | 2 被屏蔽
     */
    private Integer data_flag = 0;

    /**
     * 转发数
     */
    private Integer reblog_num;

    /**
     * 评论数
     */
    private Integer reply_num;

    /**
     * 编辑推荐
     */
    // private ContentChoiceResponse choice;

    /**
     * 当前用户信息
     */
    private CbsUserResponse user;

    /**
     * 源内容信息
     */
    private FriendCircleResponse source_content;

    /**
     * 标题
     */
    private String title;

    /**
     * 评论数量
     */
    private Integer commNum = 0;

    /**
     * 龙筹券
     */
    private Integer coupon ;

    /**
     * 投注理由数量
     */
    private Integer reason_num ;

    public Integer getReason_num() {
        return reason_num;
    }

    public void setReason_num(Integer reason_num) {
        this.reason_num = reason_num;
    }

    private Long user_id;
    private boolean has_content;
    private String params;

    private Long contest_id ;
    private Integer contest_type;

    private Map<Object, Object> bets ;

    public FriendCircleResponse() {
	super();
    }

    public Map<Object, Object> getBets() {
        return bets;
    }

    public void setBets(Map<Object, Object> bets) {
        this.bets = bets;
    }

    public Long getContest_id() {
        return contest_id;
    }

    public void setContest_id(Long contest_id) {
        this.contest_id = contest_id;
    }

    public Integer getContest_type() {
        return contest_type;
    }

    public void setContest_type(Integer contest_type) {
        this.contest_type = contest_type;
    }

    public Integer getCoupon() {
        return coupon;
    }

    public void setCoupon(Integer coupon) {
        this.coupon = coupon;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public String getContent() {
	return content;
    }

    public String[] getImages() {
	return images;
    }

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public boolean isHas_content() {
	return has_content;
    }

    public void setHas_content(boolean has_content) {
	this.has_content = has_content;
    }

    public String getParams() {
	return params;
    }

    public void setParams(String params) {
	this.params = params;
    }

    public Integer getCommNum() {
	return commNum;
    }

    public void setCommNum(Integer commNum) {
	this.commNum = commNum;
    }

    public void setImages(String[] images) {
	this.images = images;
    }

    public Long getFriend_circle_id() {
	return friend_circle_id;
    }

    public void setFriend_circle_id(Long friend_circle_id) {
	this.friend_circle_id = friend_circle_id;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public FriendCircleContestResponse getContest() {
	return contest;
    }

    public void setContest(FriendCircleContestResponse contest) {
	this.contest = contest;
    }

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
	if (this.create_time != null) {
	    setFormat_create_time(CbsTimeUtils.getContentTimeDiff(this.create_time));
	}
    }

    public Integer getData_flag() {
	return data_flag;
    }

    public void setData_flag(Integer data_flag) {
	this.data_flag = data_flag;
    }

    public String getClient() {
	return client;
    }

    public void setClient(String client) {
	this.client = client;
    }

    public Integer getReblog_num() {
	return reblog_num;
    }

    public void setReblog_num(Integer reblog_num) {
	this.reblog_num = reblog_num;
    }

    public Integer getReply_num() {
	return reply_num;
    }

    public void setReply_num(Integer reply_num) {
	this.reply_num = reply_num;
    }

    public String getFormat_create_time() {
	return format_create_time;
    }

    public void setFormat_create_time(String format_create_time) {
	this.format_create_time = format_create_time;
    }

    public CbsUserResponse getUser() {
	return user;
    }

    public void setUser(CbsUserResponse user) {
	this.user = user;
    }

    public FriendCircleResponse getSource_content() {
	return source_content;
    }

    public void setSource_content(FriendCircleResponse source_content) {
	this.source_content = source_content;
    }

    @Override
    public String getObjectName() {
	return "content";
    }

    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Boolean getHasContent() {
	return hasContent;
    }

    public void setHasContent(Boolean hasContent) {
	this.hasContent = hasContent;
    }

    public Integer getFriendType() {
	return friendType;
    }

    public void setFriendType(Integer friendType) {
	this.friendType = friendType;
    }

    @Override
    public JsonElement serialize(ContestResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
