package com.lifeix.cbs.contest.impl.contest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.ContestMsg;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.contest.ContestAdListResponse;
import com.lifeix.cbs.contest.bean.contest.ContestAdResponse;
import com.lifeix.cbs.contest.dao.bb.BbContestDao;
import com.lifeix.cbs.contest.dao.contest.ContestAdDao;
import com.lifeix.cbs.contest.dao.fb.FbContestDao;
import com.lifeix.cbs.contest.dao.yy.YyContestDao;
import com.lifeix.cbs.contest.dto.bb.BbContest;
import com.lifeix.cbs.contest.dto.contest.ContestAd;
import com.lifeix.cbs.contest.dto.fb.FbContest;
import com.lifeix.cbs.contest.dto.yy.YyContest;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.contest.ContestAdService;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

@Service("contestAdService")
public class ContestAdServiceImpl extends ImplSupport implements ContestAdService {

    @Autowired
    private ContestAdDao contestAdDao;

    @Autowired
    private YyContestDao yyContestDao;

    @Autowired
    private FbContestDao fbContestDao;

    @Autowired
    private BbContestDao bbContestDao;

    @Override
    public ContestAdResponse viewAdContest(Long id) throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(id);

	ContestAd bean = contestAdDao.selectById(id);

	if (bean == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	}

	return ContestTransformUtil.transformContestAd(bean);
    }

    private void existContest(Long contestId, Integer contestType) throws L99IllegalParamsException, L99IllegalDataException {
	// 校验赛事是否存在
	if (contestType == ContestType.YAYA) {
	    YyContest contest = yyContestDao.selectById(contestId);
	    if (contest == null) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	}

	if (contestType == ContestType.FOOTBALL) {
	    FbContest contest = fbContestDao.selectById(contestId);
	    if (contest == null) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	}

	if (contestType == ContestType.BASKETBALL) {
	    BbContest contest = bbContestDao.selectById(contestId);
	    if (contest == null) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	}
    }

    @Override
    public void insertContestAd(Long contestId, Integer contestType, String title, String images, String text)
	    throws L99IllegalParamsException, L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(contestId, contestType, title, images, text);
	// 校验赛事是否存在
	existContest(contestId, contestType);
	// 插入赛事
	ContestAd bean = new ContestAd();
	bean.setTitle(title);
	bean.setImages(images);
	bean.setText(text);
	bean.setContestId(contestId);
	bean.setContestType(contestType);
	bean.setCreateTime(new Date());
	bean.setUpdateTime(new Date());
	bean.setHideFlag(true);
	boolean flag = contestAdDao.insert(bean);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}

    }

    @Override
    public void updateContestAd(Long id, Long contestId, Integer contestType, String title, String images, String text)
	    throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(id);

	// 查询赛事
	ContestAd bean = contestAdDao.selectById(id);
	if (bean == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	boolean flag;
	ParamemeterAssert.assertDataNotNull(contestId, contestType, title, text, images);

	// 校验赛事是否存在
	existContest(contestId, contestType);

	// 修改赛事
	bean.setTitle(title);
	bean.setImages(images);
	bean.setText(text);
	bean.setContestId(contestId);
	bean.setContestType(contestType);
	flag = contestAdDao.update(bean);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    @Override
    public void updateHide(Long id, boolean hideFlag) throws L99IllegalParamsException, L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(id, hideFlag);
	boolean flag;
	flag = contestAdDao.updateHide(id, hideFlag);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    @Override
    public ContestAdListResponse findContestsAdInner(Integer contestType, Boolean hideFlag, Long startId, int limit)
	    throws L99IllegalParamsException {

	List<ContestAd> list = contestAdDao.findContestAds(contestType, hideFlag, startId, limit);

	List<ContestAdResponse> ads = new ArrayList<ContestAdResponse>();
	for (ContestAd bean : list) {
	    ads.add(ContestTransformUtil.transformContestAd(bean));

	}

	int size = ads.size();
	if (size > 0) {
	    startId = list.get(size - 1).getId();
	}

	ContestAdListResponse reponse = new ContestAdListResponse();
	reponse.setAds(ads);
	reponse.setNumber(size);
	reponse.setStartId(startId);
	return reponse;
    }

    @Override
    public ContestAdListResponse findContestsAd(Integer contestType, int limit) throws L99IllegalParamsException {
	ContestAdListResponse reponse = new ContestAdListResponse();
	List<ContestAd> list = contestAdDao.findContestAds(contestType, true, null, limit);

	if (list.size() < limit) {
	    return reponse;
	}

	List<ContestAdResponse> ads = new ArrayList<ContestAdResponse>();
	for (ContestAd bean : list) {
	    ads.add(ContestTransformUtil.transformContestAd(bean));
	}

	reponse.setAds(ads);
	reponse.setNumber(limit);
	return reponse;
    }

}
