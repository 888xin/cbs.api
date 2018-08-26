package com.lifeix.cbs.contest.bean.bb;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.contest.bean.bb.ext.BbPlayerResponse;
import com.lifeix.user.beans.Response;

public class BbChangeDataResponse implements JsonSerializer<BbChangeDataResponse>, Response {

    /**
     * 
     */
    private static final long serialVersionUID = 3541050091919538996L;

   /**
    *  主场球队技术统计（包括每一节的比分）
    */
    private BbTeamStatisticsResponse h_team_statistics;
    /**
     *  客场球队技术统计（包括每一节的比分）
     */
    private BbTeamStatisticsResponse a_team_statistics;
    /**
     * 主场球员技术统计
     */
    private List<BbPlayerStatisticsResponse>  h_player_statistics;
    /**
     * 客场球员技术统计
     */
    private List<BbPlayerStatisticsResponse>  a_player_statistics;
    
    /**
     * 主队阵容
     */
    private List<BbPlayerResponse> home_lineups;

    /**
     * 客队阵容
     */
    private List<BbPlayerResponse> away_lineups;
    
    public List<BbPlayerStatisticsResponse> getH_player_statistics() {
        return h_player_statistics;
    }

    public void setH_player_statistics(List<BbPlayerStatisticsResponse> h_player_statistics) {
        this.h_player_statistics = h_player_statistics;
    }

    public List<BbPlayerStatisticsResponse> getA_player_statistics() {
        return a_player_statistics;
    }

    public void setA_player_statistics(List<BbPlayerStatisticsResponse> a_player_statistics) {
        this.a_player_statistics = a_player_statistics;
    }

    public BbTeamStatisticsResponse getH_team_statistics() {
        return h_team_statistics;
    }

    public void setH_team_statistics(BbTeamStatisticsResponse h_team_statistics) {
        this.h_team_statistics = h_team_statistics;
    }

    public BbTeamStatisticsResponse getA_team_statistics() {
        return a_team_statistics;
    }

    public void setA_team_statistics(BbTeamStatisticsResponse a_team_statistics) {
        this.a_team_statistics = a_team_statistics;
    }

    public List<BbPlayerResponse> getHome_lineups() {
        return home_lineups;
    }

    public void setHome_lineups(List<BbPlayerResponse> home_lineups) {
        this.home_lineups = home_lineups;
    }

    public List<BbPlayerResponse> getAway_lineups() {
        return away_lineups;
    }

    public void setAway_lineups(List<BbPlayerResponse> away_lineups) {
        this.away_lineups = away_lineups;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(BbChangeDataResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
