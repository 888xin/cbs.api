package com.lifeix.cbs.content.dao.game;

import com.lifeix.cbs.content.dto.game.ZodiacAnimalBet;

import java.util.List;

/**
 * Created by lhx on 15-12-11 上午11:33
 *
 * @Description
 */
public interface ZodiacAnimalBetDao {
    /**
     * 插入记录
     */
    public ZodiacAnimalBet insert(ZodiacAnimalBet zodiacAnimalBet);

    /**
     * 获取下单记录
     */
    public List<ZodiacAnimalBet> findZodiacAnimalBets(Long userId, Integer startId, Integer limit, Integer status);

    /**
     * 获取下单记录
     */
    public List<ZodiacAnimalBet> findZodiacAnimalBetsByGameId(Integer gameId);

    /**
     * 更新
     */
    public boolean update(Integer id, Double backSum, Integer status);
}
