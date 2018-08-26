package com.lifeix.cbs.content.dao.game.impl;

import com.lifeix.cbs.content.dao.ContentDaoSupport;
import com.lifeix.cbs.content.dao.game.ZodiacAnimalBetDao;
import com.lifeix.cbs.content.dto.game.ZodiacAnimalBet;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lhx on 15-12-11 上午11:34
 *
 * @Description
 */
@Repository("zodiacAnimalBetDao")
public class ZodiacAnimalBetDaoImpl extends ContentDaoSupport implements ZodiacAnimalBetDao {

    @Override
    public ZodiacAnimalBet insert(ZodiacAnimalBet zodiacAnimalBet) {
        if ( sqlSession.insert("ZodiacAnimalBetMapper.insert", zodiacAnimalBet) > 0 ){
            return zodiacAnimalBet ;
        } else {
            return null ;
        }
    }

    @Override
    public List<ZodiacAnimalBet> findZodiacAnimalBets(Long userId, Integer startId, Integer limit, Integer status) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("startId", startId);
        params.put("status", status);
        params.put("limit", limit);
        return sqlSession.selectList("ZodiacAnimalBetMapper.findZodiacAnimalBets", params);
    }

    @Override
    public List<ZodiacAnimalBet> findZodiacAnimalBetsByGameId(Integer gameId) {
        return sqlSession.selectList("ZodiacAnimalBetMapper.findZodiacAnimalBetsByGameId", gameId);
    }

    @Override
    public boolean update(Integer id, Double backSum, Integer status) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("backSum", backSum);
        params.put("status", status);
        return sqlSession.update("ZodiacAnimalBetMapper.update", params) > 0;
    }
}
