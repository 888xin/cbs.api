package com.lifeix.cbs.contest.dao.contest.impl;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.contest.RepairBetDao;
import com.lifeix.cbs.contest.dto.bet.BetRepair;
import org.springframework.stereotype.Repository;

/**
 * Created by lhx on 16-7-1 上午10:20
 *
 * @Description
 */
@Repository("repairBetDao")
public class RepairBetDaoImpl extends ContestDaoSupport implements RepairBetDao {

    @Override
    public boolean insert(BetRepair betRepair) {
        return sqlSession.insert("RepairBetMapper.insert",betRepair) > 0;
    }

}
