package com.lifeix.cbs.contest.util.transform;

import com.lifeix.cbs.contest.bean.odds.OddsDssResponse;

/**
 * 足球 单双数vo生成
 * @author Peter
 *
 */
public class FbOddsDssDateUtil {
	
	private OddsDssResponse dssResponse;
	
	private static class InstanceHolder{
		private static final FbOddsDssDateUtil INSTANCE = new FbOddsDssDateUtil();
	}
	
	public static FbOddsDssDateUtil getInstance(){
		return InstanceHolder.INSTANCE;
	}

	private FbOddsDssDateUtil() {
		super();
		dssResponse = new OddsDssResponse();
		dssResponse.setOdd_roi(1.9);
		dssResponse.setEven_roi(1.85);
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
