package com.lifeix.cbs.api.dao.market.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lifeix.anti.common.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.dao.ContentDaoSupport;
import com.lifeix.cbs.api.dao.market.CbsMarketMainDao;
import com.lifeix.cbs.api.dto.market.CbsMarketMain;

@Service("cbsMarketMainDao")
public class CbsMarketMainDaoImpl extends ContentDaoSupport implements CbsMarketMainDao {

    @Override
    public List<CbsMarketMain> findList(String nickName, String password, String status) {
	Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(nickName)){
			params.put("nickName", nickName);
		}
		if (StringUtils.isNotEmpty(password)){
			params.put("password", password);
		}
		if (StringUtils.isNotEmpty(status)){
			params.put("status", status);
		}
	return sqlSession.selectList("CbsMarkeMainMapper.findList", params);
    }

}
