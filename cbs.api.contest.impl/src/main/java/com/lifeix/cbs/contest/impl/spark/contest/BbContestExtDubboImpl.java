package com.lifeix.cbs.contest.impl.spark.contest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.contest.bean.bb.BbContestExtDubboDTO;
import com.lifeix.cbs.contest.dao.bb.BbContestExtDao;
import com.lifeix.cbs.contest.dto.bb.BbContestExt;
import com.lifeix.cbs.contest.service.spark.contest.BbContestExtDubbo;

public class BbContestExtDubboImpl implements BbContestExtDubbo {

    @Autowired
    private BbContestExtDao bbContestExtDao;

    @Override
    public boolean saveContestExt(List<BbContestExtDubboDTO> list) {
	List<BbContestExt> contestExtList = new ArrayList<BbContestExt>(list.size());
	for (BbContestExtDubboDTO dubboDTO : list) {
	    BbContestExt contestExt = new BbContestExt();
	    contestExt.setContestId(dubboDTO.getContestId());
	    contestExt.setLineups(dubboDTO.getLineups());
	    contestExt.setReferee(dubboDTO.getReferee());
	    contestExt.setVenue(dubboDTO.getVenue());
	    contestExtList.add(contestExt);
	}
	return bbContestExtDao.saveContestExt(contestExtList);
    }
}
