package com.lifeix.cbs.contest.dto.bb;

import java.io.Serializable;

public class BbPlayer implements Serializable {

    private static final long serialVersionUID = -6316429362901069024L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 雷达球员ID
     */
    private String targetId;

    /**
     * 球员全名
     */
    private String name;

    /**
     * 球员名
     */
    private String firstName;

    /**
     * 球员姓
     */
    private String lastName;

    /**
     * 当前效力的球队ID
     */
    private Long teamId;

    public BbPlayer() {

    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getTargetId() {
	return targetId;
    }

    public void setTargetId(String targetId) {
	this.targetId = targetId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
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

    public Long getTeamId() {
	return teamId;
    }

    public void setTeamId(Long teamId) {
	this.teamId = teamId;
    }

}
