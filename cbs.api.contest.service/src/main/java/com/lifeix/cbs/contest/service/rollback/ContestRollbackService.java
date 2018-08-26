package com.lifeix.cbs.contest.service.rollback;

import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import org.json.JSONException;

/**
 * Created by lhx on 16-3-17 上午11:19
 *
 * @Description 回滚赛事
 */
public interface ContestRollbackService {

    /**
     * 回滚
     */
    public void rollback(Long contestId, Integer contestType) throws L99IllegalParamsException, L99IllegalDataException, JSONException;
}
