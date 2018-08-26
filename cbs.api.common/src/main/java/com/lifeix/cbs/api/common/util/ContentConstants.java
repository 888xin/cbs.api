package com.lifeix.cbs.api.common.util;

import java.util.HashMap;
import java.util.Map;

public class ContentConstants {

    /**
     * 
     * @author lifeix
     * 
     */
    public static final class PkMainStatus {
	/**
	 * 心情
	 */
	public static final int CREATE = 0;

	/**
	 * 竟猜
	 */
	public static final int ONE = 1;

	/**
	 * 竟猜
	 */
	public static final int TWO = 2;

	/**
	 * 结算
	 */
	public static final int SETTLE = 3;

	/**
	 * 走盘
	 */
	public static final int ZOUPAN = 4;
    }

    public static final class MsgType {
	/**
	 * 通知
	 */
	public static final int NOTITY = 1;

	/**
	 * pk消息
	 */
	public static final int PK = 2;

	/**
	 * 关注消息
	 */
	public static final int FOLLOW = 3;

	/**
	 * 社区消息
	 */
	public static final int SEQU = 4;

    }

    public static final class PkBetStatus {
	/**
	 * CHUSHI
	 */
	public static final int CREATE = 0;

	/**
	 * 接受pk
	 */
	public static final int ACCEPT = 1;

	/**
	 * YING
	 */
	public static final int WIN = 2;

	/**
	 * SHU
	 */
	public static final int LOSS = 3;

	/**
	 * 走盘
	 */
	public static final int ZOUPAN = 4;

	/**
	 * QUXIAO
	 */
	public static final int CANCLE = 5;

    }

    /**
     * 发表内容类型
     */
    public static final class PostType {
	/**
	 * 心情
	 */
	public static final int MOOD = 0;

	/**
	 * 竟猜
	 */
	public static final int GUESS = 1;

	/**
	 * pk
	 */
	public static final int PK = 2;

	/**
	 * 押押
	 */
	public static final int YY = 3;

	/**
	 * 过滤类型
	 * 
	 * @param type
	 * @return
	 */
	public static int filtType(int type) {
	    switch (type) {
	    case GUESS:
		return GUESS;
	    case MOOD:
		return MOOD;
	    case PK:
		return PK;
	    case YY:
		return YY;
	    default:
		return MOOD;
	    }
	}
    }

    public static final class BroadcastPrice {
	public static Map<Integer, Double> longchouMap = new HashMap<Integer, Double>();
	public static Map<Integer, Double> longbiMap = new HashMap<Integer, Double>();

	public static Map<Integer, Double> normalMap = new HashMap<Integer, Double>();
	public static Map<Integer, Double> topMap = new HashMap<Integer, Double>();

	static {
	    longchouMap.put(1, 500D);
	    longbiMap.put(11, 5D);

	    normalMap.put(Broadcast.TYPE_USER_NORMAL, 100D);
	    topMap.put(Broadcast.TYPE_USER_TOP, 2D);
	}
    }

    /**
     * 发表的爆料子类型
     * 
     * @author pengkw
     */
    public static final class PostMoodType {

	/**
	 * 正常发表的爆料
	 */
	public static final int DEFAULT = 0;

	/**
	 * 成就分享后的爆料类型
	 */
	public static final int ACHIEVE = 1;

	/**
	 * 转发
	 */
	public static final int REBLOG = 2;

	/**
	 * 过滤类型
	 * 
	 * @param type
	 * @return
	 */
	public static int filtType(int type) {
	    if (type == ACHIEVE) {
		return ACHIEVE;
	    } else if (type == REBLOG) {
		return REBLOG;
	    }
	    return DEFAULT;
	}
    }

    /**
     * 广播
     * 
     * @author huiy 2015年11月27日下午5:23:54
     */
    public static final class Broadcast {
	public static final int TYPE_ALL = 0; // 全部广播
	public static final int TYPE_SYSTEM = 1; // 系统广播
	public static final int TYPE_USER_NORMAL = 2; // 用户普通广播
	public static final int TYPE_USER_TOP = 3; // 用户置顶广播
	public static final int TYPE_USER = 4; // 我发布的广播
	public static final int TYPE_AT = 5; // @我的广播
	public static final int TYPE_USER_ALL = 6; // 所有用户发的广播

	public static final int TYPE_SYSTEM_RECHARGE = 11; // 系统广播:
	// 用户充值人民币18及18元以上
	public static final int TYPE_SYSTEM_GIVE_PRESENT = 12; // 系统广播:
	// 用户一次性赠送价值1000床币及以上价值的礼物
	public static final int TYPE_SYSTEM_LOTTERY = 13; // 系统广播:
	// 用户幸运大抽奖抽中50000床点／3000床币／一个月VIP
	public static final int TYPE_SYSTEM_BUY_VIP = 14; // 系统广播: 用户购买VIP
	public static final int TYPE_SYSTEM_BLOCK = 15; // 系统广播: 系统禁言／屏蔽的广播
	public static final int TYPE_SYSTEM_CHATROOM_SHARE = 16; // 系统广播:
	// 用户从聊天室分享到广播的文字
	public static final int TYPE_SYSTEM_INNER_REWARD = 17; // 系统广播:
	// 用户参加活动获奖，系统奖励
	public static final int TYPE_SYSTEM_INNER_POST = 18; // 系统广播:
	// 运营人员从后台发布到广播上的文字
	public static final int TYPE_SYSTEM_SHICAI = 19; // 系统广播: 时彩游戏中奖

	public static final double TYPE_SYSTEM_CHATROOM_SHARE_LONGBI = 3; // 聊天室信息分享到广播消耗龙币
	public static final long TYPE_SYSTEM_CHATROOM_SHARE_BEDPOINT = 300; // 聊天室信息分享到广播消耗床点

	public static final double TYPE_USER_NORMAL_LONGBI = 0.5; // 用户发布普通广播消耗龙币
	public static final long TYPE_USER_NORMAL_BEDPOINT = 50; // 用户发布普通广播消耗龙币

	public static final double TYPE_USER_TOP_LONGBI = 5; // 用户发布置顶广播消耗龙币

    }

    public static final class FrontPage {
	// 头版广告区 类型一 用户的吐槽
	public static final int TYPE_AD_REMAKES = -1;
	// 头版广告区 类型二 下单理由
	public static final int TYPE_AD_REASON = -2;
	// 头版广告区 类型三 推荐
	public static final int TYPE_AD_RECOMMEND = -3;
	// 头版广告区 类型三 推荐咨询
	public static final int TYPE_AD_RECOMMEND_ZHIXUN = -4;
	// 头版广告区 类型三 推荐比赛
	public static final int TYPE_AD_RECOMMEND_BISAI = -5;
	// 头版广告区 类型三 推荐网页
	public static final int TYPE_AD_RECOMMEND_WANGYE = -6;
	// 头版广告区
	public static final int TYPE_AD_ALL = -100;

	// 头版内容区 类型一 用户的吐槽
	public static final int TYPE_CONTENT_REMAKES = 1;
	// 头版内容区 类型二 下单理由
	public static final int TYPE_CONTENT_REASON = 2;
	// 头版内容区 类型三 官方消息
	public static final int TYPE_CONTENT_MESSAGE = 3;
	// 头版内容区 类型四 推荐咨询
	public static final int TYPE_CONTENT_RECOMMEND_ZHIXUN = 4;
	// 头版内容区 类型五 推荐比赛
	public static final int TYPE_CONTENT_RECOMMEND_BISAI = 5;
	// 头版内容区 类型六 推荐网页
	public static final int TYPE_CONTENT_RECOMMEND_WANGYE = 6;
	// 头版内容区 类型七 新闻（包括资讯/推荐比赛/推荐网页）
	public static final int TYPE_CONTENT_RECOMMEND_NEWS = 7;
	// 头版内容区
	public static final int TYPE_CONTENT_ALL = 100;
    }

    public static final class FrontPageStatus {
	// 初始
	public static final int INIT = 0;
	// 审核通过
	public static final int PASS = 1;
	// 删除
	public static final int NOPASS = 2;
	// 移出redis队列
	public static final int REMOVE_QUEUE = 7;
	// 排到redis队列中
	public static final int INSERT_QUEUE = 8;
	// 置顶到redis队列中
	public static final int INSERT_TOP = 9;

	public static final int OPER_STATUS = 10;

	public static final int OPER_INFO = 11;

    }

    /**
     * 消息记录初始化添加方式
     * 
     * @author Lifeix
     */
    public static final class NotifyAddType {
	/**
	 * 单条发送 *
	 */
	public static final int SOLO_ADD = 0;

	/**
	 * 批量发送 *
	 */
	public static final int BATCH_ADD = 1;

	/**
	 * 聚合发送 *
	 */
	public static final int POLYMERIZE_ADD = 2;
    }

    /**
     * 消息记录初始化添加方式
     * 
     * @author Lifeix
     */
    public static final class PushType {
	/**
	 * 公告 *
	 */
	public static final int NOTICE_MSG = 1;

	/**
	 * 系统消息 *
	 */
	public static final int SYSTEM_MSG = 2;

    }

    /**
     * 商品状态类型
     */
    public static final class GoodsStatus {
	/**
	 * 上架
	 */
	public static final int UP = 1;

	/**
	 * 下架
	 */
	public static final int DOWN = 0;

	// 移出redis队列
	public static final int REMOVE_QUEUE = 7;
	// 排到redis队列中
	public static final int INSERT_QUEUE = 8;

    }

    /**
     * 商品支付类型
     */
    public static final class PayType {
	/**
	 * 龙筹购买
	 */
	public static final int GOLD = 0;

	/**
	 * 龙币购买
	 */
	public static final int LONGBI = 1;

    }

    /**
     * 商品排序
     */
    public static final class GoodsOrder {
	/**
	 * 创建时间降序
	 */
	public static final int TIME_DESC = 0;

	/**
	 * 创建时间升序
	 */
	public static final int TIME_ASC = 1;

	/**
	 * 销量降序
	 */
	public static final int SALES_DESC = 2;

	/**
	 * 销量升序
	 */
	public static final int SALES_ASC = 3;

	/**
	 * 价格降序
	 */
	public static final int PRICE_DESC = 4;

	/**
	 * 价格升序
	 */
	public static final int PRICE_ASC = 5;

    }

    /**
     * 商品状态类型
     */
    public static final class GoodsType {
	/**
	 * 虚拟(龙币换龙筹)
	 */
	public static final int LONGBI2GOLD = 1;

	/**
	 * 实物
	 */
	public static final int KIND = 0;

    }

    public static final class GoodsBetStatus {
	/**
	 * 取消
	 */
	public static final int CANCELED = -1;
	/**
	 * 已发起
	 */
	public static final int CREATED = 0;
	/**
	 * 已兑换
	 */
	public static final int EXCHANGED = 1;
    }

    /**
     * 参与砍价人数限制
     */
    public static final class GoodsBetHelpLimit {
	/**
	 * 新用户
	 */
	public static final int NEW_GUY = 2;
	/**
	 * 老用户
	 */
	public static final int OLD_GUY = 3;
    }

    /**
     * 奖品竞猜帮忙砍价状态
     */
    public static final class GoodsBetHelpStatus {
	/**
	 * 砍价失败
	 */
	public static final int FAILED = -1;
	/**
	 * 新建
	 */
	public static final int CREATED = 0;
	/**
	 * 砍价成功
	 */
	public static final int SUCCESS = 1;
    }

    /**
     * 举报类型
     * 
     * @author lifeix
     */
    public static final class InformType {

	/**
	 * 评论
	 */
	public static final int COMMENT = 1;

	/**
	 * 吐槽
	 */
	public static final int CONTENT = 2;

	/**
	 * im
	 */
	public static final int IM = 3;

	/**
	 * 新闻评论
	 */
	public static final int NEWS_COMMENT = 4;

	/**
	 * 新闻
	 */
	public static final int NEWS = 5;

	/**
	 * 用户
	 */
	public static final int USER = 10;
    }

    /**
     * 举报理由类型
     * 
     * @author lifeix
     */
    public static final class InformReasonType {
	/**
	 * 淫秽色情
	 */
	public static final int SQ = 1;

	/**
	 * 垃圾广告
	 */
	public static final int GD = 2;

	/**
	 * 谣言
	 */
	public static final int ZM = 3;

	/**
	 * 反动
	 */
	public static final int ZZ = 4;

	/**
	 * 其他
	 */
	public static final int QT = 0;

    }

    /**
     * 举报处理状态
     * 
     * @author lifeix
     */
    public static final class InformStatus {

	/**
	 * 待处理
	 */
	public static final int PENDING = 0;

	/**
	 * 已屏蔽
	 */
	public static final int SHEILDED = 1;

	/**
	 * 已忽略
	 */
	public static final int IGNORED = 2;

	/**
	 * 已禁言
	 */
	public static final int GAG = 3;

	/**
	 * 已被删除
	 */
	public static final int DELETED = -1;
    }

    public static final class ZodiacAnimalStatus {

	/**
	 * 待处理
	 */
	public static final int INIT = 0;

	/**
	 * 已派奖（中奖）已处理
	 */
	public static final int LOTTERY = 1;

	/**
	 * 未中奖
	 */
	public static final int UN_LOTTERY = 2;

    }

}
