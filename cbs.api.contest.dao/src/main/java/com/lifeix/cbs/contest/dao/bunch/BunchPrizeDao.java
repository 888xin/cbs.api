package com.lifeix.cbs.contest.dao.bunch;

import com.lifeix.cbs.contest.dto.bunch.BunchPrize;

import java.util.List;

/**
 * Created by lhx on 16-5-19 下午4:41
 *
 * @Description
 */
public interface BunchPrizeDao {

    BunchPrize selectById(long id);

    List<BunchPrize> selectByBunchId(long bunchId);

    boolean insert(BunchPrize bunchPrize);

    boolean insertBatch(List<BunchPrize> bunchPrizes);

    boolean update(BunchPrize bunchPrize);
}
