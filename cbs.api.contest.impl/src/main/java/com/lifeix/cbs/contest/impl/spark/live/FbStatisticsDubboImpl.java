package com.lifeix.cbs.contest.impl.spark.live;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.api.common.util.ContestConstants.TeamDiffer;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.fb.FbStatisticsResponse;
import com.lifeix.cbs.contest.bean.fb.ext.StatisticsResponse;
import com.lifeix.cbs.contest.dao.fb.FbStatisticsDao;
import com.lifeix.cbs.contest.dto.fb.FbStatistics;
import com.lifeix.cbs.contest.service.spark.live.FbStatisticsDubbo;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.exception.service.L99IllegalParamsException;

public class FbStatisticsDubboImpl implements FbStatisticsDubbo {

    @Autowired
    private FbStatisticsDao fbStatisticsDao;

    /**
     * 批量保存技术统计
     */
    @Override
    public boolean saveStatistics(List<StatisticsResponse> respList) {
	List<FbStatistics> list = new ArrayList<FbStatistics>(respList.size());
	for (StatisticsResponse resp : respList) {
	    FbStatistics statistics = new FbStatistics();
	    statistics.setBallPossession(resp.getBall_possession());
	    statistics.setContestId(resp.getContest_Id());
	    statistics.setCornerKicks(resp.getCorner_kicks());
	    statistics.setFouls(resp.getFouls());
	    statistics.setFreeKicks(resp.getFree_kicks());
	    statistics.setGoalkeeperSaves(resp.getGoalkeeper_saves());
	    statistics.setGoalKicks(resp.getGoal_kicks());
	    statistics.setOffsides(resp.getOffsides());
	    statistics.setShotsOffGoal(resp.getShots_off_goal());
	    statistics.setShotsOnGoal(resp.getShots_on_goal());
	    statistics.setsId(resp.getS_id());
	    statistics.setTargetId(resp.getTarget_id());
	    statistics.setTeam(resp.getTeam());
	    statistics.setTeamId(resp.getT_id());
	    statistics.setThrowIns(resp.getThrow_ins());
	    list.add(statistics);
	}
	return fbStatisticsDao.saveStatistics(list);
    }

    /**
     * 查找赛事技术统计
     */
    @Override
    public FbStatisticsResponse findStatisticsByContestId(Long contestId) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(contestId);
	FbStatisticsResponse resp = new FbStatisticsResponse();
	List<FbStatistics> list = fbStatisticsDao.selectByContestId(contestId);
	if (list == null || list.size() == 0)
	    return resp;
	for (FbStatistics statistics : list) {
	    if (statistics.getTeam() == TeamDiffer.HOME) {
		resp.setH_statistics(ContestTransformUtil.transformStatistics(statistics));
	    } else if (statistics.getTeam() == TeamDiffer.AWAY) {
		resp.setA_statistics(ContestTransformUtil.transformStatistics(statistics));
	    }
	}
	return resp;
    }

}
