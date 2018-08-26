package com.lifeix.cbs.api.impl.market;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.bean.market.CbsMarketMainResponse;
import com.lifeix.cbs.api.bean.market.CbsMarketStatListResponse;
import com.lifeix.cbs.api.bean.market.CbsMarketStatisticResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.dao.market.CbsMarketDayStatisticDao;
import com.lifeix.cbs.api.dao.market.CbsMarketMainDao;
import com.lifeix.cbs.api.dto.market.CbsMarketDayStatistic;
import com.lifeix.cbs.api.dto.market.CbsMarketMain;
import com.lifeix.cbs.api.impl.redis.RedisMarketHandler;
import com.lifeix.cbs.api.impl.util.AccountTransformUtil;
import com.lifeix.cbs.api.service.market.CbsMarketService;
import com.lifeix.common.utils.StringUtil;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * @author yis
 * 
 */
@Service("cbsMarketService")
public class CbsMarketServiceImpl extends ImplSupport implements CbsMarketService {

    protected static final Logger LOG = LoggerFactory.getLogger(CbsMarketServiceImpl.class);

    @Autowired
    private CbsMarketDayStatisticDao cbsMarketDayStatisticDao;

    @Autowired
    private CbsMarketMainDao cbsMarketMainDao;

    @Autowired
    private RedisMarketHandler redisMarketHandler;

    /**
     * 获取所有渠道统计
     * 
     * @param market
     * @param token
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public CbsMarketStatListResponse findMarketStat(String market, String token, String queryDateBefore,
	    String queryDateAfter, Long startId, int limit) throws L99IllegalParamsException {

	if (StringUtils.isEmpty(token)) {
	    throw new L99IllegalParamsException(MsgCode.UserTokenMsg.CODE_TOKEN_NOT_EXSIST,
		    MsgCode.UserTokenMsg.KEY_TOKEN_NOT_EXSIST);
	}

	if (StringUtils.isEmpty(market)) {
	    throw new L99IllegalParamsException(MsgCode.BasicMsg.CODE_PARAMEMETER, MsgCode.BasicMsg.KEY_PARAMEMETER);
	}

	// 校验渠道token是否存在
	String loginToken = redisMarketHandler.getMarketLoginInfo(market);

	if (!loginToken.equals(token)) {
	    throw new L99IllegalParamsException(MsgCode.UserTokenMsg.CODE_TOKEN_NOT_EXSIST,
		    MsgCode.UserTokenMsg.KEY_TOKEN_NOT_EXSIST);
	}
	List<CbsMarketDayStatistic> list = null;
	if (market.equals("admin")) {
	    Date statisticDateBefore = null;
	    Date statisticDateAfter = null;
	    try {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isEmpty(queryDateBefore)) {
		    Calendar cal = Calendar.getInstance();
		    cal.add(Calendar.DATE, -1);
		    String date = sdf.format(cal.getTime());
		    statisticDateBefore = sdf.parse(date);
		} else {
		    statisticDateBefore = sdf.parse(queryDateBefore);
		}

		// if (StringUtils.isEmpty(queryDateAfter)) {
		// statisticDateAfter = new Date();
		// } else {
		// statisticDateBefore = sdf.parse(queryDateBefore);
		// }
	    } catch (ParseException e) {
		LOG.error(e.getMessage(), e);
	    }

	    list = cbsMarketDayStatisticDao.findList(null, startId, statisticDateBefore, statisticDateBefore, limit);
	} else {
	    // 获取渠道数据
	    list = cbsMarketDayStatisticDao.findList(StringUtil.MD5Encode(market), startId, null, null, limit);
	}

	List<CbsMarketStatisticResponse> marketstats = new ArrayList<CbsMarketStatisticResponse>();
	CbsMarketStatisticResponse retBean = null;
	Map<String, String> marketMd5Map = findMarketList();
	for (CbsMarketDayStatistic bean : list) {
	    retBean = AccountTransformUtil.transformMarketStat(bean, marketMd5Map);
	    marketstats.add(retBean);
	}

	if (list.size() == 0 || list.size() < limit) { // 没有数据或数据不够返回0
	    startId = 0L;
	} else {
	    startId = Long.valueOf(list.get(list.size() - 1).getId());
	}

	CbsMarketStatListResponse reponse = new CbsMarketStatListResponse();
	reponse.setStartId(startId);
	reponse.setMarketstats(marketstats);
	return reponse;
    }

    @Override
    public Map<String, String> findMarketList() {
	try {
	    List<CbsMarketMain> list = cbsMarketMainDao.findList(null, null, null);
	    if (list.size() > 0) {
		Map<String, String> map = new HashMap();
		for (CbsMarketMain bean : list) {
		    map.put(StringUtil.MD5Encode(bean.getMarketCode()), bean.getMarketCode());
		}
		return map;
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return null;
    }

    @Override
    public CbsMarketMainResponse findMarket(String nickName, String password) throws L99IllegalParamsException {
	ParamemeterAssert.assertStringNotNull(nickName, password);
	CbsMarketMainResponse response = null;
	List<CbsMarketMain> list = cbsMarketMainDao.findList(nickName, password, "4");

	if (list.size() > 0) {
	    response = AccountTransformUtil.transformMarket(list.get(0));
	    String token = UUID.randomUUID().toString();
	    redisMarketHandler.addMarketLogin(response.getMarketCode(), token);
	    response.setToken(token);
	    response.setMarketCode(response.getMarketCode());
	}
	return response;
    }
}
