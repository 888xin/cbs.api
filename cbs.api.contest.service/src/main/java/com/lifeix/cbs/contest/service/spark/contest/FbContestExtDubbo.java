package com.lifeix.cbs.contest.service.spark.contest;

import java.util.List;

import com.lifeix.cbs.contest.bean.fb.FbContestExtDubboDTO;

public interface FbContestExtDubbo {

    public boolean saveContestExt(List<FbContestExtDubboDTO> list);

}
