package com.lifeix.cbs.contest.impl.spark.live;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.contest.bean.bb.BbPlayerStatisticsResponse;
import com.lifeix.cbs.contest.dao.bb.BbPlayerStatisticsDao;
import com.lifeix.cbs.contest.dto.bb.BbPlayerStatistics;
import com.lifeix.cbs.contest.service.spark.live.BbPlayerStatisticsDubbo;

public class BbPlayerStatisticsDubboImpl implements BbPlayerStatisticsDubbo {

    @Autowired
    private BbPlayerStatisticsDao bbPlayerStatisticsDao;

    /**
     * 批量保存技术统计
     */
    @Override
    public boolean saveStatistics(List<BbPlayerStatisticsResponse> respList) {
	List<BbPlayerStatistics> list = new ArrayList<BbPlayerStatistics>(respList.size());
	for (BbPlayerStatisticsResponse resp : respList) {
	    BbPlayerStatistics statistics = new BbPlayerStatistics();
	    statistics.setAssists(resp.getAssists());
	    statistics.setBlocks(resp.getBlocks());
	    statistics.setContestId(resp.getContest_id());
	    statistics.setDefensiveRebounds(resp.getDefensive_rebounds());
	    statistics.setFreeThrowAtt(resp.getFree_throw_att());
	    statistics.setFreeThrowMade(resp.getFree_throw_made());
	    statistics.setOffensiveRebounds(resp.getOffensive_rebounds());
	    statistics.setPersonalFouls(resp.getPersonal_fouls());
	    statistics.setpId(resp.getP_id());
	    statistics.setPlayerId(resp.getPlayer_id());
	    statistics.setPlayTime(resp.getPlay_time());
	    statistics.setPoints(resp.getPoints());
	    statistics.setSteals(resp.getSteals());
	    statistics.setTeam(resp.getTeam());
	    statistics.setTeamId(resp.getTeam_id());
	    statistics.setTechFouls(resp.getTech_fouls());
	    statistics.setThreePointAtt(resp.getThree_point_att());
	    statistics.setThreePointMade(resp.getThree_point_made());
	    statistics.setTurnovers(resp.getTurnovers());
	    statistics.setTwoPointAtt(resp.getTwo_point_att());
	    statistics.setTwoPointMade(resp.getTwo_point_made());
	    statistics.setDisabled(resp.getDisabled());
	    list.add(statistics);
	}
	return bbPlayerStatisticsDao.saveStatistics(list);
    }

}
