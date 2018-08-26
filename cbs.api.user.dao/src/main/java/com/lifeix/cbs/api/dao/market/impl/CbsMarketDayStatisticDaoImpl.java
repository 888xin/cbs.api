package com.lifeix.cbs.api.dao.market.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.dao.ContentDaoSupport;
import com.lifeix.cbs.api.dao.market.CbsMarketDayStatisticDao;
import com.lifeix.cbs.api.dto.market.CbsMarketDayStatistic;

@Service("cbsMarketDayStatisticDao")
public class CbsMarketDayStatisticDaoImpl extends ContentDaoSupport implements CbsMarketDayStatisticDao {

    @Override
    public List<CbsMarketDayStatistic> findList(String marketCode, Long startId, Date statisticDateBefore,
	    Date statisticDateAfter, int limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	if (StringUtils.isNotEmpty(marketCode)) {
	    params.put("marketCode", marketCode);
	}

	if (statisticDateBefore != null) {
	    params.put("statisticDateBefore", statisticDateBefore);
	}

	if (statisticDateAfter != null) {
	    params.put("statisticDateAfter", statisticDateAfter);
	}

	if (startId != null && startId > 0) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	return sqlSession.selectList("CbsMarketDayStatisticMapper.findList", params);
    }

    @Override
    public Integer insert(CbsMarketDayStatistic bean) {
	return sqlSession.insert("CbsMarketDayStatisticMapper.insert", bean);
    }

}
