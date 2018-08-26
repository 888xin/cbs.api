package com.lifeix.cbs.contest.impl.rollback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.contest.dao.bb.BbBetJcDao;
import com.lifeix.cbs.contest.dao.bb.BbBetOpDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsJcDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsOpDao;
import com.lifeix.cbs.contest.dao.contest.RollbackDao;
import com.lifeix.cbs.contest.dao.fb.FbBetJcDao;
import com.lifeix.cbs.contest.dao.fb.FbBetOpDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsJcDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsOpDao;
import com.lifeix.cbs.contest.dao.settle.UserSettleLogDao;
import com.lifeix.cbs.contest.dto.bet.BetJc;
import com.lifeix.cbs.contest.dto.bet.BetOp;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.cirle.FriendCircleService;
import com.lifeix.cbs.contest.service.rollback.ContestRollbackService;
import com.lifeix.cbs.contest.util.ContestRollbackUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 16-3-17 上午11:21
 * 
 * @Description
 */
@Service("contestRollbackService")
public class ContestRollbackServiceImpl extends ImplSupport implements ContestRollbackService {

    protected static Logger LOG = LoggerFactory.getLogger(ContestRollbackServiceImpl.class);

    @Autowired
    private FriendCircleService friendCircleService;

    @Autowired
    private UserSettleLogDao userSettleLogDao;

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private FbOddsOpDao fbOpDao;

    @Autowired
    private FbOddsJcDao fbJcDao;

    @Autowired
    private FbBetOpDao fbBetOpDao;

    @Autowired
    private FbBetJcDao fbBetJcDao;

    @Autowired
    private RollbackDao rollbackDao;

    @Autowired
    private CouponUserService couponUserService;

    @Autowired
    private BbOddsOpDao bbOpDao;

    @Autowired
    private BbOddsJcDao bbJcDao;

    @Autowired
    private BbBetOpDao bbBetOpDao;

    @Autowired
    private BbBetJcDao bbBetJcDao;

    @Override
    @Transactional(rollbackFor = L99IllegalDataException.class)
    public void rollback(final Long contestId, Integer contestType) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException {

	ParamemeterAssert.assertDataNotNull(contestId);
	// 1.修改contest表 status=0 AND settle=0 AND homeScores=0 AND awayScores=0
	// boolean flag1 = fbContestDao.rollback(contestId);

	// 2.odds_op和odds_jc表的closeflag设为false
	boolean flag1;
	boolean flag2;

	if (contestType == ContestConstants.ContestType.FOOTBALL) {
	    // 足球
	    flag1 = fbOpDao.openOdds(contestId);
	    flag2 = fbJcDao.openOdds(contestId);
	    if (!flag1 && !flag2) {
		throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_BASIC_SERVCER, MsgCode.BasicMsg.KEY_BASIC_SERVCER);
	    }

	    // 3.统计op和jc下单数量
	    new FbThead(contestId).start();

	} else if (contestType == ContestConstants.ContestType.BASKETBALL) {
	    flag1 = bbOpDao.openOdds(contestId);
	    flag2 = bbJcDao.openOdds(contestId);
	    if (!flag1 && !flag2) {
		throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_BASIC_SERVCER, MsgCode.BasicMsg.KEY_BASIC_SERVCER);
	    }

	    // 3.统计op和jc下单数量
	    new BbThead(contestId).start();

	}
    }

    /**
     * 足球线程进行操作
     */
    class FbThead extends Thread {
	Long contestId;

	public FbThead(Long contestId) {
	    this.contestId = contestId;
	}

	@Override
	public void run() {
	    int opNum = 0;
	    int jcNum = 0;
	    double[] resultDouble = { 0, 0, 0 };
	    int limit = 100;

	    try {

		Long opStartId = null;
		int size = limit;
		List<BetOp> opList;
		List<BetJc> jcList;
		while (size == limit) {
		    opList = fbBetOpDao.findFbBetOpByContestId(contestId, opStartId, limit);
		    size = opList.size();
		    if (size == 0) {
			break;
		    }
		    opStartId = opList.get(size - 1).getbId();
		    opNum += size;
		    double[] opResult = ContestRollbackUtil.betOpRollback(opList, couponUserService, moneyService);
		    for (int i = 0; i < opResult.length; i++) {
			resultDouble[i] = CommerceMath.add(resultDouble[i], opResult[i]);
		    }
		}
		size = limit;
		Long jcStartId = null;
		while (size == limit) {
		    jcList = fbBetJcDao.findFbBetJcByContestId(contestId, jcStartId, limit);
		    size = jcList.size();
		    if (size == 0) {
			break;
		    }
		    jcStartId = jcList.get(size - 1).getbId();
		    jcNum += size;
		    double[] jcResult = ContestRollbackUtil.betJcRollback(jcList, couponUserService, moneyService);
		    for (int i = 0; i < jcResult.length; i++) {
			resultDouble[i] = CommerceMath.add(resultDouble[i], jcResult[i]);
		    }
		}

		// 5.朋友圈软删除
		friendCircleService.deleteByContestId(ContestConstants.ContestType.FOOTBALL, contestId);

	    } catch (L99IllegalParamsException e) {
		LOG.error(e.getMessage(), e);
	    } catch (L99IllegalDataException e) {
		LOG.error(e.getMessage(), e);
	    } catch (JSONException e) {
		LOG.error(e.getMessage(), e);
	    }

	    // 4.删除相关日志记录
	    int numLog = userSettleLogDao.deleteByContestId(ContestConstants.ContestType.FOOTBALL, contestId);

	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("赛事类型", "足球");
	    map.put("赛事ID", contestId);
	    map.put("胜平负下单数量", opNum);
	    map.put("让球胜平负下单数量", jcNum);
	    map.put("删除掉的结算日志条数", numLog);
	    map.put("加回的龙币数额", resultDouble[0]);
	    map.put("扣掉的龙币数额", resultDouble[1]);
	    map.put("加回的龙筹数额", resultDouble[2]);
	    String jsonStr = JSONArray.toJSONString(map);
	    rollbackDao.insert(jsonStr);

	}
    }

    /**
     * 篮球线程进行操作
     */
    class BbThead extends Thread {
	Long contestId;

	public BbThead(Long contestId) {
	    this.contestId = contestId;
	}

	@Override
	public void run() {
	    int opNum = 0;
	    int jcNum = 0;
	    double[] resultDouble = { 0, 0, 0 };
	    int limit = 100;

	    try {

		Long opStartId = null;
		int size = limit;
		List<BetOp> opList;
		List<BetJc> jcList;
		while (size == limit) {
		    opList = bbBetOpDao.findBbBetOpByContestId(contestId, opStartId, limit);
		    size = opList.size();
		    if (size == 0) {
			break;
		    }
		    opStartId = opList.get(size - 1).getbId();
		    opNum += size;
		    double[] opResult = ContestRollbackUtil.betOpRollback(opList, couponUserService, moneyService);
		    for (int i = 0; i < opResult.length; i++) {
			resultDouble[i] = CommerceMath.add(resultDouble[i], opResult[i]);
		    }
		}
		size = limit;
		Long jcStartId = null;
		while (size == limit) {
		    jcList = bbBetJcDao.findBbBetJcByContestId(contestId, jcStartId, limit);
		    size = jcList.size();
		    if (size == 0) {
			break;
		    }
		    jcStartId = jcList.get(size - 1).getbId();
		    jcNum += size;
		    double[] jcResult = ContestRollbackUtil.betJcRollback(jcList, couponUserService, moneyService);
		    for (int i = 0; i < jcResult.length; i++) {
			resultDouble[i] = CommerceMath.add(resultDouble[i], jcResult[i]);
		    }
		}

		// 5.朋友圈软删除
		friendCircleService.deleteByContestId(ContestConstants.ContestType.BASKETBALL, contestId);

	    } catch (L99IllegalParamsException e) {
		LOG.error(e.getMessage(), e);
	    } catch (L99IllegalDataException e) {
		LOG.error(e.getMessage(), e);
	    } catch (JSONException e) {
		LOG.error(e.getMessage(), e);
	    }

	    // 4.删除相关日志记录
	    int numLog = userSettleLogDao.deleteByContestId(ContestConstants.ContestType.BASKETBALL, contestId);

	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("赛事类型", "篮球");
	    map.put("赛事ID", contestId);
	    map.put("胜平负下单数量", opNum);
	    map.put("让球胜平负下单数量", jcNum);
	    map.put("删除掉的结算日志条数", numLog);
	    map.put("加回的龙币数额", resultDouble[0]);
	    map.put("扣掉的龙币数额", resultDouble[1]);
	    map.put("加回的龙筹数额", resultDouble[2]);
	    String jsonStr = JSONArray.toJSONString(map);
	    rollbackDao.insert(jsonStr);

	}
    }

}
