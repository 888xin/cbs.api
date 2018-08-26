package com.lifeix.cbs.api.dto.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 猜比赛基础用户信息
 * 
 * @author lifeix-sz
 * 
 */
public class CbsUser implements Serializable {

    private static final long serialVersionUID = 4667373102632423695L;

    /**
     * 主键
     */
    private Long userId;

    /**
     * 龙号
     */
    private Long userNo;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 头像
     */
    private String userPath;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 1 屏蔽 2 未验证 4 已验证
     */
    private Integer status;

    /**
     * 所在地
     */
    private String local;

    /**
     * 描述
     */
    private String aboutme;

    /**
     * 背景图地址
     */
    private String back;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 名字的拼音
     */
    private String namePinyin;

    /**
     * 手机号码
     */
    private String mobilePhone;

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Long getUserNo() {
	return userNo;
    }

    public void setUserNo(Long userNo) {
	this.userNo = userNo;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getUserPath() {
	return userPath;
    }

    public void setUserPath(String userPath) {
	this.userPath = userPath;
    }

    public Integer getGender() {
	return gender;
    }

    public String getNamePinyin() {
	return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
	this.namePinyin = namePinyin;
    }

    public String getMobilePhone() {
	return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
	this.mobilePhone = mobilePhone;
    }

    public void setGender(Integer gender) {
	this.gender = gender;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getLocal() {
	return local;
    }

    public void setLocal(String local) {
	this.local = local;
    }

    public String getAboutme() {
	return aboutme;
    }

    public void setAboutme(String aboutme) {
	this.aboutme = aboutme;
    }

    public String getBack() {
	return back;
    }

    public void setBack(String back) {
	this.back = back;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

}
