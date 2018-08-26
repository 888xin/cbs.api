package com.lifeix.cbs.contest.service.spark.contest;

import java.util.List;

import com.lifeix.cbs.contest.bean.bb.BbContestExtDubboDTO;

public interface BbContestExtDubbo {

    public boolean saveContestExt(List<BbContestExtDubboDTO> list);

}
