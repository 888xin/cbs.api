package com.lifeix.cbs.contest.impl.spark.live;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.contest.bean.bb.BbTeamStatisticsResponse;
import com.lifeix.cbs.contest.dao.bb.BbTeamStatisticsDao;
import com.lifeix.cbs.contest.dto.bb.BbTeamStatistics;
import com.lifeix.cbs.contest.service.spark.live.BbTeamStatisticsDubbo;

public class BbTeamStatisticsDubboImpl implements BbTeamStatisticsDubbo {

    @Autowired
    private BbTeamStatisticsDao bbTeamStatisticsDao;

    /**
     * 批量保存技术统计
     */
    @Override
    public boolean saveStatistics(List<BbTeamStatisticsResponse> respList) {
	List<BbTeamStatistics> list = new ArrayList<BbTeamStatistics>(respList.size());
	for (BbTeamStatisticsResponse resp : respList) {
	    BbTeamStatistics statistics = new BbTeamStatistics();
	    statistics.setAssists(resp.getAssists());
	    statistics.setBlocks(resp.getBlocks());
	    statistics.setContestId(resp.getContest_id());
	    statistics.setDefensiveRebounds(resp.getDefensive_rebounds());
	    statistics.setFreeThrowAtt(resp.getFree_throw_att());
	    statistics.setFreeThrowMade(resp.getFree_throw_made());
	    statistics.setOffensiveRebounds(resp.getOffensive_rebounds());
	    statistics.setPersonalFouls(resp.getPersonal_fouls());
	    statistics.setScores(resp.getScores());
	    statistics.setsId(resp.getS_id());
	    statistics.setSteals(resp.getSteals());
	    statistics.setTeam(resp.getTeam());
	    statistics.setTeamId(resp.getTeam_id());
	    statistics.setTechFouls(resp.getTech_fouls());
	    statistics.setThreePointAtt(resp.getThree_point_att());
	    statistics.setThreePointMade(resp.getThree_point_made());
	    statistics.setTurnovers(resp.getTurnovers());
	    statistics.setTwoPointAtt(resp.getTwo_point_att());
	    statistics.setTwoPointMade(resp.getTwo_point_made());
	    list.add(statistics);
	}
	return bbTeamStatisticsDao.saveStatistics(list);
    }

}
