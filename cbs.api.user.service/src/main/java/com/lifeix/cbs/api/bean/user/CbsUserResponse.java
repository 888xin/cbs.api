package com.lifeix.cbs.api.bean.user;

import com.lifeix.user.beans.Response;

public class CbsUserResponse implements Response {
    private static final long serialVersionUID = -523151880287410140L;

    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 龙号
     */
    private Long long_no;

    /**
     * name
     */
    private String name;

    /**
     * 头像
     */
    private String head;

    /**
     * 背景图片
     */
    private String background;

    /**
     * 1 屏蔽 2 未验证 4 已验证
     */
    private Integer status;
    /**
     * 关系类型 0 无关系 2 关注关系 4 相互关注关系
     */
    private Integer relationship_type;

    /**
     * 描述
     */
    private String aboutme;

    /**
     * 性别
     */
    private int gender = 1;

    /**
     * 位置
     */
    private String location;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String mobilePhone;
    /**
     * 名
     */
    private String firstName;

    /**
     * 姓
     */
    private String lastName;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 个人空间域名
     */
    private String domainName;
    /**
     * 是否在线
     */
    private boolean online;
    /**
     * 头像图片ID
     */
    private Long avatarsId;

    /**
     * 0 普通用户 10 神秘人 11 酒店帐号 12 评委 21 机构
     */
    private Integer accountType = 0;

    /**
     * 手机头像
     */
    private String mobilePhoto;

    /**
     * 个性空间
     */
    private String spaceName;

    /**
     * 地点经度
     */
    private String lng;
    /**
     * 地点纬度
     */
    private String lat;

    /**
     * 名字的拼音
     */
    private String namePinyin;

    /**
     * 个人简介
     */
    private String miniBlog;

    /**
     * 可用余额
     */
    private Double availableBalance;

    /**
     * 可用龙币
     */
    private Double availableMoney;

    /**
     * 连续登录奖励
     */
    private Double award;

    /**
     * 连续登录次数
     */
    private Integer loginTimes;

    /**
     * 连续登录次数
     */
    private Boolean show_mission ;

    private String token;

    private UserContestStatisticsResponse userStatisticsResponse;

    public CbsUserResponse() {
	super();
    }

    public Boolean getShow_mission() {
        return show_mission;
    }

    public void setShow_mission(Boolean show_mission) {
        this.show_mission = show_mission;
    }

    public String getToken() {
	return token;
    }

    public void setToken(String token) {
	this.token = token;
    }

    public String getBackground() {
	return background;
    }

    public void setBackground(String background) {
	this.background = background;
    }

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public Long getLong_no() {
	return long_no;
    }

    public Double getAvailableBalance() {
	return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
	this.availableBalance = availableBalance;
    }

    public void setLong_no(Long long_no) {
	this.long_no = long_no;
    }

    public String getName() {
	return name;
    }

    public Integer getStatus() {
	return status;
    }

    public Double getAvailableMoney() {
	return availableMoney;
    }

    public void setAvailableMoney(Double availableMoney) {
	this.availableMoney = availableMoney;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public Integer getRelationship_type() {
	return relationship_type;
    }

    public void setRelationship_type(Integer relationship_type) {
	this.relationship_type = relationship_type;
    }

    public String getAboutme() {
	return aboutme;
    }

    public void setAboutme(String aboutme) {
	this.aboutme = aboutme;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getHead() {
	return head;
    }

    public void setHead(String head) {
	this.head = head;
    }

    public int getGender() {
	return gender;
    }

    public void setGender(int gender) {
	this.gender = gender;
    }

    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getMobilePhone() {
	return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
	this.mobilePhone = mobilePhone;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getDomainName() {
	return domainName;
    }

    public void setDomainName(String domainName) {
	this.domainName = domainName;
    }

    public boolean isOnline() {
	return online;
    }

    public void setOnline(boolean online) {
	this.online = online;
    }

    public Long getAvatarsId() {
	return avatarsId;
    }

    public void setAvatarsId(Long avatarsId) {
	this.avatarsId = avatarsId;
    }

    public Integer getAccountType() {
	return accountType;
    }

    public void setAccountType(Integer accountType) {
	this.accountType = accountType;
    }

    public String getMobilePhoto() {
	return mobilePhoto;
    }

    public void setMobilePhoto(String mobilePhoto) {
	this.mobilePhoto = mobilePhoto;
    }

    public String getSpaceName() {
	return spaceName;
    }

    public void setSpaceName(String spaceName) {
	this.spaceName = spaceName;
    }

    public String getLng() {
	return lng;
    }

    public void setLng(String lng) {
	this.lng = lng;
    }

    public String getLat() {
	return lat;
    }

    public void setLat(String lat) {
	this.lat = lat;
    }

    public String getNamePinyin() {
	return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
	this.namePinyin = namePinyin;
    }

    public String getMiniBlog() {
	return miniBlog;
    }

    public void setMiniBlog(String miniBlog) {
	this.miniBlog = miniBlog;
    }

    public Double getAward() {
	return award;
    }

    public void setAward(Double award) {
	this.award = award;
    }

    public Integer getLoginTimes() {
	return loginTimes;
    }

    public void setLoginTimes(Integer loginTimes) {
	this.loginTimes = loginTimes;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    public UserContestStatisticsResponse getUserStatisticsResponse() {
	return userStatisticsResponse;
    }

    public void setUserStatisticsResponse(UserContestStatisticsResponse userContestStatisticsResponse) {
	this.userStatisticsResponse = userContestStatisticsResponse;
    }

}
