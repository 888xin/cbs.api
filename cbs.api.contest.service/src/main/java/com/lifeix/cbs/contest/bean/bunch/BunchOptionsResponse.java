package com.lifeix.cbs.contest.bean.bunch;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.odds.OddsDssResponse;
import com.lifeix.cbs.contest.bean.odds.OddsJcResponse;
import com.lifeix.cbs.contest.bean.odds.OddsOpResponse;
import com.lifeix.cbs.contest.bean.odds.OddsSizeResponse;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;

/**
 * Created by lhx on 16-5-17 上午9:32
 *
 * @Description 串选项
 */
public class BunchOptionsResponse implements JsonSerializer<BunchOptionsResponse>, Response {

    private static final long serialVersionUID = -9129411162034950163L;
    /**
     * 序号
     */
    private Integer index ;
    /**
     * 赔率
     */
    private OddsOpResponse oddsOpResponse ;
    private OddsJcResponse oddsJcResponse ;
    private OddsSizeResponse oddsSizeResponse ;
    private OddsDssResponse oddsDssResponse ;
    private ContestResponse contestResponse;
    /**
     * 赛事ID
     */
    private Long contest_id ;

    /**
     * 赛事类型
     */
    private Integer contest_type ;

    /**
     * 赢方
     */
    private Integer win ;
    /**
     * 玩法
     */
    private Integer play_type ;

    /**
     * 支持方
     */
    private Integer support ;

    /**
     * 盘口
     */
    private Double handicap;

    public Double getHandicap() {
        return handicap;
    }

    public void setHandicap(Double handicap) {
        this.handicap = handicap;
    }

    public Integer getSupport() {
        return support;
    }

    public void setSupport(Integer support) {
        this.support = support;
    }

    public ContestResponse getContestResponse() {
        return contestResponse;
    }

    public void setContestResponse(ContestResponse contestResponse) {
        this.contestResponse = contestResponse;
    }

    public OddsOpResponse getOddsOpResponse() {
        return oddsOpResponse;
    }

    public void setOddsOpResponse(OddsOpResponse oddsOpResponse) {
        this.oddsOpResponse = oddsOpResponse;
    }

    public OddsJcResponse getOddsJcResponse() {
        return oddsJcResponse;
    }

    public void setOddsJcResponse(OddsJcResponse oddsJcResponse) {
        this.oddsJcResponse = oddsJcResponse;
    }

    public OddsSizeResponse getOddsSizeResponse() {
        return oddsSizeResponse;
    }

    public void setOddsSizeResponse(OddsSizeResponse oddsSizeResponse) {
        this.oddsSizeResponse = oddsSizeResponse;
    }

    public OddsDssResponse getOddsDssResponse() {
        return oddsDssResponse;
    }

    public void setOddsDssResponse(OddsDssResponse oddsDssResponse) {
        this.oddsDssResponse = oddsDssResponse;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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

    public Integer getWin() {
        return win;
    }

    public void setWin(Integer win) {
        this.win = win;
    }

    public Integer getPlay_type() {
        return play_type;
    }

    public void setPlay_type(Integer play_type) {
        this.play_type = play_type;
    }

    @Override
    public JsonElement serialize(BunchOptionsResponse bunchOptionsResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
