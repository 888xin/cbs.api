package com.lifeix.cbs.contest.impl.spark.cup;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.ContestMsg;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestMemcached;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.fb.CupListResponse;
import com.lifeix.cbs.contest.bean.fb.CupResponse;
import com.lifeix.cbs.contest.dao.bb.BbContestDao;
import com.lifeix.cbs.contest.dao.bb.BbCupDao;
import com.lifeix.cbs.contest.dto.bb.BbCup;
import com.lifeix.cbs.contest.service.spark.cup.BbCupDubbo;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.memcache.MemcacheService;

@Service("bbCupDubbo")
public class BbCupDubboImpl implements BbCupDubbo {

    @Autowired
    private BbCupDao bbCupDao;

    @Autowired
    private BbContestDao bbContestDao;

    @Autowired
    protected MemcacheService memcacheService;

    @Override
    public boolean saveCup(List<CupResponse> list) {
	List<BbCup> cupList = new ArrayList<BbCup>(list.size());
	for (CupResponse resp : list) {
	    BbCup cup = new BbCup();
	    cup.setColor(resp.getColor());
	    cup.setCountry(resp.getCountry());
	    cup.setCountryId(resp.getCountry_id());
	    cup.setLevel(resp.getLevel());
	    cup.setName(resp.getName());
	    cup.setNameEN(resp.getName_en());
	    cup.setNameFull(resp.getName_full());
	    cup.setTargetId(resp.getTarget_id());
	    cup.setType(resp.getType());
	    cupList.add(cup);
	}
	return bbCupDao.saveCup(cupList);
    }

    @Override
    public Map<Long, CupResponse> getAllCup() {
	Map<Long, BbCup> cupMap = bbCupDao.getAllCup();
	Map<Long, CupResponse> respMap = new HashMap<Long, CupResponse>(cupMap.size());
	Set<Long> keySet = cupMap.keySet();
	for (Long key : keySet) {
	    BbCup cup = cupMap.get(key);
	    CupResponse resp = ContestTransformUtil.transformBbCup(cup);
	    respMap.put(key, resp);
	}
	return respMap;
    }

    @Override
    public Map<Long, CupResponse> getCupByTargetIds(List<Long> targetIds) {
	Map<Long, BbCup> cupMap = bbCupDao.getCupByTargetIds(targetIds);
	Map<Long, CupResponse> respMap = new HashMap<Long, CupResponse>(cupMap.size());
	Set<Long> keySet = cupMap.keySet();
	for (Long key : keySet) {
	    BbCup cup = cupMap.get(key);
	    CupResponse resp = ContestTransformUtil.transformBbCup(cup);
	    respMap.put(key, resp);
	}
	return respMap;
    }

    @Override
    public CupListResponse getLevelCup(int level) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(level);
	List<BbCup> list = bbCupDao.getLevelCup(level);
	if (list.size() > 0) {
	    CupListResponse cupListResponse = new CupListResponse();
	    List<CupResponse> cupResponseList = new ArrayList<CupResponse>();
	    for (BbCup bbCup : list) {
		CupResponse cupResponse = new CupResponse();
		cupResponse.setTarget_id(bbCup.getTargetId());
		cupResponse.setName(bbCup.getName());
		cupResponseList.add(cupResponse);
	    }
	    cupListResponse.setCups(cupResponseList);
	    return cupListResponse;
	}
	return null;
    }

    @Override
    public void updateCupName(Long id, String name) throws L99IllegalDataException, L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(id, name);
	BbCup cup = bbCupDao.selectById(id);
	if (cup == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_TEAM_NOT_EXIST, ContestMsg.KEY_TEAM_NOT_EXIST);
	}
	cup.setName(name);
	boolean flag = bbCupDao.update(cup);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    @Override
    public CupListResponse getCupByContestDate() {

	// 先从缓存获取
	Map<Long, String> cupMap = memcacheService.get(ContestMemcached.VALID_BB_CUP);
	if (cupMap == null) {
	    cupMap = refresValidBbCup();
	}
	CupListResponse response = new CupListResponse();
	if (cupMap.size() > 0) {
	    List<CupResponse> cupResponseList = new ArrayList<CupResponse>();
	    CupResponse cupResponse = null;
	    Iterator<Entry<Long, String>> iter = cupMap.entrySet().iterator();
	    while (iter.hasNext()) {
		Entry<Long, String> e = iter.next();
		cupResponse = new CupResponse();
		cupResponse.setTarget_id(e.getKey());
		cupResponse.setName(e.getValue());
		cupResponseList.add(cupResponse);
	    }
	    response.setCups(cupResponseList);
	}
	return response;
    }

    /**
     * 更新当前三十天内容的篮球球联赛列表缓存
     */
    @Override
    public Map<Long, String> refresValidBbCup() {
	Date nowTime = new Date();
	Date startTime = CbsTimeUtils.getCalDate(nowTime, -15);
	Date endTime = CbsTimeUtils.getCalDate(nowTime, 15);
	Map<Long, String> cupMap = bbContestDao.findBbContestCupByRangeTime(startTime, endTime);
	memcacheService.set(ContestMemcached.VALID_BB_CUP, cupMap);
	return cupMap;
    }
}
