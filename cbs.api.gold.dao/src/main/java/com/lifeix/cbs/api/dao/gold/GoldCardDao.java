package com.lifeix.cbs.api.dao.gold;

import java.util.List;

import com.lifeix.cbs.api.dao.BasicDao;
import com.lifeix.cbs.api.dto.gold.GoldCard;

public interface GoldCardDao extends BasicDao<GoldCard, Long> {

    /**
     * 充值活动列表
     * 
     * @param enable
     * @return
     */
    public List<GoldCard> findGoldCards(Boolean enable);

}
