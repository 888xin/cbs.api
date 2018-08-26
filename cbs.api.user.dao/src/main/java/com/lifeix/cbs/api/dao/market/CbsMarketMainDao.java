package com.lifeix.cbs.api.dao.market;

import java.util.List;

import com.lifeix.cbs.api.dto.market.CbsMarketMain;

public interface CbsMarketMainDao {

    /**
     * 获取渠道
     * @param nickName
     * @param password
     * @param status
     * @return
     */
    public List<CbsMarketMain> findList(String nickName, String password, String status);
}