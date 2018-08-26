package com.lifeix.cbs.contest.impl.statistic;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.statistic.ContestStatisticDao;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.statistic.ContestStatisticService;

/**
 * Created by lhx on 15-12-21 上午11:15
 *
 * @Description
 */
@Service("contestStatisticService")
public class ContestStatisticServiceImpl extends ImplSupport implements ContestStatisticService {

    // 下单数量
    private final String BETS_STATISTIC = this.getClass().getName() + ":betsStatistic";
    // 下单人数数量
    private final String PEOPLE_STATISTIC = this.getClass().getName() + ":peopleStatistic";
    // 胜平负数量
    private final String OP_STATISTIC = this.getClass().getName() + ":opStatistic";
    // 让球胜平负数量
    private final String JC_STATISTIC = this.getClass().getName() + ":jcStatistic";
    // 足球数量
    private final String FB_STATISTIC = this.getClass().getName() + ":fbStatistic";
    // 篮球数量
    private final String BB_STATISTIC = this.getClass().getName() + ":bbStatistic";

    //限制的长度
    private final static long LIMIT = 180;


    @Resource(name = "srt")
    private StringRedisTemplate srt;

    @Autowired
    private ContestStatisticDao contestStatisticDao;

    @Override
    public void betsStatistic(String date) {
        date = getDate(date);
        int num = contestStatisticDao.betsStatistic(date);
        String[] dates = date.split("-");
        String noStr = dates[0] + dates[1] + dates[2];
        Long no = Long.valueOf(noStr);
        ZSetOperations<String, String> zso = srt.opsForZSet();
        zso.add(BETS_STATISTIC, num + "", no);
        // 控制长度
        long keyCount = zso.zCard(BETS_STATISTIC);
        if (keyCount > LIMIT) {
            zso.removeRange(BETS_STATISTIC, 0, 0);
        }
    }

    @Override
    public void peopleStatistic(String date) {
        date = getDate(date);
        int num = contestStatisticDao.peopleStatistic(date);
        String[] dates = date.split("-");
        String noStr = dates[0] + dates[1] + dates[2];
        Long no = Long.valueOf(noStr);
        ZSetOperations<String, String> zso = srt.opsForZSet();
        zso.add(PEOPLE_STATISTIC, num + "", no);
        // 控制长度
        long keyCount = zso.zCard(PEOPLE_STATISTIC);
        if (keyCount > LIMIT) {
            zso.removeRange(PEOPLE_STATISTIC, 0, 0);
        }
    }

    @Override
    public void opStatistic(String date) {
        date = getDate(date);
        int num = contestStatisticDao.opStatistic(date);
        String[] dates = date.split("-");
        String noStr = dates[0] + dates[1] + dates[2];
        Long no = Long.valueOf(noStr);
        ZSetOperations<String, String> zso = srt.opsForZSet();
        zso.add(OP_STATISTIC, num + "", no);
        // 控制长度
        long keyCount = zso.zCard(OP_STATISTIC);
        if (keyCount > LIMIT) {
            zso.removeRange(OP_STATISTIC, 0, 0);
        }
    }

    @Override
    public void jcStatistic(String date) {
        date = getDate(date);
        int num = contestStatisticDao.jcStatistic(date);
        String[] dates = date.split("-");
        String noStr = dates[0] + dates[1] + dates[2];
        Long no = Long.valueOf(noStr);
        ZSetOperations<String, String> zso = srt.opsForZSet();
        zso.add(JC_STATISTIC, num + "", no);
        // 控制长度
        long keyCount = zso.zCard(JC_STATISTIC);
        if (keyCount > LIMIT) {
            zso.removeRange(JC_STATISTIC, 0, 0);
        }
    }

    @Override
    public void fbStatistic(String date) {
        date = getDate(date);
        int num = contestStatisticDao.fbStatistic(date);
        String[] dates = date.split("-");
        String noStr = dates[0] + dates[1] + dates[2];
        Long no = Long.valueOf(noStr);
        ZSetOperations<String, String> zso = srt.opsForZSet();
        zso.add(FB_STATISTIC, num + "", no);
        // 控制长度
        long keyCount = zso.zCard(FB_STATISTIC);
        if (keyCount > LIMIT) {
            zso.removeRange(FB_STATISTIC, 0, 0);
        }
    }

    @Override
    public void bbStatistic(String date) {
        date = getDate(date);
        int num = contestStatisticDao.bbStatistic(date);
        String[] dates = date.split("-");
        String noStr = dates[0] + dates[1] + dates[2];
        Long no = Long.valueOf(noStr);
        ZSetOperations<String, String> zso = srt.opsForZSet();
        zso.add(BB_STATISTIC, num + "", no);
        // 控制长度
        long keyCount = zso.zCard(BB_STATISTIC);
        if (keyCount > LIMIT) {
            zso.removeRange(BB_STATISTIC, 0, 0);
        }
    }

    private String getDate(String date) {
        if (date == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(calendar.getTime());
        } else {
            return date;
        }
    }

}
