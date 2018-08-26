package com.lifeix.cbs.content.dao.game;

import java.util.Date;
import java.util.List;

import com.lifeix.cbs.content.dto.game.ZodiacAnimal;

/**
 * Created by lhx on 15-12-11 上午11:33
 *
 * @Description
 */
public interface ZodiacAnimalDao {
    /**
     * 插入记录
     */
    public boolean insert(ZodiacAnimal zodiacAnimal);

    public boolean insertBatch(List<ZodiacAnimal> animals);

    /**
     * 获取当期中奖记录
     */
    public ZodiacAnimal findLast();

    /**
     * 获取当期中奖记录
     */
    public ZodiacAnimal findOne(Date now, Integer id);

    
    /**
     * 获取当期中奖记录
     */
    public ZodiacAnimal findOne(Date time);
    
    /**
     * 获得未开奖的
     */
    public List<ZodiacAnimal> findNoLotteryList(String startTime , String endTime);
    
    /**
     * 更新
     */
    public boolean update(Integer id, Integer status, String lottery);

    /**
     * 获取往期
     */
    public List<ZodiacAnimal> findZodiacs(Integer startId, Integer limit);
}
