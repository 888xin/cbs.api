package com.lifeix.cbs.contest.impl.contest;

import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.contest.dao.bb.BbTeamDao;
import com.lifeix.cbs.contest.dao.contest.ContestStatisticsDao;
import com.lifeix.cbs.contest.dao.fb.FbTeamDao;
import com.lifeix.cbs.contest.dto.bb.BbTeam;
import com.lifeix.cbs.contest.dto.contest.Contest;
import com.lifeix.cbs.contest.dto.fb.FbTeam;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.contest.ContestStatisticsService;
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
 * Created by lhx on 2016/8/1 14:50
 *
 * @Description
 */
@Service("contestStatisticsService")
public class ContestStatisticsServiceImpl extends ImplSupport implements ContestStatisticsService {

    private final Double MUCH = 1000D ;

    protected static Logger LOG = LoggerFactory.getLogger(ContestStatisticsServiceImpl.class);

    @Autowired
    private ContestStatisticsDao contestStatisticsDao ;

    @Autowired
    private BbTeamDao bbTeamDao;

    @Autowired
    private FbTeamDao fbTeamDao;

    @Autowired
    private RedisStringHandler redisStringHandler;

    @Resource(name = "srt")
    private StringRedisTemplate srt;

    @Override
    public void findContestsBySame(int type) {
        List<Contest> list = contestStatisticsDao.findContestsBySame(type);
        Map<String, List<Long>> map = new HashMap<>();
        for (Contest contest : list) {
            String time = new SimpleDateFormat("yyyy-MM-dd").format(contest.getStartTime());
            String homeKey = time + ":" + contest.getHomeTeam();
            List<Long> homeList = map.get(homeKey);
            if (homeList == null){
                homeList = new ArrayList<>();
            }
            homeList.add(contest.getContestId());
            map.put(homeKey, homeList);

            String awayKey = time + ":" + contest.getAwayTeam();
            List<Long> awayList = map.get(awayKey);
            if (awayList == null){
                awayList = new ArrayList<>();
            }
            awayList.add(contest.getContestId());
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
                RedisDataIdentify indentify ;
                if (type == ContestConstants.ContestType.FOOTBALL){
                    indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.CONTEST_REPEAT_FB);
                } else {
                    indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.CONTEST_REPEAT_BB);
                }
                indentify.setIdentifyId(s);
                // 七天后过期删除
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
    public String[] getContestsBySame(int type) {
        RedisDataIdentify indentify ;
        if (type == ContestConstants.ContestType.FOOTBALL){
            indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.CONTEST_REPEAT_FB);
        } else {
            indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.CONTEST_REPEAT_BB);
        }
        indentify.setIdentifyId("*");
        Set<String> set = redisStringHandler.keys(indentify);
        int n = set.size();
        if (n > 0){
            String[] strings = new String[n];
            for (String s : set) {
                n -- ;
                strings[n] = srt.opsForValue().get(s) ;
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
    public void refreshContestsSame(int type) {
        RedisDataIdentify indentify ;
        if (type == ContestConstants.ContestType.FOOTBALL){
            indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.CONTEST_REPEAT_FB);
        } else {
            indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.CONTEST_REPEAT_BB);
        }
        indentify.setIdentifyId("*");
        Set<String> set = redisStringHandler.keys(indentify);
        for (String s : set) {
            srt.delete(s);
        }
        findContestsBySame(type);
    }

    @Override
    public void findMuchBetMoney(int type) {
        List<Contest> list = contestStatisticsDao.findContestsByStatus(type, ContestConstants.ContestStatu.NOTOPEN);
        for (Contest contest : list) {
            get(type, contest.getContestId());
        }

    }

    private void get(int type, long contestId){
        if (type == ContestConstants.ContestType.FOOTBALL){
            for (int i = 1; i < 5; i++) {
                if (i == 3){
                    continue;
                }
                for (int j = 0; j < 3; j++) {
                    Double betMoney = contestStatisticsDao.findBetMoney(i, contestId, j);
                    save(contestId, i, j, betMoney);
                }
            }
        } else {
            for (int i = 6; i < 10; i++) {
                if (i == 8){
                    continue;
                }
                for (int j = 0; j < 2; j++) {
                    Double betMoney = contestStatisticsDao.findBetMoney(i, contestId, j);
                    save(contestId, i, j, betMoney);
                }
            }
        }

    }

    private void save(long contestId, int i, int j, Double betMoney) {
        if (betMoney != null && betMoney >= MUCH){
            RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.BET_MUCH);
            String key = contestId + "-" + i + "-" + j;
            indentify.setIdentifyId(key);
            // 七天后过期删除
            long expireTime = 60*60*24*7;
            indentify.setExpireTime(expireTime);
            redisStringHandler.set(indentify, new byte[]{});
        }
    }


    @Override
    public String[] getMuchBetInfo() {
        RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.BET_MUCH);
        indentify.setIdentifyId("*");
        Set<String> set = redisStringHandler.keys(indentify);
        int index = set.size();
        String[] result = new String[index];
        for (String s : set) {
            index -- ;
            String[] keys = s.split(":");
            String key =  keys[keys.length - 1] ;
            String[] keysTmp = key.split("-");
            int type = 0 ;
            if (Integer.valueOf(keysTmp[1]) > 5){
                type = 1 ;
            }
            Contest contest = contestStatisticsDao.findContestsById(Long.valueOf(keysTmp[0]), type);
            String teamStr ;
            if (Integer.valueOf(keysTmp[1]) > 5){
                BbTeam bbTeam1 = bbTeamDao.selectById(contest.getHomeTeam());
                BbTeam bbTeam2 = bbTeamDao.selectById(contest.getAwayTeam());
                teamStr = bbTeam1.getName() + "-" + bbTeam2.getName();
            } else {
                FbTeam fbTeam1 = fbTeamDao.selectById(contest.getHomeTeam());
                FbTeam fbTeam2 = fbTeamDao.selectById(contest.getAwayTeam());
                teamStr = fbTeam1.getName() + "-" + fbTeam2.getName();
            }
            result[index] = key + "-" + contest.getStartTime() + "-" + teamStr;
        }
        return result;
    }

    @Override
    public void refreshMuchBet() {
        RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, RedisConstants.ContestRedis.BET_MUCH);
        indentify.setIdentifyId("*");
        Set<String> set = redisStringHandler.keys(indentify);
        for (String s : set) {
            srt.delete(s);
        }
        findMuchBetMoney(ContestConstants.ContestType.FOOTBALL);
        findMuchBetMoney(ContestConstants.ContestType.BASKETBALL);
    }
}
