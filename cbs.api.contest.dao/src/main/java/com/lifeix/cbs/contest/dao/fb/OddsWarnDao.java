package com.lifeix.cbs.contest.dao.fb;

import java.util.List;

import com.lifeix.cbs.contest.dto.odds.OddsWarn;

public interface OddsWarnDao {

    public OddsWarn selectById(long id);

    public OddsWarn selectByOddsId(Integer playType, Long oddsId);

    public boolean insert(OddsWarn entity);

    public boolean update(OddsWarn entity);

    public List<OddsWarn> selectByStatus(Integer status, Long startId, int limit);
}