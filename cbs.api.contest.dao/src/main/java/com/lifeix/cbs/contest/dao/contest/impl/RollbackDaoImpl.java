package com.lifeix.cbs.contest.dao.contest.impl;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.contest.RollbackDao;
import com.lifeix.cbs.contest.dto.contest.RollbackLog;
import org.springframework.stereotype.Repository;

/**
 * Created by lhx on 16-3-7 下午3:42
 *
 * @Description
 */
@Repository("rollbackDao")
public class RollbackDaoImpl extends ContestDaoSupport implements RollbackDao {

    @Override
    public boolean insert(String descr) {
        return sqlSession.insert("RollbackMapper.insert",descr) > 0;
    }
}
