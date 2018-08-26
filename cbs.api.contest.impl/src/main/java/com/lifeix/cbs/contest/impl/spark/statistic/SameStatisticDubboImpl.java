package com.lifeix.cbs.contest.impl.spark.statistic;

import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.contest.dao.bb.BbContestDao;
import com.lifeix.cbs.contest.dao.fb.FbContestDao;
import com.lifeix.cbs.contest.dto.bb.BbContest;
import com.lifeix.cbs.contest.dto.fb.FbContest;
import com.lifeix.cbs.contest.service.spark.statistic.SameStatisticDubbo;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisStringHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lhx on 2016/7/20 16:37
 *
 * @Description
 */
@Service("sameStatisticDubbo")
public class SameStatisticDubboImpl implements SameStatisticDubbo {

    protected static Logger LOG = LoggerFactory.getLogger(SameStatisticDubboImpl.class);

    @Autowired
    private FbContestDao fbContestDao;

    @Autowired
    private BbContestDao bbContestDao;

    @Autowired
    private RedisStringHandler redisStringHandler;

    @Resource(name = "srt")
    private StringRedisTemplate srt;

    @Override
    public void findFbContestsBySame() {
        List<FbContest> list = fbContestDao.findFbContestsBySame();
        Map<String, List<Long>> map = new HashMap<>();
        for (FbContest fbContest : list) {
            String time = new SimpleDateFormat("yyyy-MM-dd").format(fbContest.getStartTime());
            String homeKey = time + ":" + fbContest.getHomeTeam();
            List<Long> homeList = map.get(homeKey);
            if (homeList == null){
                homeList = new ArrayList<>();
            }
            homeList.add(fbContest.getContestId());
            map.put(homeKey, homeList);

            String awayKey = time + ":" + fbContest.getAwayTeam();
            List<Long> awayList = map.get(awayKey);
            if (awayList == null){
                awayList = new ArrayList<>();
            }
            awayList.add(fbContest.getContestId());
            map.put(awayKey, awayList);
        }
        for (String s : map.keySet()) {
            List<Long> mapList = map.get(s);
            if (mapList.size() > 1){
                StringBuilder stringBuilder = new StringBuilder("赛事ID为：(");
                for (Long aLong : mapList) {
                    stringBuilder.append(aLong).append("，");
                }
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                stringBuilder.append("）").append("，日期与相同的球队ID：").append(s);
                //记录重复的赛事ID
                RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.CONTEST_REPEAT_FB);
                indentify.setIdentifyId(s);
                // 第二天的凌晨00:00:00过期删除
                long expireTime = 60*60*24*7;
                indentify.setExpireTime(expireTime);
                try {
                    redisStringHandler.set(indentify, stringBuilder.toString().getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    LOG.error(e.getMessage(),e);
                }
            }
        }
    }

    @Override
    public String[] getFbContestsBySame() {
        RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.CONTEST_REPEAT_FB);
        indentify.setIdentifyId("*");
        Set<String> set = redisStringHandler.keys(indentify);
        int n = set.size();
        if (n > 0){
            String[] strings = new String[n];
            n -- ;
            for (String s : set) {
                strings[n] = srt.opsForValue().get(s) ;
                n -- ;
            }
            return strings ;
        }
        return new String[0];
    }

    @Override
    public int[] getContestsBySameNum() {
        int[] result = new int[2];
        RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.CONTEST_REPEAT_FB);
        indentify.setIdentifyId("*");
        Set<String> set = redisStringHandler.keys(indentify);
        result[0] = set.size();

        RedisDataIdentify indentify2 = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.CONTEST_REPEAT_BB);
        indentify2.setIdentifyId("*");
        Set<String> set2 = redisStringHandler.keys(indentify2);
        result[1] = set2.size();
        return result;
    }


    @Override
    public void findBbContestsBySame() {
        List<BbContest> list = bbContestDao.findBbContestsBySame();
        Map<String, List<Long>> map = new HashMap<>();
        for (BbContest bbContest : list) {
            String time = new SimpleDateFormat("yyyy-MM-dd").format(bbContest.getStartTime());
            String homeKey = time + ":" + bbContest.getHomeTeam();
            List<Long> homeList = map.get(homeKey);
            if (homeList == null){
                homeList = new ArrayList<>();
            }
            homeList.add(bbContest.getContestId());
            map.put(homeKey, homeList);

            String awayKey = time + ":" + bbContest.getAwayTeam();
            List<Long> awayList = map.get(awayKey);
            if (awayList == null){
                awayList = new ArrayList<>();
            }
            awayList.add(bbContest.getContestId());
            map.put(awayKey, awayList);
        }
        for (String s : map.keySet()) {
            List<Long> mapList = map.get(s);
            if (mapList.size() > 1){
                StringBuilder stringBuilder = new StringBuilder("赛事ID为：(");
                for (Long aLong : mapList) {
                    stringBuilder.append(aLong).append("，");
                }
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                stringBuilder.append("）").append("，日期与相同的球队ID：").append(s);
                //记录重复的赛事ID
                RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.CONTEST_REPEAT_BB);
                indentify.setIdentifyId(s);
                // 第二天的凌晨00:00:00过期删除
                long expireTime = 60*60*24*7;
                indentify.setExpireTime(expireTime);
                try {
                    redisStringHandler.set(indentify, stringBuilder.toString().getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    LOG.error(e.getMessage(),e);
                }
            }
        }
    }

    @Override
    public String[] getBbContestsBySame() {
        RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.CONTEST_REPEAT_BB);
        indentify.setIdentifyId("*");
        Set<String> set = redisStringHandler.keys(indentify);
        int n = set.size();
        if (n > 0){
            String[] strings = new String[n];
            n -- ;
            for (String s : set) {
                strings[n] = srt.opsForValue().get(s) ;
                n -- ;
            }
            return strings ;
        }
        return new String[0];
    }
}
