package com.lifeix.cbs.contest.dto.contest;

import java.util.Date;

/**
 * Created by lhx on 2016/7/28 16:37
 *
 * @Description
 */
public class Contest {

    /**
     * 主键
     */
    private Long contestId;

    /**
     * 主场球队id
     */
    private Long homeTeam;

    /**
     * 客场球队id
     */
    private Long awayTeam;

    /**
     * 开始时间
     */
    private Date startTime;

    public Long getContestId() {
        return contestId;
    }

    public void setContestId(Long contestId) {
        this.contestId = contestId;
    }

    public Long getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Long homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Long getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Long awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
