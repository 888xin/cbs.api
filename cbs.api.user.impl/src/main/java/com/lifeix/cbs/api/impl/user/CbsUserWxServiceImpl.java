/**
 * 
 */
package com.lifeix.cbs.api.impl.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.bean.user.CbsUserWxResponse;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.dao.user.CbsUserWxDao;
import com.lifeix.cbs.api.dto.user.CbsUserWx;
import com.lifeix.cbs.api.impl.util.AccountTransformUtil;
import com.lifeix.cbs.api.service.user.CbsUserWxService;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

@Service("cbsUserWxService")
public class CbsUserWxServiceImpl extends ImplSupport implements CbsUserWxService {

    @Autowired
    private CbsUserWxDao cbsUserWxDao;

    @Override
    public void saveUserWxInfo(Long userId, String openId, String appId, String source) throws L99IllegalParamsException,
	    L99NetworkException, JSONException {
	ParamemeterAssert.assertDataNotNull(userId, openId, appId, source);
	CbsUserWx userWx = new CbsUserWx();
	userWx.setAppId(appId);
	userWx.setOpenId(openId);
	userWx.setSource(source);
	userWx.setUserId(userId);
	userWx.setCreateTime(new Date());
	CbsUserWx bean = cbsUserWxDao.selectById(userId);
	if (bean == null) {
	    cbsUserWxDao.insertAndGetPrimaryKey(userWx);
	} else {
	    cbsUserWxDao.update(userWx);
	}
    }

    @Override
    public void updateUserWxInfo(Long userId, String openId, String appId, String source) {
	CbsUserWx userWx = new CbsUserWx();
	userWx.setAppId(appId);
	userWx.setOpenId(openId);
	userWx.setSource(source);
	userWx.setUserId(userId);
	userWx.setCreateTime(new Date());
	cbsUserWxDao.update(userWx);
    }

    @Override
    public CbsUserWxResponse selectById(Long userId) {
	CbsUserWx bean = cbsUserWxDao.selectById(userId);
	return AccountTransformUtil.transformUserWx(bean);
    }

    @Override
    public List<CbsUserWxResponse> selectById(List<Long> userIds) {
	List<CbsUserWx> list = cbsUserWxDao.selectByUsers(userIds);
	List<CbsUserWxResponse> responses = new ArrayList<CbsUserWxResponse>();
	CbsUserWxResponse res = null;
	if (list != null) {
	    for (CbsUserWx bean : list) {
		res = AccountTransformUtil.transformUserWx(bean);
		responses.add(res);
	    }
	}
	return responses;
    }
}
