package com.lifeix.cbs.contest.dto.contest;

import java.io.Serializable;

public class ScoreModule implements Serializable {

    private static final long serialVersionUID = 685503484508181373L;

    private Long id;

    private Integer contestType;

    private Integer moduleValue;

    public ScoreModule() {

    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Integer getContestType() {
	return contestType;
    }

    public void setContestType(Integer contestType) {
	this.contestType = contestType;
    }

    public Integer getModuleValue() {
	return moduleValue;
    }

    public void setModuleValue(Integer moduleValue) {
	this.moduleValue = moduleValue;
    }

}
