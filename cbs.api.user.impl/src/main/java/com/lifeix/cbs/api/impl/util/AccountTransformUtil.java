package com.lifeix.cbs.api.impl.util;

import java.util.Map;

import com.lifeix.cbs.api.bean.market.CbsMarketMainResponse;
import com.lifeix.cbs.api.bean.market.CbsMarketStatisticResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.bean.user.CbsUserStarResponse;
import com.lifeix.cbs.api.bean.user.CbsUserWxResponse;
import com.lifeix.cbs.api.bean.user.UserContestStatisticsResponse;
import com.lifeix.cbs.api.bean.user.UserStatisticsResponse;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.dto.market.CbsMarketDayStatistic;
import com.lifeix.cbs.api.dto.market.CbsMarketMain;
import com.lifeix.cbs.api.dto.rank.UserContestStatistics;
import com.lifeix.cbs.api.dto.rank.UserContestStatisticsWeek;
import com.lifeix.cbs.api.dto.user.CbsUser;
import com.lifeix.cbs.api.dto.user.CbsUserStar;
import com.lifeix.cbs.api.dto.user.CbsUserWx;
import com.lifeix.user.beans.account.FullAccountResponse;

/**
 * DTO 转换 VO
 * 
 * @author peter
 * 
 */
public class AccountTransformUtil {

    public static UserContestStatisticsResponse transformUserContestStatistics(UserContestStatistics statistics, double gold) {
	UserContestStatisticsResponse resp = new UserContestStatisticsResponse();
	if (statistics != null) {
	    resp.setUser_id(statistics.getUserId());
	    resp.setBet_count(statistics.getBetCount());
	    resp.setWin_count(statistics.getWinCount());
	    resp.setDraw_count(statistics.getDrawCount());
	    resp.setLoss_count(statistics.getLossCount());
	    resp.setWinning(statistics.getWinning());
	    // 避免客户端发布新版本，将这个字段设置为最长连胜记录
	    resp.setCon_win_count(statistics.getMaxConWinCount());
	    resp.setMax_con_win_count(statistics.getMaxConWinCount());
	    // 避免客户端发布新版本，将这个字段设置为最长连输记录
	    resp.setCon_loss_count(statistics.getMaxConLossCount());
	    resp.setMax_con_loss_count(statistics.getMaxConLossCount());
	    resp.setWin_odds(statistics.getWinOdds());
	    resp.setLoss_odds(statistics.getLossOdds());
	    resp.setRoi(statistics.getRoi());
	    resp.setRank(statistics.getRank());
	    resp.setContest_count(statistics.getContestCount());
	} else {
	    resp.setRank(9999);
	}
	resp.setGold(gold);
	return resp;
    }

    /**
     * 用户dto转换vo
     * 
     * @param user
     * @param simpleFlag
     * @return
     */
    public static CbsUserResponse transformUser(CbsUser user, boolean simpleFlag) {
	CbsUserResponse resp = null;
	if (user != null) {
	    resp = new CbsUserResponse();
	    resp.setUser_id(user.getUserId());
	    resp.setName(user.getUserName());
	    resp.setHead(user.getUserPath());
	    resp.setGender(user.getGender());
	    resp.setLocation(user.getLocal());
	    resp.setLong_no(user.getUserNo());
	    resp.setBackground(user.getBack());
	    resp.setAboutme(user.getAboutme());
	    resp.setStatus(user.getStatus());
	    if (!simpleFlag) {
		resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(user.getCreateTime()));
	    }
	}
	return resp;
    }

    public static UserStatisticsResponse transformUserContestStatisticsWeek(UserContestStatisticsWeek week, Integer rank,
	    boolean simple) {
	UserStatisticsResponse resp = null;
	if (week != null) {
	    resp = new UserStatisticsResponse();
	    resp.setUser_id(week.getUserId());
	    resp.setBet_count(week.getBetCount());
	    resp.setWinning(week.getWinning());
	    resp.setRank(rank);
	    resp.setWin_gold(week.getWinGold());
	    resp.setFinal_gold(week.getProfit());
	    resp.setWin_count(week.getWinCount());
	    resp.setDraw_count(week.getDrawCount());
	    resp.setLoss_count(week.getLossCount());
	    resp.setRoi(week.getRoi());
	    resp.setWin_odds(week.getWinOdds());
	    resp.setLoss_odds(week.getLossOdds());
	    resp.setBet_gold(week.getBetGold());
	    resp.setLoss_gold(week.getLossGold());
	}
	return resp;
    }

    public static UserStatisticsResponse transformUserStatistics(UserContestStatistics statistics, Integer rank,
	    Double fund, Boolean isLongbi) {
	UserStatisticsResponse resp = null;
	if (statistics != null) {
	    resp = new UserStatisticsResponse();
	    resp.setUser_id(statistics.getUserId());
	    resp.setBet_gold(statistics.getBetGold());
	    resp.setWinning(statistics.getWinning());
	    resp.setWin_gold(statistics.getWinGold());
	    resp.setRank(rank);
	    resp.setFund(fund);
	    resp.setLongbi(isLongbi);
	    resp.setBet_count(statistics.getBetCount());
	    resp.setWin_count(statistics.getWinCount());
	    resp.setDraw_count(statistics.getDrawCount());
	    resp.setLoss_count(statistics.getLossCount());
	    resp.setRoi(statistics.getRoi());
	    resp.setWin_odds(statistics.getWinOdds());
	    resp.setLoss_odds(statistics.getLossOdds());
	    resp.setBet_gold(statistics.getBetGold());
	    resp.setLoss_gold(statistics.getLossGold());
	    resp.setFinal_gold(CommerceMath.sub(statistics.getWinGold(), statistics.getLossGold()));
	}
	return resp;
    }

    /**
     * 从立方网用户填充数据到大赢家用户
     * 
     * @param account
     * @param user
     * @param b
     */
    public static void transformCbsUserResponse(FullAccountResponse account, CbsUserResponse user, boolean b) {
	if (account != null && user != null) {
	    user.setAccountType(account.getAccountType());
	    user.setAvatarsId(account.getAvatarsId());
	    user.setDomainName(account.getDomainName());
	    user.setEmail(account.getEmail());
	    user.setFirstName(account.getFirstName());
	    user.setLastName(account.getLastName());
	    user.setLat(account.getLat());
	    user.setLng(account.getLng());
	    user.setLocation(account.getLocation());
	    user.setMiniBlog(account.getMiniBlog());
	    user.setMobilePhone(account.getMobilePhone());
	    user.setMobilePhoto(account.getMobilePhoto());
	    user.setSpaceName(account.getSpaceName());
	    user.setStatus(account.getStatus());
	    user.setPassword(account.getPassword());
	}
    }

    public static UserStatisticsResponse transformUser(CbsUser user, Integer rank, Double fund, Boolean isLongbi) {
	UserStatisticsResponse resp = null;
	if (user != null) {
	    resp = new UserStatisticsResponse();
	    resp.setUser_id(user.getUserId());
	    resp.setBet_gold(0.0D);
	    resp.setWinning(0.0D);
	    resp.setWin_gold(0.0D);
	    resp.setRank(rank);
	    resp.setFund(fund);
	    resp.setLongbi(isLongbi);
	    resp.setBet_count(0);
	    resp.setWin_count(0);
	    resp.setDraw_count(0);
	    resp.setLoss_count(0);
	    resp.setRoi(0.0D);
	    resp.setWin_odds(0.0D);
	    resp.setLoss_odds(0.0D);
	    resp.setBet_gold(0.0D);
	    resp.setLoss_gold(0.0D);
	    resp.setFinal_gold(0.0D);
	}
	return resp;
    }

    /**
     * 用户推荐信息转换
     * 
     * @param userStar
     * @return
     */
    public static CbsUserStarResponse transformUserStar(CbsUserStar userStar) {
	CbsUserStarResponse resp = null;
	if (userStar != null) {
	    resp = new CbsUserStarResponse();
	    resp.setId(userStar.getId());
	    resp.setUser_id(userStar.getUserId());
	    resp.setRank(userStar.getRank());
	    resp.setWinning(userStar.getWinning());
	    resp.setShow_num(userStar.getShowNum());
	    resp.setHit_num(userStar.getHitNum());
	    resp.setFactor(userStar.getFactor());
	    resp.setHide_flag(userStar.isHideFlag());
	    resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(userStar.getCreateTime()));
	}
	return resp;
    }

    /**
     * 渠道统计转换
     * 
     * @param bean
     * @param marketMd5Map
     * @return
     */
    public static CbsMarketStatisticResponse transformMarketStat(CbsMarketDayStatistic bean, Map<String, String> marketMd5Map) {
	CbsMarketStatisticResponse resp = null;
	if (bean != null) {
	    resp = new CbsMarketStatisticResponse();
	    resp.setId(bean.getId());
	    resp.setMarketCode(bean.getMarketCode());
	    resp.setStatisticDate(CbsTimeUtils.getTimeForDate(bean.getStatisticDate()));
	    resp.setFemaleNums(bean.getFemaleNums());
	    resp.setMaleNums(bean.getMaleNums());
	    resp.setTotalNums(bean.getTotalNums());
	    if (marketMd5Map != null) {
		resp.setMarketCodeOri(marketMd5Map.get(bean.getMarketCode()));
	    }
	}
	return resp;
    }

    /**
     * 渠道转换
     * 
     * @param bean
     * @return
     */
    public static CbsMarketMainResponse transformMarket(CbsMarketMain bean) {
	CbsMarketMainResponse resp = null;
	if (bean != null) {
	    resp = new CbsMarketMainResponse();
	    resp.setId(bean.getId());
	    resp.setMarketCode(bean.getMarketCode());
	    resp.setNickName(bean.getNickName());
	    resp.setStatus(bean.getStatus());
	}
	return resp;
    }

    /**
     * 用户微信信息转换
     * 
     * @param userStar
     * @return
     */
    public static CbsUserWxResponse transformUserWx(CbsUserWx bean) {
	CbsUserWxResponse resp = null;
	if (bean != null) {
	    resp = new CbsUserWxResponse();
	    resp.setUser_id(bean.getUserId());
	    resp.setApp_id(bean.getAppId());
	    resp.setSource(bean.getSource());
	    resp.setOpen_id(bean.getOpenId());
	    resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(bean.getCreateTime()));
	}
	return resp;
    }
}
