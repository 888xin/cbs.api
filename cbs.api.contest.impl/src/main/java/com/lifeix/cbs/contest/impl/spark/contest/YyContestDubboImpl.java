package com.lifeix.cbs.contest.impl.spark.contest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.bean.yy.YyContestResponse;
import com.lifeix.cbs.contest.dao.yy.YyContestDao;
import com.lifeix.cbs.contest.dto.yy.YyContest;
import com.lifeix.cbs.contest.service.spark.contest.YyContestDubbo;
import com.lifeix.cbs.contest.util.transform.YyContestTransformUtil;

@Service("yyContestDubbo")
public class YyContestDubboImpl implements YyContestDubbo {

    @Autowired
    private YyContestDao yyContestDao;

    /**
     * 获取超时未结算的比赛列表(超过开场时间3小时)
     */
    @Override
    public List<YyContestResponse> findTimeoutContest(Long contestId) {
	List<YyContest> timeouts = yyContestDao.findTimeoutContest(contestId, 20);
	List<YyContestResponse> contestReponses = new ArrayList<YyContestResponse>();
	for (YyContest contest : timeouts) {
	    contestReponses.add(YyContestTransformUtil.transformYyContest(contest, null, true));
	}
	return contestReponses;
    }

}
