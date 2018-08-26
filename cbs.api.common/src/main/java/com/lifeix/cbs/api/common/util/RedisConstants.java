package com.lifeix.cbs.api.common.util;

/**
 * Redis key命令常量
 *
 * @author lifeix
 */
public class RedisConstants {

	// 用户模块
	public static final String MODEL_USER = "cbs-api-user";

	// 赛事模块
	public static final String MODEL_CONTEST = "cbs-api-contest";

	// 内容模块
	public static final String MODEL_CONTENT = "cbs-api-content";

	// 货币模块
	public static final String MODEL_GOLD = "cbs-api-gold";

	// 活动模块
	public static final String MODEL_ACTIVITY = "cbs-api-activity";

	// 串模块
	public static final String MODEL_BUNCH = "cbs-api-bunch";

	// 任务模块
	public static final String MODEL_MISSION = "cbs-api-mission";

	// 消息模块
	public static final String MODEL_MESSAGE = "cbs-api-message";

	/**
	 * 赛事模块
	 *
	 * @author lifeix
	 */
	public static final class ContestRedis {

		/**
		 * 单场赛事下单统计
		 */
		public static final String CONTEST_COUNT = "contest:count:all";

		/**
		 * 单场赛事支持统计
		 */
		public static final String CONTEST_BET = "contest:bet:count";

		/**
		 * 针对赛事进行押押下单统计
		 */
		public static final String CONTEST_YY_BET = "contest:yy:count";

		/**
		 * 押押选项照片
		 */
		public static final String CONTEST_YY_OPTION_IMAGE = "contest:yy:option:image";

		/**
		 * 押押精选
		 */
		public static final String CONTEST_YY_GOOD = "contest:yy:option:good";

		/**
		 * 针对用户进行押押下单统计
		 */
		public static final String CONTEST_YY_USER = "contest:yy:user";

		/**
		 * 猜友圈未读数量
		 */
		public static final String CIRCLE_COMMENT = "contest:circle:comment";

		/**
		 * 赛事每日下单数统计
		 */
		public static final String STATISTIC_BETS = "contest:statistic:bet:day";

		/**
		 * 赛事每日下单人数统计
		 */
		public static final String STATISTIC_PEOTLE = "contest:statistic:people:day";

		/**
		 * 赛事每日胜平负下单统计
		 */
		public static final String STATISTIC_OP = "contest:statistic:bet:op";

		/**
		 * 赛事每日让球胜平负下单统计
		 */
		public static final String STATISTIC_JC = "contest:statistic:bet:jc";

		/**
		 * 赛事每日足球下单统计
		 */
		public static final String STATISTIC_FB = "contest:statistic:bet:fb";

		/**
		 * 赛事每日篮球下单统计
		 */
		public static final String STATISTIC_BB = "contest:statistic:bet:bb";

		/**
		 * 赛事重复
		 */
		public static final String CONTEST_REPEAT_BB = "contest:repeat:bb";
		public static final String CONTEST_REPEAT_FB = "contest:repeat:fb";

		/**
		 * 重注
		 */
		public static final String BET_MUCH = "contest:bet:much";

	}

	/**
	 * 货币模块
	 *
	 * @author lifeix
	 */
	public static final class GoldRedis {

		/**
		 * 每日用户龙币收入统计
		 */
		public static final String STATISTIC_lB_INCOME = "gold:statistic:longbi:income";

		/**
		 * 每日用户龙币支出统计
		 */
		public static final String STATISTIC_lB_OUTLAY = "gold:statistic:longbi:outlay";

		/**
		 * 每日系统龙币收入（用户充值）统计
		 */
		public static final String STATISTIC_RECHARGE_INCOME = "gold:statistic:longbi:recharge:income";

		/**
		 * 每日系统龙币支出（用户购买商品）统计
		 */
		public static final String STATISTIC_GOODS_OUTLAY = "gold:statistic:longbi:goods:outlay";

		/**
		 * 每日龙筹信鸽消息
		 */
		public static final String COUPON_USER_PIGEON_MESSAGE = "coupon:user:pigeon:message";

	}

	/**
	 * 用户模块
	 *
	 * @author lifeix
	 */
	public static final class UserRedis {

		// 活动券领取
		public static final String COUPON_USER_KEY = "COUPON_USER_KEY";

		// 旧版龙筹转换新版龙筹记录
		public static final String COUPON_SEND = "coupon:send";

		/**
		 * 连续登录奖励记录
		 */
		public static final String USER_LOGIN_MONTH = "login:month:%s";

		/**
		 * 用户是否领取奖励记录
		 */
		public static final String USER_LOGIN_DAY = "login:day:%s";

		/**
		 * 用户注册--客户端
		 */
		public static final String USER_REG_TYPE = "register:type";

		/**
		 * 用户注册--手机或微信端
		 */
		public static final String USER_REG_SOURCE = "register:source";

		/**
		 * 用户注册--性别
		 */
		public static final String USER_REG_GENDER = "register:gender";

		/**
		 * 渠道列表
		 */
		public static final String MARKET_MARKET = "market:market";

		/**
		 * 渠道登录
		 */
		public static final String MARKET_MARKET_LOGIN = "market:marketLogin";

		/**
		 * 渠道日统计
		 */
		public static final String MARKET_MARKET_STATISTIC = "market:marketStatistic";
	}

	/**
	 * 内容模块
	 *
	 * @author lifeix
	 */
	public static final class ContentRedis {

		// 统计评论数redis
		public static final String COMMENT = "comment";

	}

	/**
	 * 消息模块
	 *
	 * @author lifeix
	 */
	public static final class MessageRedis {

		// 公告关联
		public static final String MESSAGE_PLACARD_RELATION = "message:placard:relation";
	}

	/**
	 * 活动模块
	 *
	 * @author lifeix
	 */
	public static final class ActivityRedis {

		// 首充活动记录
		public static final String FIRST_RECORDS = "activity:first:records";

		// 首充活动获奖消息
		public static final String FIRST_REWARD_LOG = "activity:first:reward:log";

	}

	public static final class BunchRedis {

		// 下注人数
		public static final String BET_RECORDS = "bunch:bet";

		public static final String IMAGE = "prize:image";

	}

	/**
	 * 榜单模块
	 *
	 * @author lifeix
	 */
	public static final class RankRedis {

		// 胜率总榜
		public static final String RANK_WINNING_ALL = "rank:winning:all";
		// 胜率周榜
		public static final String RANK_WINNING_WEEK = "rank:winning:week";
		// 回报率总榜
		public static final String RANK_ROI_ALL = "rank:roi:all";
		// 回报率周榜
		public static final String RANK_ROI_WEEK = "rank:roi:week";
		// 收益总榜
		public static final String RANK_INCOME_ALL = "rank:income:all";
		// 收益周榜
		public static final String RANK_INCOME_WEEK = "rank:income:week";
		// 投资总榜
		public static final String RANK_INVEST_ALL = "rank:invest:all";
		// 投资周榜
		public static final String RANK_INVEST_WEEK = "rank:invest:week";
	}

	public static final class MissionRedis {

		// 每日任务
		public static final String DAY = "mission:day";

		//床上用户额外的任务
		public static final String NYX = "mission:nyx";

		//床上做过任务的用户
		public static final String USER = "mission:user";

		// 取消的每日任务
		public static final String CANCEL = "mission:cancel";

		// 用户是否触发过客户端的咆哮窗口（首次）
		public static final String ROAR_FIRST = "mission:roar:first";

		// 用户是否触发过客户端的咆哮窗口（每日）
		public static final String ROAR_DAY = "mission:roar:day";

	}

}
