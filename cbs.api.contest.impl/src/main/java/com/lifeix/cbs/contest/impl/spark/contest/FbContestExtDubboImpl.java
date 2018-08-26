package com.lifeix.cbs.contest.impl.spark.contest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.contest.bean.fb.FbContestExtDubboDTO;
import com.lifeix.cbs.contest.dao.fb.FbContestExtDao;
import com.lifeix.cbs.contest.dto.fb.FbContestExt;
import com.lifeix.cbs.contest.service.spark.contest.FbContestExtDubbo;

public class FbContestExtDubboImpl implements FbContestExtDubbo {

    @Autowired
    private FbContestExtDao fbContestExtDao;

    @Override
    public boolean saveContestExt(List<FbContestExtDubboDTO> list) {
	List<FbContestExt> contestExtList = new ArrayList<FbContestExt>(list.size());
	for (FbContestExtDubboDTO dubboDTO : list) {
	    FbContestExt contestExt = new FbContestExt();
	    contestExt.setAwayTeamExt(dubboDTO.getAwayTeamExt());
	    contestExt.setCards(dubboDTO.getCards());
	    contestExt.setContestId(dubboDTO.getContestId());
	    contestExt.setGoals(dubboDTO.getGoals());
	    contestExt.setHomeTeamExt(dubboDTO.getHomeTeamExt());
	    contestExt.setLineups(dubboDTO.getLineups());
	    contestExt.setPenalties(dubboDTO.getPenalties());
	    contestExt.setReferee(dubboDTO.getReferee());
	    contestExt.setScores(dubboDTO.getScores());
	    contestExt.setStatus(dubboDTO.getStatus());
	    contestExt.setSubstitutions(dubboDTO.getSubstitutions());
	    contestExt.setTargetId(dubboDTO.getTargetId());
	    contestExt.setVenue(dubboDTO.getVenue());
	    contestExtList.add(contestExt);
	}
	return fbContestExtDao.saveContestExt(contestExtList);
    }
}
