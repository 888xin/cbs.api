package com.lifeix.cbs.contest.impl.live;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.util.ContestConstants.TeamDiffer;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.fb.FbStatisticsResponse;
import com.lifeix.cbs.contest.dao.fb.FbStatisticsDao;
import com.lifeix.cbs.contest.dto.fb.FbStatistics;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.live.FbStatisticsService;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.exception.service.L99IllegalParamsException;
@Service("fbStatisticsService")
public class FbStatisticsServiceImpl extends ImplSupport implements FbStatisticsService {

    @Autowired
    private FbStatisticsDao fbStatisticsDao;
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
