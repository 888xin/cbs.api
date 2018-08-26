package com.lifeix.cbs.contest.service.spark.contest;

import java.io.IOException;

/**
 * Created by lhx on 16-5-18 上午11:34
 *
 * @Description
 */
public interface BunchContestDubbo {

    /**
     * 修改状态，status从0修改为1，结束下单；状态为1且endTime < NOW()时，就获取赛事的结果，如果全部赛事都有结果了，更新状态为2
     */
    void updateStatus(int status) throws IOException;

    /**
     * 结算，满足条件就结算，status从1修改为-1
     */
    //void settle() throws IOException, L99IllegalDataException, L99IllegalParamsException;
}
