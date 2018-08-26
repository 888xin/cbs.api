package com.lifeix.cbs.contest.impl.spark.contest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode.ScoreModuleMsg;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.contest.ScoreModuleResponse;
import com.lifeix.cbs.contest.dao.contest.ScoreModuleDao;
import com.lifeix.cbs.contest.dto.contest.ScoreModule;
import com.lifeix.cbs.contest.service.spark.contest.ScoreModuleDubbo;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

@Service("scoreModuleDubbo")
public class ScoreModuleDubboImpl implements ScoreModuleDubbo {

    @Autowired
    private ScoreModuleDao scoreModuleDao;

    @Override
    public ScoreModuleResponse selectByContestType(Integer contestType)
            throws L99IllegalDataException, L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(contestType);
	ScoreModule sm = scoreModuleDao.selectByContestType(contestType);
	if (sm == null)
	    throw new L99IllegalDataException(ScoreModuleMsg.CODE_SCORE_MODULE_NOT_FOUND,
	            ScoreModuleMsg.KEY_SCORE_MODULE_NOT_FOUND);
	ScoreModuleResponse resp = ContestTransformUtil.transformScoreModule(sm);
	return resp;
    }

    @Override
    public void updateModuleValue(Integer contestType, Integer moduleValue)
            throws L99IllegalParamsException, L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(contestType, moduleValue);
	ScoreModule sm = scoreModuleDao.selectByContestType(contestType);
	if (sm == null)
	    throw new L99IllegalDataException(ScoreModuleMsg.CODE_SCORE_MODULE_NOT_FOUND,
	            ScoreModuleMsg.KEY_SCORE_MODULE_NOT_FOUND);
	sm.setModuleValue(moduleValue);
	scoreModuleDao.updateModuleValue(sm);
    }
}
