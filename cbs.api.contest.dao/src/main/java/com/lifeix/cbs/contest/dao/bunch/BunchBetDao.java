package com.lifeix.cbs.contest.dao.bunch;

import com.lifeix.cbs.contest.dto.bunch.BunchBet;

import java.util.List;

/**
 * Created by lhx on 16-5-16 下午5:56
 *
 * @Description
 */
public interface BunchBetDao {

    BunchBet selectById(long id);

    BunchBet selectByUser(long bunchId, long userId);

    /**
     * 批量获取
     */
    List<BunchBet> selectByUsers(long bunchId, List<Long> userIds);

    boolean insert(BunchBet bunchBet);

    boolean update(BunchBet bunchBet);

    boolean updateBatch(List<BunchBet> bunchBetList);

    /**
     * 获取用户的下单记录
     */
    List<BunchBet> getList(Long userId, Long startId, int limit);
    /**
     * 获取该期中奖记录
     */
    List<BunchBet> getAwardsList(Long bunchId, Integer status, int skip, int limit);

    /**
     * 根据串id和状态来获取
     */
    List<BunchBet> getListByBunchId(Long bunchId, Integer status, Long startId, int limit);

}
