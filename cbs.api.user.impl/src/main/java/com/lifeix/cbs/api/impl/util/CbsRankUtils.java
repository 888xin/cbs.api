package com.lifeix.cbs.api.impl.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifeix.cbs.api.common.util.ContentBeanUtils;
import com.lifeix.cbs.api.common.util.GoldConstants;
import com.lifeix.cbs.api.dao.user.UserRankLogDao;
import com.lifeix.cbs.api.dto.user.CbsUserRankLog;
import com.lifeix.framework.memcache.MemcacheService;

/**
 * 基于投资回报率的全站用户排名
 * 
 * @author huiy
 * 
 */
public class CbsRankUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CbsRankUtils.class);

    /**
     * 数据读锁
     */
    // private static final Object READ_LOCKER = new Object();

    /**
     * 数据写锁
     */
    // private static final Object WRITE_LOCKER = new Object();

    /**
     * 标识数据库中是否存在排名记录
     */
    private static volatile boolean isExists = false;

    /**
     * 最高的积分
     */
    private static final int MAX_SCORE = 31000;

    /**
     * 所有用户的积分排名数组 积分范围[0,31000) 例如allUserRank[200] =
     * 1000，则表示积分为200的用户目前全站排名为1000；
     */
    // private static int[] allUserRank = new int[MAX_SCORE];

    /**
     * 每个积分对应多少用户 rankUserNum[200] = 10，则表示有10个用户的积分为200。
     */
    // private static int[] rankUserNum = new int[MAX_SCORE];

    private static final String ALLUSERRANK = "allUserRank";

    private static final String RANKUSERNUM = "rankUserNum";

    private static MemcacheService memcacheService = (MemcacheService) ContentBeanUtils.getCurrentBean("memcacheService");

    static {
	loadRankFromDB();
    }

    /**
     * 将数据库中的数据取出放到内存中
     */
    public static void loadRankFromDB() {
	long begin = System.currentTimeMillis();
	UserRankLogDao userRankLogDao = (UserRankLogDao) ContentBeanUtils.getCurrentBean("userRankLogDao");
	// 从数据库中加载全站用户的排名情况
	List<CbsUserRankLog> logs = userRankLogDao.findAll();
	Map<Integer, String> dataMap = getDataMap(logs);
	String ranks = dataMap.get(GoldConstants.Statistics.ALL_USER_RANK);
	String nums = dataMap.get(GoldConstants.Statistics.USER_RANK_NUM);
	if (StringUtils.isNotBlank(ranks) && StringUtils.isNotBlank(nums)) {
	    isExists = true;
	    String[] ranksArr = ranks.split(",");
	    String[] numsArr = nums.split(",");
	    int len = ranksArr.length, len2 = numsArr.length;
	    if (len != len2 && len != MAX_SCORE) {
		LOG.warn(String.format("Get all user roi rank from db not equal %d, %d, %d. Be careful!!!!!!!!!!!!!!!!!!!",
		        len, len2, MAX_SCORE));
	    } else {
		// 将数据库中的数据写入内存
		int[] allUserRank = new int[MAX_SCORE];
		int[] rankUserNum = new int[MAX_SCORE];
		for (int i = 0; i < len; i++) {

		    allUserRank[i] = Integer.valueOf(ranksArr[i]);
		    rankUserNum[i] = Integer.valueOf(numsArr[i]);
		}
		memcacheService.set(getCacheId(ALLUSERRANK), allUserRank);
		memcacheService.set(getCacheId(RANKUSERNUM), rankUserNum);
	    }
	} else {
	    LOG.warn("Cant get user roi rank from db. Be careful!!!!!!!!!!!!!!!!!!!");
	}
	LOG.info("Get user roi rank from db " + (System.currentTimeMillis() - begin));
    }

    private static Map<Integer, String> getDataMap(List<CbsUserRankLog> logs) {
	Map<Integer, String> map = new HashMap<Integer, String>();
	if (logs == null || logs.isEmpty()) {
	    return map;
	}
	for (CbsUserRankLog log : logs) {
	    map.put(log.getLogId(), log.getRank());
	}
	return map;
    }

    /**
     * 将内存中的数据写入数据库
     */
    public static void storeRankToDB() {
	UserRankLogDao userRankLogDao = (UserRankLogDao) ContentBeanUtils.getCurrentBean("userRankLogDao");
	Date now = new Date();
	int[] allUserRank = memcacheService.get(getCacheId(ALLUSERRANK));
	int[] rankUserNum = memcacheService.get(getCacheId(RANKUSERNUM));
	LOG.info("storeRankToDB---allUserRank=" + allUserRank + "rankUserNum=" + rankUserNum);
	if (allUserRank == null) {
	    allUserRank = new int[MAX_SCORE];
	}
	if (rankUserNum == null) {
	    rankUserNum = new int[MAX_SCORE];
	}
	// 数据库中存在排名，则更新数据库中的排名
	if (isExists) {
	    CbsUserRankLog rankLog = new CbsUserRankLog();
	    rankLog.setLogId(GoldConstants.Statistics.ALL_USER_RANK);
	    rankLog.setRank(join(allUserRank, ","));
	    // rankLog.setRank(join(allUserRank, ","));
	    rankLog.setUpdateTime(now);
	    boolean flag = userRankLogDao.update(rankLog);
	    if (flag) {
		CbsUserRankLog numRankLog = new CbsUserRankLog();
		numRankLog.setLogId(GoldConstants.Statistics.USER_RANK_NUM);
		numRankLog.setRank(join(rankUserNum, ","));
		// numRankLog.setRank(join(rankUserNum, ","));
		numRankLog.setUpdateTime(now);
		//LOG.info("isExists storeRankToDB---rank=" + numRankLog.getRank());
		flag = userRankLogDao.update(numRankLog);
	    }
	    if (!flag) {
		LOG.error("Flushing/Update user roi rank to db failed.");
	    }
	    
	} else {
	    // 写入排名
	    CbsUserRankLog rankLog = new CbsUserRankLog();
	    rankLog.setLogId(GoldConstants.Statistics.ALL_USER_RANK);
	    rankLog.setRank(join(allUserRank, ","));
	    // rankLog.setRank(join(allUserRank, ","));
	    rankLog.setUpdateTime(now);
	    boolean flag = userRankLogDao.insert(rankLog);
	    if (flag) {
		CbsUserRankLog numRankLog = new CbsUserRankLog();
		numRankLog.setLogId(GoldConstants.Statistics.USER_RANK_NUM);
		numRankLog.setRank(join(rankUserNum, ","));
		// numRankLog.setRank(join(rankUserNum, ","));
		numRankLog.setUpdateTime(now);
		flag = userRankLogDao.insert(numRankLog);
		if (flag) {
		    isExists = true;
		}
		
		//LOG.info("not isExists  storeRankToDB---rank=" + numRankLog.getRank());
	    }
	    if (!flag) {
		LOG.error("Flushing/Insert user roi rank to db failed.");
	    }
	}
    }

    // }

    /**
     * 添加积分 新加积分，则比该积分小的且存在的积分排名都增加一位
     * 
     * @param score
     * @return
     */
    public static boolean addScore(int score) {
	if (score < 0 || score >= MAX_SCORE) {
	    return false;
	}
	int[] allUserRank = memcacheService.get(getCacheId(ALLUSERRANK));
	int[] rankUserNum = memcacheService.get(getCacheId(RANKUSERNUM));

	if (allUserRank == null) {
	    allUserRank = new int[MAX_SCORE];
	}
	if (rankUserNum == null) {
	    rankUserNum = new int[MAX_SCORE];
	}

	// 小于该积分的所有用户排名都往后一名
	for (int i = 0; i < score; i++) {
	    if (rankUserNum[i] > 0) {
		allUserRank[i]++;
	    }
	}
	// 计算score对应的排名
	int topRank = 0;
	for (int i = score + 1; i < MAX_SCORE; i++) {
	    if (rankUserNum[i] > 0) {
		// 找到比当前积分大且存在排名的积分，由于存在并列排名的情况，所有需要加上这个分数对应的人数
		topRank = allUserRank[i] + rankUserNum[i] - 1;
		break;
	    }
	}
	// 在前一个积分的基础上排名+1
	allUserRank[score] = topRank + 1;
	// 对应的人数+1
	rankUserNum[score]++;
	LOG.info("addScore--" + allUserRank[score] + "--" + rankUserNum[score]);
	memcacheService.set(getCacheId(ALLUSERRANK), allUserRank);
	memcacheService.set(getCacheId(RANKUSERNUM), rankUserNum);
	return true;
    }

    /**
     * 积分变动
     * 
     * @param fromScore
     * @param toScore
     */
    public static boolean changeScore(int fromScore, int toScore) {
	if (fromScore < 0 || fromScore >= MAX_SCORE || toScore < 0 || toScore >= MAX_SCORE) {
	    return false;
	}
	int[] allUserRank = memcacheService.get(getCacheId(ALLUSERRANK));
	int[] rankUserNum = memcacheService.get(getCacheId(RANKUSERNUM));
	if (allUserRank == null) {
	    allUserRank = new int[MAX_SCORE];
	}
	if (rankUserNum == null) {
	    rankUserNum = new int[MAX_SCORE];
	}
	if (fromScore < toScore) {
	    if (rankUserNum[fromScore] <= 0) {
		return false;
	    }
	    // 该积分对应的人数-1
	    rankUserNum[fromScore]--;

	    // 只有一个用户的积分，改变后将其值置0
	    if (rankUserNum[fromScore] == 0) {
		allUserRank[fromScore++] = 0;
	    }
	    // 积分增加,则积分段范围内的所有用户的排名都往后一名
	    for (; fromScore < toScore; fromScore++) {
		if (allUserRank[fromScore] > 0) {
		    allUserRank[fromScore]++;
		}
	    }
	    // 计算积分改变后的排名
	    int topRank = 0;
	    for (int i = toScore + 1; i < MAX_SCORE; i++) {
		if (allUserRank[i] > 0) {
		    topRank = allUserRank[i] + rankUserNum[i] - 1;
		    break;
		}
	    }
	    allUserRank[toScore] = topRank + 1;
	    // 对应的积分人数+1
	    rankUserNum[toScore]++;
	} else if (fromScore > toScore) {

	    if (rankUserNum[fromScore] <= 0) {
		return false;
	    }
	    // 该积分对应的人数-1
	    rankUserNum[fromScore]--;

	    // 只有一个用户的积分，改变后将其值置零
	    if (rankUserNum[fromScore] == 0) {
		allUserRank[fromScore] = 0;
	    }
	    fromScore--;

	    // 积分减小，则积分段范围内的所有用户的排名都往前一名
	    for (; fromScore > toScore; fromScore--) {
		if (allUserRank[fromScore] > 0) {
		    allUserRank[fromScore]--;
		}
	    }
	    // 计算改变后的排名
	    int topRank = 0;
	    for (int i = toScore + 1; i < MAX_SCORE; i++) {
		if (allUserRank[i] > 0) {
		    topRank = allUserRank[i] + rankUserNum[i] - 1;
		    break;
		}
	    }
	    allUserRank[toScore] = topRank + 1;
	    // 对应的积分人数+1
	    rankUserNum[toScore]++;
	}

	LOG.info("changeScore--" + allUserRank[toScore] + "--" + rankUserNum[toScore]);
	
	memcacheService.set(getCacheId(ALLUSERRANK), allUserRank);
	memcacheService.set(getCacheId(RANKUSERNUM), rankUserNum);
	return true;
    }

    /**
     * 根据积分获取对应的排名
     * 
     * @param score
     * @return
     */
    public static int getAllRoiRank(Integer score) {
	if (score == null || score < 0 || score >= MAX_SCORE) {
	    return 0;
	}
	int[] allUserRank = memcacheService.get(getCacheId(ALLUSERRANK));
	if (allUserRank == null) {
	    allUserRank = new int[MAX_SCORE];
	}
	return allUserRank[score];
    }

    /**
     * 同一个积分对应的人数
     * 
     * @param score
     * @return
     */
    public static int getRankUser(Integer score) {
	if (score == null || score < 0 || score >= MAX_SCORE) {
	    return 0;
	}
	int[] rankUserNum = memcacheService.get(getCacheId(RANKUSERNUM));
	if (rankUserNum == null) {
	    rankUserNum = new int[MAX_SCORE];
	}
	return rankUserNum[score];
    }

    /**
     * 清空内存中的排名数据
     * 
     * @return
     */
    public static boolean clearRankArray() {
	memcacheService.delete(getCacheId(ALLUSERRANK));
	memcacheService.delete(getCacheId(RANKUSERNUM));
	return true;
    }

    private static String join(int[] array, String separator) {
	if (array == null) {
	    return null;
	}
	if (separator == null) {
	    separator = "";
	}
	int startIndex = 0, endIndex = array.length;
	int bufSize = (endIndex - startIndex);
	if (bufSize <= 0) {
	    return "";
	}
	bufSize *= (16 + separator.length());

	StrBuilder buf = new StrBuilder(bufSize);
	if (startIndex < endIndex) {
	    buf.append(array[startIndex++]);
	}
	for (; startIndex < endIndex; startIndex++) {
	    buf.append(separator).append(array[startIndex]);
	}
	return buf.toString();
    }

    private static String getCacheId(Object id) {
	return String.format("%s:id:%s", "com.lifeix.cbs.api.impl.util.CbsRankUtils", id.toString());
    }

}
