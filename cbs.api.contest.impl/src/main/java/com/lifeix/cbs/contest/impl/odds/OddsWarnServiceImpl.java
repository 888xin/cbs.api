/**
 * 
 */
package com.lifeix.cbs.contest.impl.odds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode.ContestMsg;
import com.lifeix.cbs.api.common.util.PlayType;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.odds.OddsWarnListResponse;
import com.lifeix.cbs.contest.bean.odds.OddsWarnResponse;
import com.lifeix.cbs.contest.dao.bb.BbContestDao;
import com.lifeix.cbs.contest.dao.bb.BbTeamDao;
import com.lifeix.cbs.contest.dao.fb.FbContestDao;
import com.lifeix.cbs.contest.dao.fb.FbTeamDao;
import com.lifeix.cbs.contest.dao.fb.OddsWarnDao;
import com.lifeix.cbs.contest.dto.bb.BbContest;
import com.lifeix.cbs.contest.dto.bb.BbTeam;
import com.lifeix.cbs.contest.dto.fb.FbContest;
import com.lifeix.cbs.contest.dto.fb.FbTeam;
import com.lifeix.cbs.contest.dto.odds.OddsWarn;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.odds.OddsWarnService;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.cbs.contest.util.transform.OddsTransformUtil;
import com.lifeix.exception.service.L99IllegalDataException;

/**
 * @author lifeix
 *
 */
@Service("oddsWarnService")
public class OddsWarnServiceImpl extends ImplSupport implements OddsWarnService {

    @Autowired
    private BbContestDao bbContestDao;

    @Autowired
    private FbContestDao fbContestDao;

    @Autowired
    private FbTeamDao fbTeamDao;

    @Autowired
    private BbTeamDao bbTeamDao;

    @Autowired
    private OddsWarnDao oddsWarnDao;

    /**
     * 获取异常赔率列表
     * 
     * @param status
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalDataException
     */
    @Override
    public OddsWarnListResponse getOddsWarnList(Integer status, Long startId, int limit) throws L99IllegalDataException {
	OddsWarnListResponse oddsWarnListResponse = new OddsWarnListResponse();
	List<OddsWarnResponse> oddsWarnResponseList = new ArrayList<OddsWarnResponse>();
	List<OddsWarn> oddsWarnList = oddsWarnDao.selectByStatus(status, startId, limit);
	int size = oddsWarnList.size();
	if (size > 0) {
	    for (int i = 0; i < size; i++) {
		OddsWarn oddsWarn = oddsWarnList.get(i);
		OddsWarnResponse oddsWarnResponse = OddsTransformUtil.transformOddsWarn(oddsWarn);
		Integer playType = oddsWarnResponse.getPlayType();
		if (PlayType.FB_SPF.getId() == playType || PlayType.FB_RQSPF.getId() == playType) {
		    FbContest contest = fbContestDao.selectById(oddsWarn.getContestId());
		    if (contest == null) {
			throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST,
			        ContestMsg.KEY_CONTEST_NOT_EXIST);
		    }
		    List<Long> teamIds = new ArrayList<Long>();
		    teamIds.add(contest.getHomeTeam());
		    teamIds.add(contest.getAwayTeam());
		    Map<Long, FbTeam> teams = fbTeamDao.findFbTeamMapByIds(teamIds);
		    int[] betCount = {0,0};
		    ContestResponse contest_res = ContestTransformUtil.transformFbContest(contest,
			    teams.get(contest.getHomeTeam()), teams.get(contest.getAwayTeam()), betCount, false);
		    oddsWarnResponse.setContest(contest_res);
		} else if (PlayType.BB_SPF.getId() == playType || PlayType.BB_JC.getId() == playType) {
		    BbContest contest = bbContestDao.selectById(oddsWarn.getContestId());
		    List<Long> teamIds = new ArrayList<Long>();
		    teamIds.add(contest.getHomeTeam());
		    teamIds.add(contest.getAwayTeam());
		    Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);
		    int[] betCount = {0,0};
		    ContestResponse contest_res = ContestTransformUtil.transformBbContest(contest,
			    teams.get(contest.getHomeTeam()), teams.get(contest.getAwayTeam()), betCount, false);
		    oddsWarnResponse.setContest(contest_res);
		}
		oddsWarnResponseList.add(oddsWarnResponse);
		startId = oddsWarn.getId();
	    }
	} else {
	    startId = -1L;
	}
	oddsWarnListResponse.setOddsWarnList(oddsWarnResponseList);
	oddsWarnListResponse.setStartId(startId);
	oddsWarnListResponse.setNumber(size);
	oddsWarnListResponse.setLimit(limit);
	return oddsWarnListResponse;
    }

    /**
     * 处理异常赔率
     * 
     * @param id
     * @param status
     */
    @Override
    public void editOddsWarn(Long id, Integer status) {

    }

}
