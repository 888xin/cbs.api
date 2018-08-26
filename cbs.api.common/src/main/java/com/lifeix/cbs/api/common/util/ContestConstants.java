package com.lifeix.cbs.api.common.util;

public class ContestConstants {

    /**
     * 数据库返回最大记录数
     */
    public static final int SQL_RET_LIMIT = 2000;

    /**
     * 每个事务处理100条下单 缩短事务时间
     */
    public final static int SETTLE_LIMIT = 100;

    /**
     * 赛事自定义缓存
     * 
     * @author lifeix
     * 
     */
    public final static class ContestMemcached {
	/**
	 * 当前三十天的足球联赛缓存Key
	 */
	public final static String VALID_FB_CUP = "valid:fb:cup";

	/**
	 * 当前三十天的篮球联赛缓存Key
	 */
	public final static String VALID_BB_CUP = "valid:bb:cup";

	/**
	 * 榜单缓存时间 10分钟
	 */
	public final static int RANK_EXPIRED = 600;

	/**
	 * 胜率榜周榜缓存
	 */
	public final static String RANK_WIN_WEEK = "rank:win:week";

	/**
	 * 胜率榜总榜缓存
	 */
	public final static String RANK_WIN_ALL = "rank:win:all";

	/**
	 * 回报率榜周榜缓存
	 */
	public final static String RANK_ROI_WEEK = "rank:roi:week";

	/**
	 * 回报率榜总榜缓存
	 */
	public final static String RANK_ROI_ALL = "rank:roi:all";
    }

    /**
     * 比赛类型
     */
    public final static class ContestType {

	/**
	 * 足球
	 */
	public static final int FOOTBALL = 0;

	/**
	 * 篮球
	 */
	public static final int BASKETBALL = 1;

	/**
	 * 押押
	 */
	public static final int YAYA = 10;

	/**
	 * 吐槽
	 */
	public static final int TUCAO = 11;

	/**
	 * pk
	 */
	public static final int PK = 20;

	/**
	 * 生肖游戏
	 */
	public static final int GAME = 30;

	/**
	 * 头版
	 */
	public static final int FRONTPAGE = 40;

    }

    /**
     * 足球赛事状态
     */
    public final static class ContestStatu {

	/**
	 * 未开
	 */
	public static final int NOTOPEN = 0;

	/**
	 * 上半场
	 */
	public static final int HALF_PREV = 1;

	/**
	 * 中场
	 */
	public static final int MIDFIELDER = 2;

	/**
	 * 下半场
	 */
	public static final int HALF_NEXT = 3;

	/** 上下半场结束，等待加时赛开始 **/
	public static final int AWAITING_EXTRA_TIME = 4;
	/** 加时赛开始 **/
	public static final int OVERTIME = 5;
	/** 加时赛上半场 **/
	public static final int EXTRA_1ST = 6;
	/** 加时赛半场休息 **/
	public static final int EXTRA_HALFTIME = 7;
	/** 加时赛下半场 **/
	public static final int EXTRA_2ND = 8;
	/** 等待点球大战 **/
	public static final int AWAITING_PENALTIES = 9;
	/** 点球大战 **/
	public static final int PENALTIES = 10;

	/**
	 * 待定
	 */
	public static final int PEDING = -11;

	/**
	 * 腰斩
	 */
	public static final int CUT = -12;

	/**
	 * 中断
	 */
	public static final int INTERRUPT = -13;

	/**
	 * 推迟
	 */
	public static final int PUTOFF = -14;

	/**
	 * 完场
	 */
	public static final int DONE = -1;

	/**
	 * 取消
	 */
	public static final int CANCLE = -10;
    }

    /**
     * 足球赛事层级与彩种定义
     */
    public final static class ContestLevel {

	/**
	 * 一级
	 */
	public static final int LEVEL_1 = 1;

	/**
	 * 二级
	 */
	public static final int LEVEL_2 = 2;

	/**
	 * 足彩
	 */
	public static final int LOTTERY_FB = 4;

	/**
	 * 竞彩
	 */
	public static final int LOTTERY_SMG = 8;

	/**
	 * 单场
	 */
	public static final int LOTTERY_SINGLE = 16;
    }

    /**
     * 任务类型
     */
    public static final class MeadalTaskType {

	/**
	 * cbs_user_contest_statistics 更新猜友圈
	 */
	public static final Long USER_CONTEST_CIRCLE = 1L;

	/**
	 * cbs_user_contest_statistics 统计 并计算总榜
	 */
	public static final Long USER_CONTEST_STATISTICS = 3L;
	/**
	 * cbs_user_contest_statistics_week统计 并计算周榜
	 */
	public static final Long USER_CONTEST_STATISTICS_WEEK = 5L;

    }

    /**
     * 押押赛事状态
     */
    public final static class ContestStatu_YY {

	/**
	 * 初始状态
	 */
	public static final int NOTOPEN = 0;

	/**
	 * 正常结算
	 */
	public static final int DONE = -1;

	/**
	 * 走盘结算
	 */
	public static final int CANCLE = -10;
    }

    /**
     * 篮球赛事状态
     */
    public final static class ContestStatu_BB {

	/**
	 * 未开
	 */
	public static final int NOTOPEN = 0;

	/**
	 * 一节
	 */
	public static final int ONE = 1;

	/**
	 * 二节
	 */
	public static final int TWO = 2;

	/**
	 * 三节
	 */
	public static final int THREE = 3;

	/**
	 * 四节
	 */
	public static final int FOUR = 4;

	/**
	 * 加时
	 */
	public static final int OVERTIME = 5;

	/**
	 * 暂停
	 */
	public static final int PAUSE = 10;

	/**
	 * 中场
	 */
	public static final int MIDFIELDER = 50;

	/**
	 * 待定
	 */
	public static final int PEDING = -2;

	/**
	 * 中断
	 */
	public static final int INTERRUPT = -3;

	/**
	 * 推迟
	 */
	public static final int PUTOFF = -5;

	/**
	 * 完场
	 */
	public static final int DONE = -1;

	/**
	 * 取消
	 */
	public static final int CANCLE = -4;
    }

    /**
     * 篮球赛事层级与彩种定义
     */
    public final static class ContestLevel_BB {

	/**
	 * 一级
	 */
	public static final int LEVEL_1 = 1;

	/**
	 * NBA
	 */
	public static final int LEVEL_NBA = 2;

	/**
	 * 竞彩
	 */
	public static final int LEVEL_SMG = 4;

    }

    /**
     * 足球事件类型
     */
    public final static class ContestEventType_FB {
	/** 普通进球 */
	public static final int COMMON_GOAL = 1;
	/** 点球(90分钟和加时) */
	public static final int PENALTY_GOAL = 2;
	/** 乌龙球 */
	public static final int OWN_GOAL = 3;
	/** 头球 */
	public static final int HEADING_GOAL = 4;
	/** 黄牌 */
	public static final int YELLOW_CARD = 5;
	/** 两黄变一红 */
	public static final int YELLOW_RED_CARD = 6;
	/** 红牌 */
	public static final int RED_CARD = 7;
	/** 换下 */
	public static final int PLAYER_OUT = 8;
	/** 换上 */
	public static final int PLAYER_IN = 9;
	/** 点球大战进球 */
	public static final int PENALTIES_IN = 10;
	/** 点球大战罚失 */
	public static final int PENALTIES_OUT = 11;
    }

    /**
     * 篮球事件类型
     */
    public final static class ContestEventType_BB {
	/** 开场争球 **/
	public static final int OPEN_TIP = 0;
	/** 跳球 **/
	public static final int JUMP_BALL = 1;
	/** 两分不进 **/
	public static final int TWO_POINT_MISS = 2;
	/** 两分进球 **/
	public static final int TWO_POINT_MADE = 3;
	/** 三分不进 **/
	public static final int THREE_POINT_MISS = 4;
	/** 三分进球 **/
	public static final int THREE_POINT_MADE = 5;
	/** 罚球不中 **/
	public static final int FREE_THROW_MISS = 6;
	/** 罚球得分 **/
	public static final int FREE_THROW_MADE = 7;
	/** 篮板 **/
	public static final int REBOUND = 8;
	/** 失误 **/
	public static final int TURNOVER = 9;
	/** 投篮犯规 **/
	public static final int SHOOTING_FOUL = 10;
	/** 进攻24秒超时违例 **/
	public static final int TEAM_TIME_OUT = 11;
	/** 个人犯规 **/
	public static final int PERSONAL_FOUL = 12;
	/** 单节结束 **/
	public static final int END_PERIOD = 13;
	/** 进攻犯规 **/
	public static final int OFFENSIVE_FOUL = 14;
	/** 官方暂停 **/
	public static final int OFFICIAL_TIME_OUT = 15;
	/** 脚踢球违例 **/
	public static final int KICK_BALL = 16;
	/** 单节开始 **/
	public static final int OPEN_IN_BOUND = 17;
	/** 防守三秒违例 **/
	public static final int DEFENSIVE_THREE_SECONDS = 18;
	/** 复审 **/
	public static final int REVIEW = 19;
	/** 技术犯规 **/
	public static final int TECHNICAL_FOUL = 20;
	/** 阻碍比赛技术犯规 **/
	public static final int DELAY = 21;
	/** 电视暂停 **/
	public static final int TV_TIME_OUT = 22;
	/** 得球 **/
	public static final int POSSESSION = 23;
	/** 路径犯规 **/
	public static final int CLEAR_PATH_FOUL = 24;
	/** 一级恶意犯规 **/
	public static final int FLAGRANTONE = 25;
	/** 二级恶意犯规 **/
	public static final int FLAGRANTTWO = 26;
	/** 口头警告 **/
	public static final int WARNING = 27;
	/** 驱逐出场 **/
	public static final int EJECTION = 28;
    }

    /**
     * 篮球球员赛事状态
     */
    public final static class BbPlayerStatus {
	/** 首发 **/
	public static final int STARTER = 0;
	/** 替补上场 **/
	public static final int SUBSTITUTE = 1;
	/** 未上场 **/
	public static final int NOT_PLAYED = 2;
	/** 未激活 **/
	public static final int NOT_ACTIVE = 3;
    }

    /**
     * 篮球球员场上位置
     */
    public final static class BbPlayerPosition {
	/** 得分后卫 **/
	public static final int SG = 0;
	/** 小前锋 **/
	public static final int SF = 1;
	/** 中锋 **/
	public static final int C = 2;
	/** 控球后卫 **/
	public static final int PG = 3;
	/** 大前锋 **/
	public static final int PF = 4;
    }

    /**
     * 篮球裁判角色
     */
    public final static class BbRefereeAssignment {
	/** 主裁判 **/
	public static final int HEAD_OFFICIAL = 0;
	/** 副裁判 **/
	public static final int OFFICIAL = 1;
    }

    /**
     * 赛事相关信息标志位(如有新加，按照2的幂指数递增)
     */
    public final static class ContestExtFlag {
	/** 有文字直播 */
	public static final int LIVE_WORDS = 1;
    }

    public final static class Content {
	/**
	 * 内容最大长度
	 */
	public static final int MAX_CONTENT_LENGTH = 500;

	/**
	 * 评论最大长度
	 */
	public static final int MAX_COMMENT_LENGTH = 256;

	/**
	 * 单张照片最大大小限制（单位m）
	 */
	public static final int MAX_PHOTO_SIZE = 10;

	/**
	 * 单个内容中最多包含的照片数量
	 */
	public static final int MAX_CONTENT_PHOTO_NUM = 3;

	/**
	 * 单个内容中最多包含的音频数量
	 */
	public static final int MAX_CONTENT_AUDIO_NUM = 1;

	/**
	 * 内容可以正常访问
	 */
	public static final int DATA_NORMAL = 0;

	/**
	 * 内容被删除
	 */
	public static final int DATA_DELETE = 1;

	/**
	 * 内容被屏蔽
	 */
	public static final int DATA_BLOCK = 2;
    }

    /**
     * 足球比分类型
     * 
     * @author Lifeix
     * 
     */
    public final static class FbScoreType {
	/**
	 * 当前实时比分
	 */
	public static final int CURRENT = 0;
	/**
	 * 90分钟常规时间比分
	 */
	public static final int NORMAL_TIME = 1;
	/**
	 * 上半场比分
	 */
	public static final int PERIOD1 = 2;
	/**
	 * 加时赛比分
	 */
	public static final int OVERTIME = 3;
	/**
	 * 点球大战比分
	 */
	public static final int PENALTIES = 4;
    }

    public final static class FriendType {
	/**
	 * 下单
	 */
	public static final int CONTEST = 1;

	/**
	 * 下单理由
	 */
	public static final int REASON = 2;

	/**
	 * 吐槽
	 */
	public static final int TUCAO = 3;
    }

    /**
     * 篮球节次类型
     * 
     * @author Lifeix
     * 
     */
    public final static class BbPeriodType {
	/**
	 * 常规时间
	 */
	public static final int NORMAL_TIME = 0;
	/**
	 * 加时赛
	 */
	public static final int OVER_TIME = 1;
    }

    /**
     * 队伍主客区分
     * 
     * @author Lifeix
     * 
     */
    public final static class TeamDiffer {
	/**
	 * 主队
	 */
	public static final int HOME = 1;
	/**
	 * 客队
	 */
	public static final int AWAY = 2;
    }

    /**
     * 模板类型
     * 
     * @author Lifeix
     * 
     */
    public static final class TempletType {
	/** 内容相关 **/
	public static final int CONTENT = 1;
	/** 比赛相关 **/
	public static final int CONTEST = 2;
	/** 成就相关 **/
	public static final int ACHIEVE = 3;
	/** 关注相关 **/
	public static final int RELATIONSHP = 4;
	/** 连续登录 **/
	public static final int CONTINUELOGIN = 5;
	/** 后台龙筹充值 **/
	public static final int ROIL_RECHARGE = 6;
    }

    public static final class TempletId {
	/** 评论和转发 **/
	public static final long CONTENT = 1;
	/** 竞猜结果 **/
	public static final long BET_RESULT = 2;
	/** 成就 **/
	public static final long ACHIEVE = 3;
	/** 押押竞猜结果 **/
	public static final long YY_BET_RESULT = 4;
	/** pk结果 **/
	public static final long PK_BET_RESULT = 5;
	/** 锦标赛月冠军 **/
	public static final long GAME_MONTHLY_CHAMPION = 6;
	/** 锦标赛年冠军 **/
	public static final long GAME_YEARLY_CHAMPION = 7;
	/** 关注 **/
	public static final long USER_RELATION = 8;
	/** 连续登录奖励 **/
	public static final long CONTINUE_LOGIN_REWARD = 9;
	/** 后台龙筹充值 **/
	public static final long SYSTEM_ROIL_RECHARGE = 10;
	/** PK **/
	public static final long USER_PK = 11;
	/** 注册用户赠送龙筹 **/
	public static final long SYSTEM_ROIL_PRIZE = 12;
	/** PK 接收 **/
	public static final long USER_PK_ACCEPT = 14;
	/** 用户首胜，获取龙筹奖励 **/
	public static final long SYSTEM_FIRST_WIN_PRIZE = 13;

	/** AT **/
	public static final long USER_AT = 15;

	/** 大赢家4.0上线转换龙筹消息 **/
	public static final long UPGRADE_4 = 16;

	/** 龙筹券失效消息提醒 **/
	public static final long COUPON_INVALID = 17;

	/** 后台系统提示 **/
	public static final long SYSTEM_PROMPT = 18;
    }

    /**
     * 内容交互动作类型
     * 
     * @author Lifeix
     * 
     */
    public static final class ContentActionType {
	/** 转发 **/
	public static final int REBLOG = 0;
	/** 回复 **/
	public static final int COMMENT = 1;
    }

    public static final class CommentAtStatus {
	/** 未读 **/
	public static final int NOT_DO = 0;
	/** 已读 **/
	public static final int DO = 1;
    }

    /**
     * 内容类型
     * 
     * @author Lifeix
     * 
     */
    public static final class ContentType {
	/** 正文 **/
	public static final int CONTENT = 0;
	/** 转发 **/
	public static final int REBLOG = 1;
	/** 回复 **/
	public static final int COMMENT = 2;
    }

    /**
     * 排序
     */
    public static final class ContestOrderType {
	/** 按赛事ID降序 **/
	public static final int CID_DESC = 1;
	/** 按赔率ID降序 **/
	public static final int OID_DESC = 2;
    }

    /**
     * 结算状态
     */
    public static final class SettleStatus {

	/** 初始化 **/
	public static final int INIT = 0;

	/** 成功 **/
	public static final int SUCCESS = 1;

	/** 失败 **/
	public static final int FAILED = 2;
    }

    /**
     * 交手记录胜负或赢盘种类
     */
    public static final class ContestRecordWinTypes {
	/** 初始值 **/
	public static final int INIT = 0;
	/** 主胜 **/
	public static final int HOME_WIN = 1;
	/** 平局 **/
	public static final int DRAW = 2;
	/** 客胜 **/
	public static final int AWAY_WIN = 3;
	/** 竞彩主赢盘 **/
	public static final int JC_HOME_WIN = 4;
	/** 竞彩走盘 **/
	public static final int JC_DRAW = 5;
	/** 竞彩客赢盘 **/
	public static final int JC_AWAY_WIN = 6;
    }

    /**
     * 下单客户端来源
     * 
     * @author lifeix
     * 
     */
    public static final class BetClientSource {

	public static final int WEB = 0;

	public static final int IOS = 1;

	public static final int ANDROID = 2;

	public static final int WEIXIN = 3;
    }

    /**
     * 赛事比分模式
     * 
     * @author huijiem
     * 
     */
    public static final class ScoreModule {
	/** 实时比分 **/
	public static final int LIVE_SCORE = 0;
	/** 文字直播 **/
	public static final int LIVE_WORDS = 1;
    }

    /**
     * 串的状态
     */
    public static final class BunchContestListStatus {
	/** 可下注列表 **/
	public static final int NEW = 0;
	/** 往期 **/
	public static final int OLD = -1;
    }

    /**
     * 串的状态
     */
    public static final class BunchContestStatus {
	/** 删除 **/
	public static final int EDIT = -10;
	/** 初始可下注 **/
	public static final int INIT = 0;
	/** 进行中 **/
	public static final int WORKING = 1;
	/** 已有全部结果但未统计 **/
	public static final int SETTELING = 2;
	/** 统计完用户，但未给用户派奖 **/
	public static final int STATISTICS = 3;
	/** 统计完未获奖用户，剩余需要派奖的用户下注记录 **/
	public static final int REWARDING = 4;
	/** 已结算 **/
	public static final int SETTELED = -1;
    }

    /**
     * 串下注的状态
     */
    public static final class BunchBetStatus {
	/** 初始 **/
	public static final int INIT = -1;
	/** 已统计 **/
	public static final int STATISTICS = 0;
	/** 可能中奖 **/
	public static final int MAYBE_REWARD = 1;
	/** 中奖并且已派奖 **/
	public static final int GET_REWARD = 2;
	/** 中奖但未获得派奖 **/
	public static final int NOT_GET_REWARD = 3;
	/** 未中奖 **/
	public static final int NOT_REWARD = 4;
    }

    public static final class BunchPrizeType {
	/** 筹码 **/
	public static final int GOLD = 0;
	/** 龙币 **/
	public static final int LONGBI = 1;
	/** 实物 **/
	public static final int KIND = 2;
    }

}
