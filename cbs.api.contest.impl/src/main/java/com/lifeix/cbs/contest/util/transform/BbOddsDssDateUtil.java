package com.lifeix.cbs.contest.util.transform;

import com.lifeix.cbs.contest.bean.odds.OddsDssResponse;

/**
 * 篮球 单双数vo生成
 * @author Peter
 *
 */
public class BbOddsDssDateUtil {
	
	private OddsDssResponse dssResponse;
	
	private static class InstanceHolder{
		private static final BbOddsDssDateUtil INSTANCE = new BbOddsDssDateUtil();
	}
	
	public static BbOddsDssDateUtil getInstance(){
		return InstanceHolder.INSTANCE;
	}

	private BbOddsDssDateUtil() {
		super();
		dssResponse = new OddsDssResponse();
		dssResponse.setOdd_roi(1.9);
		dssResponse.setEven_roi(1.9);
		dssResponse.setBet_flag(false);
		dssResponse.setPlay_flag(false);
		dssResponse.setClose_flag(false);
	}

	public void setContestId(Long contestId) {
		dssResponse.setContest_id(contestId);
	}
	
	public OddsDssResponse getDssResponse() {
		return dssResponse;
	}

}
