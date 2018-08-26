package com.lifeix.cbs.api.service.market;

import java.util.Map;

import com.lifeix.cbs.api.bean.market.CbsMarketMainResponse;
import com.lifeix.cbs.api.bean.market.CbsMarketStatListResponse;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * @author yis
 * 
 */
public interface CbsMarketService {

    /**
     * 获取所有渠道
     * 
     * @return
     * @throws L99IllegalParamsException
     */
    public Map<String, String> findMarketList();

    /**
     * 获取所有渠道统计
     * 
     * @param nickName
     * @param password
     * @return
     * @throws L99IllegalParamsException
     */
    public CbsMarketMainResponse findMarket(String nickName, String password) throws L99IllegalParamsException;

    /**
     * 获取所有渠道统计
     * 
     * @param market
     * @param startId
     * @param limit
     * @return
     */
    public CbsMarketStatListResponse findMarketStat(String market, String token, String queryDateBefore,
	    String queryDateAfter, Long startId, int limit) throws L99IllegalParamsException;

}
