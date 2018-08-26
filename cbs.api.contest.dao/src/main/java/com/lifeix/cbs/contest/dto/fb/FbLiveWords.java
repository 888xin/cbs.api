package com.lifeix.cbs.contest.dto.fb;

import java.io.Serializable;

public class FbLiveWords implements Serializable {

    private static final long serialVersionUID = 9111573733610762192L;

    /**
     * 主键
     */
    private Long phraseId;

    /**
     * 对应赛事ID
     */
    private Long contestId;

    /**
     * 雷达赛事ID
     */
    private Long targetId;

    /**
     * 雷达文字ID
     */
    private Long targetPhraseId;

    /**
     * 文字内容
     */
    private String textContent;

    /**
     * 发生时间
     */
    private Integer time;

    /**
     * 发生在伤停补时时间
     */
    private Integer injuryTime;

    /**
     * 是否无效
     */
    private Boolean disabled;

    /**
     * 事件类型
     */
    private Integer type;

    public FbLiveWords() {
	super();
    }

    public Long getContestId() {
	return contestId;
    }

    public void setContestId(Long contestId) {
	this.contestId = contestId;
    }

    public Long getTargetId() {
	return targetId;
    }

    public void setTargetId(Long targetId) {
	this.targetId = targetId;
    }

    public String getTextContent() {
	return textContent;
    }

    public void setTextContent(String textContent) {
	this.textContent = textContent;
    }

    public Integer getTime() {
	return time;
    }

    public void setTime(Integer time) {
	this.time = time;
    }

    public Boolean getDisabled() {
	return disabled;
    }

    public void setDisabled(Boolean disabled) {
	this.disabled = disabled;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Integer getInjuryTime() {
	return injuryTime;
    }

    public void setInjuryTime(Integer injuryTime) {
	this.injuryTime = injuryTime;
    }

    public Long getTargetPhraseId() {
	return targetPhraseId;
    }

    public void setTargetPhraseId(Long targetPhraseId) {
	this.targetPhraseId = targetPhraseId;
    }

    public Long getPhraseId() {
	return phraseId;
    }

    public void setPhraseId(Long phraseId) {
	this.phraseId = phraseId;
    }

}
