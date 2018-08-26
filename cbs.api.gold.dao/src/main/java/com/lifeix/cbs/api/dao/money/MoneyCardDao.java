package com.lifeix.cbs.api.dao.money;

import java.util.List;

import com.lifeix.cbs.api.dao.BasicDao;
import com.lifeix.cbs.api.dto.money.MoneyCard;

public interface MoneyCardDao extends BasicDao<MoneyCard, Long> {

    /**
     * 充值类型列表
     * 
     * @return
     */
    public List<MoneyCard> findMoneyCards(Integer deleteFlag);
}
