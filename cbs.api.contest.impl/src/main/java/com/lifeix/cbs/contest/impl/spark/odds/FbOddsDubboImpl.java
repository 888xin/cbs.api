package com.lifeix.cbs.contest.impl.spark.odds;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.api.common.util.PlayType;
import com.lifeix.cbs.contest.bean.odds.OddsJcResponse;
import com.lifeix.cbs.contest.bean.odds.OddsOpResponse;
import com.lifeix.cbs.contest.bean.odds.OddsSizeResponse;
import com.lifeix.cbs.contest.dao.fb.FbContestDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsJcDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsOpDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsSizeDao;
import com.lifeix.cbs.contest.dao.fb.OddsWarnDao;
import com.lifeix.cbs.contest.dto.fb.FbContest;
import com.lifeix.cbs.contest.dto.odds.OddsJc;
import com.lifeix.cbs.contest.dto.odds.OddsOp;
import com.lifeix.cbs.contest.dto.odds.OddsSize;
import com.lifeix.cbs.contest.dto.odds.OddsWarn;
import com.lifeix.cbs.contest.service.spark.odds.FbOddsDubbo;
import com.lifeix.cbs.contest.util.ContestFilterUtil;
import com.lifeix.cbs.contest.util.transform.OddsTransformUtil;

public class FbOddsDubboImpl implements FbOddsDubbo {

    @Autowired
    private FbOddsOpDao fbOpDao;

    @Autowired
    private FbOddsJcDao fbJcDao;

    @Autowired
    private FbOddsSizeDao fbSizeDao;

    @Autowired
    private OddsWarnDao oddsWarnDao;

    @Autowired
    private FbContestDao fbContestDao;

    /**
     * 批量保存欧赔赔率
     * 
     * @param list
     * @return
     */
    @Override
    public boolean saveOpOdds(List<OddsOpResponse> list) {
	List<OddsOp> oddsList = new ArrayList<OddsOp>(list.size());
	for (OddsOpResponse resp : list) {
	    OddsOp odds = new OddsOp();
	    odds.setAwayRoi(resp.getAway_roi());
	    odds.setBelongFive(resp.getBelong_five());
	    odds.setCloseFlag(resp.getClose_flag());
	    odds.setCompany(resp.getCompany());
	    odds.setContestId(resp.getContest_id());
	    odds.setDrawRoi(resp.getDraw_roi());
	    odds.setHomeRoi(resp.getHome_roi());
	    odds.setInitAwayRoi(resp.getInit_away_roi());
	    odds.setInitDrawRoi(resp.getInit_draw_roi());
	    odds.setInitHomeRoi(resp.getInit_home_roi());
	    odds.setLockFlag(resp.getLock_flag());
	    oddsList.add(odds);
	}
	return fbOpDao.saveOdds(oddsList);
    }

    /**
     * 批量保存竞彩赔率
     */
    @Override
    public boolean saveJcOdds(List<OddsJcResponse> list) {
	List<OddsJc> oddsList = new ArrayList<OddsJc>(list.size());
	for (OddsJcResponse resp : list) {
	    OddsJc odds = new OddsJc();
	    odds.setAwayRoi(resp.getAway_roi());
	    odds.setBelongFive(resp.getBelong_five());
	    odds.setCloseFlag(resp.getClose_flag());
	    odds.setCompany(resp.getCompany());
	    odds.setContestId(resp.getContest_id());
	    odds.setDrawRoi(resp.getDraw_roi());
	    odds.setHandicap(resp.getHandicap());
	    odds.setHomeRoi(resp.getHome_roi());
	    odds.setInitAwayRoi(resp.getInit_away_roi());
	    odds.setInitDrawRoi(resp.getInit_draw_roi());
	    odds.setInitHandicap(resp.getInit_handicap());
	    odds.setInitHomeRoi(resp.getInit_home_roi());
	    odds.setLockFlag(resp.getLock_flag());
	    oddsList.add(odds);
	}
	return fbJcDao.saveOdds(oddsList);
    }

    /**
     * 批量保存大小球赔率
     */
    @Override
    public boolean saveSizeOdds(List<OddsSizeResponse> list) {
	List<OddsSize> oddsList = new ArrayList<OddsSize>(list.size());
	for (OddsSizeResponse resp : list) {
	    OddsSize odds = new OddsSize();
	    odds.setBelongFive(resp.getBelong_five());
	    odds.setBigRoi(resp.getBig_roi());
	    odds.setCloseFlag(resp.getClose_flag());
	    odds.setCompany(resp.getCompany());
	    odds.setContestId(resp.getContest_id());
	    odds.setHandicap(resp.getHandicap());
	    odds.setInitBigRoi(resp.getInit_big_roi());
	    odds.setInitHandicap(resp.getInit_handicap());
	    odds.setInitTinyRoi(resp.getInit_tiny_roi());
	    odds.setLockFlag(resp.getLock_flag());
	    odds.setTinyRoi(resp.getTiny_roi());
	    oddsList.add(odds);
	}
	return fbSizeDao.saveOdds(oddsList);
    }

    @Override
    public boolean closeOpOdds(Long contestId) {
	return fbOpDao.closeOdds(contestId);
    }

    @Override
    public boolean closeJcOdds(Long contestId) {
	return fbJcDao.closeOdds(contestId);
    }

    @Override
    public boolean closeSizeOdds(Long contestId) {
	return fbSizeDao.closeOdds(contestId);
    }

    @Override
    public Map<Long, OddsJcResponse> findJcOddsByContestIds(List<Long> contestIds) {
	Map<Long, OddsJcResponse> respMap = new HashMap<Long, OddsJcResponse>();
	Map<Long, OddsJc> jcMap = fbJcDao.findOddsJcMapByIds(contestIds);
	if (jcMap != null && jcMap.size() > 0) {
	    Set<Long> keySet = jcMap.keySet();
	    for (Long contestId : keySet) {
		OddsJc jc = jcMap.get(contestId);
		OddsJcResponse resp = OddsTransformUtil.transformOddsJc(jc);
		if (resp != null) {
		    respMap.put(contestId, resp);
		}
	    }
	}
	return respMap;
    }

    @Override
    public Map<Long, OddsSizeResponse> findSizeOddsByContestIds(List<Long> contestIds) {
	Map<Long, OddsSizeResponse> respMap = new HashMap<Long, OddsSizeResponse>();
	Map<Long, OddsSize> sizeMap = fbSizeDao.findOddsSizeMapByIds(contestIds);
	if (sizeMap != null && sizeMap.size() > 0) {
	    Set<Long> keySet = sizeMap.keySet();
	    for (Long contestId : keySet) {
		OddsSize size = sizeMap.get(contestId);
		OddsSizeResponse resp = OddsTransformUtil.transformOddsSize(size);
		if (resp != null) {
		    respMap.put(contestId, resp);
		}
	    }
	}
	return respMap;
    }

    /**
     * 检查最近五天包含胜平负和让球盘的赔率
     */
    @Override
    public int checkWarnOdds(Long contestId) {

	List<FbContest> contests = null;
	if (contestId != null) {
	    FbContest fbContest = fbContestDao.selectById(contestId);
	    if (fbContest == null || (fbContest.getOddsType() & 3) != 3) {
		return 0;
	    }
	    contests = new ArrayList<>();
	    contests.add(fbContest);
	} else {
	    contests = fbContestDao.findFbContestsByCheck();
	}

	int warnData = 0;
	for (FbContest fbContest : contests) {

	    // 获取胜平负赔率
	    OddsOp oddsOp = fbOpDao.selectByContest(fbContest.getContestId());

	    // 获取让球胜平负赔率
	    OddsJc oddsJc = fbJcDao.selectByContest(fbContest.getContestId());

	    // 两种赔率缺少一种是不能比较的
	    if (oddsOp == null || oddsJc == null) {
		continue;
	    }

	    int warnType = ContestFilterUtil.checkOdds(oddsOp, oddsJc);
	    boolean insertFlag = false;
	    OddsWarn oddsWarn = null;
	    if (warnType == 1) { // 让球胜平负赔率异常
		oddsWarn = oddsWarnDao.selectByOddsId(PlayType.FB_RQSPF.getId(), oddsJc.getOddsId());
		if (oddsWarn == null) {
		    oddsWarn = new OddsWarn();
		    insertFlag = true;
		}
		oddsWarn.setContestId(oddsJc.getContestId());
		oddsWarn.setPlayType(PlayType.FB_RQSPF.getId());
		oddsWarn.setOddsId(oddsJc.getOddsId());
		oddsWarn.setHandicap(oddsJc.getHandicap());
		oddsWarn.setHomeRoi(oddsJc.getHomeRoi());
		oddsWarn.setDrawRoi(oddsJc.getDrawRoi());
		oddsWarn.setAwayRoi(oddsJc.getAwayRoi());
		oddsWarn.setInitHandicap(oddsJc.getInitHandicap());
		oddsWarn.setInitHomeRoi(oddsJc.getInitHomeRoi());
		oddsWarn.setInitDrawRoi(oddsJc.getInitDrawRoi());
		oddsWarn.setInitAwayRoi(oddsJc.getInitAwayRoi());
		oddsWarn.setCompany(oddsJc.getCompany());
		oddsWarn.setStatus(0);
		oddsWarn.setCreateTime(new Date());
	    } else if (warnType == 2) { // 胜平负赔率异常
		oddsWarn = oddsWarnDao.selectByOddsId(PlayType.FB_SPF.getId(), oddsJc.getOddsId());
		if (oddsWarn == null) {
		    oddsWarn = new OddsWarn();
		    insertFlag = true;
		}
		oddsWarn.setContestId(oddsJc.getContestId());
		oddsWarn.setPlayType(PlayType.FB_SPF.getId());
		oddsWarn.setOddsId(oddsJc.getOddsId());
		oddsWarn.setHandicap(oddsJc.getHandicap());
		oddsWarn.setHomeRoi(oddsJc.getHomeRoi());
		oddsWarn.setDrawRoi(oddsJc.getDrawRoi());
		oddsWarn.setAwayRoi(oddsJc.getAwayRoi());
		oddsWarn.setInitHandicap(oddsJc.getInitHandicap());
		oddsWarn.setInitHomeRoi(oddsJc.getInitHomeRoi());
		oddsWarn.setInitDrawRoi(oddsJc.getInitDrawRoi());
		oddsWarn.setInitAwayRoi(oddsJc.getInitAwayRoi());
		oddsWarn.setCompany(oddsJc.getCompany());
		oddsWarn.setStatus(0);
		oddsWarn.setCreateTime(new Date());
	    }

	    if (oddsWarn != null) {
		warnData++;
		if (insertFlag) {
		    oddsWarnDao.insert(oddsWarn);
		} else {
		    oddsWarnDao.update(oddsWarn);
		}
	    }
	}
	return warnData;
    }
}
