package com.lifeix.cbs.contest.service.bunch;

import com.lifeix.cbs.contest.bean.bunch.BunchBetListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by lhx on 16-5-18 上午10:03
 *
 * @Description
 */
public interface BunchBetService {

    /**
     * 下单（客户端用）
     */
    public void insert(Long userId, Long bunchContestId, String supports, Long userCouponId) throws L99IllegalParamsException, IOException, JSONException, L99IllegalDataException;

    /**
     * 获取用户下单列表（客户端用）
     */
    BunchBetListResponse getList(Long userId, Long startId, int limit) throws L99IllegalParamsException;

    /**
     * 获取该期中奖记录（客户端用和管理后台用）
     */
    BunchBetListResponse getAwardsList(Long bunchId, Integer status, int page, int limit) throws L99IllegalParamsException;
}
