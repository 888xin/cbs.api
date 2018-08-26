package com.lifeix.cbs.api.impl.spark.mission;

import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.service.spark.mission.NyxMissionDubbo;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisHashHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lhx on 2016/7/22 11:50
 *
 * @Description
 */
public class NyxMissionDubboImpl implements NyxMissionDubbo {

    @Autowired
    private RedisHashHandler redisHashHandler ;

    @Override
    public Map<Long, Integer> getBatch(List<Long> users, String date) {
        Map<Long, Integer> map = new HashMap<Long, Integer>();
        RedisDataIdentify identify = new RedisDataIdentify(RedisConstants.MissionRedis.NYX, date);
        List<String> userList = new ArrayList<String>();
        for (Long user : users) {
            userList.add(user+"");
        }
        List<byte[]> valueByte = redisHashHandler.hMget(identify, userList);
        for (int i = 0; i < users.size(); i++) {
            map.put(users.get(i), valueByte.get(i) == null ? 0 : Integer.valueOf(new String(valueByte.get(i))));
        }
        return map;
    }

    @Override
    public Map<Long, Integer> getBatch(List<Long> users) {
        String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return getBatch(users, day);
    }

    @Override
    public int get(Long userId) {
        String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return get(userId, day);
    }

    @Override
    public int get(Long userId, String date) {
        RedisDataIdentify identify = new RedisDataIdentify(RedisConstants.MissionRedis.NYX, date);
        byte[] bytes = redisHashHandler.hget(identify,userId.toString().getBytes());
        return bytes == null ? 0 : Integer.valueOf(new String(bytes));
    }
}
