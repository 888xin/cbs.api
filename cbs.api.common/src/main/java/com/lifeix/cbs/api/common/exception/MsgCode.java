package com.lifeix.cbs.api.common.exception;

/**
 * 大赢家全局信息返回定义
 * 
 * @author lifeix
 * 
 */
public class MsgCode {

    /**
     * 基本信息
     */
    public static interface BasicMsg {

	// 服务器异常
	String CODE_BASIC_SERVCER = "10001";
	String KEY_BASIC_SERVCER = "error.basic.serviceerror";

	// IP被限制
	String CODE_IPBLOCK = "10005";
	String KEY_IPBLOCK = "error.account.ipblock";

	// 参数错误
	String CODE_PARAMEMETER = "10006";
	String KEY_PARAMEMETER = "error.basic.illegalparamemeter";

	// 账号验证失败
	String CODE_AUTHORIZATIONFAIL = "10007";
	String KEY_AUTHORIZATIONFAIL = "error.basic.authorizationfail";

	// 用户服务不可用
	String CODE_USERSERVER = "10010";
	String KEY_USERSERVER = "error.basic.userserverunavailable";

	// 龙筹服务不可用
	String CODE_GOLDSERVER = "10015";
	String KEY_GOLDSERVER = "error.basic.goldserverunavailable";

	// 龙币服务不可用
	String CODE_MONEYSERVER = "10017";
	String KEY_MONEYSERVER = "error.basic.moneyserverunavailable";

	// 龙筹余额不足
	String CODE_NOT_GOLD = "10019";
	String KEY_NOT_GOLD = "error.basic.notgold";

	// 龙币余额不足
	String CODE_NOT_MONEY = "10020";
	String KEY_NOT_MONEY = "error.basic.notmoney";

	// 操作失败
	String CODE_OPERATE_FAIL = "10030";
	String KEY_OPERATE_FAIL = "error.basic.operatefail";

	// 客户端版本过低
	String CODE_BASIC_VERSIONLOW = "10040";
	String KEY_BASIC_VERSIONLOW = "error.basic.versiontoolow";

	// dubbo服务异常
	String CODE_USER_DUBBO = "16204";
	String KEY_USER_DUBBO = "error.user.dubbo.rpc";

	// 微信异常
	String CODE_WX_ERROR = "10031";
	String KEY_WX_ERROR = "error.basic.wxerror";

    }

    public static interface QiniuMsg {

	public static String CODE_REDIS = "81003";
	public static String KEY_REDIS = "qiniu.redis.exception";

    }

    /**
     * 广播异常
     * 
     * @author lifeix
     * 
     */
    public static interface BroadcastMsg {

	// 账户不存在
	String CODE_Broadcast_NOT_PRICE = "71002";
	String KEY_Broadcast_NOT_PRICE = "error.Broadcastpricenotfound";

    }

    public static interface UserTokenMsg {

	// token不存在 失效了，可以重新拿token
	String CODE_TOKEN_NOT_EXSIST = "12001";
	String KEY_TOKEN_NOT_EXSIST = "error.token.not.exist";

	// token错误
	String CODE_TOKEN_ERROR = "12002";
	String KEY_TOKEN_ERROR = "error.token.error";

	// token不能为空
	String CODE_TOKEN_CAN_NOT_ENPTY = "12003";
	String KEY_TOKEN_CAN_NOT_ENPTY = "error.token.can.not.empty";
    }

    /**
     * 用户信息
     * 
     * @author lifeix
     * 
     */
    public static interface UserMsg {

	// 账户不存在
	String CODE_USER_ACCOUNT_NOT_FOUND = "11002";
	String KEY_USER_ACCOUNT_NOT_FOUND = "error.user.accountnotfound";

	// 账户被加入黑名单
	String CODE_USER_ACCOUNT_BLOCK = "11004";
	String KEY_USER_ACCOUNT_BLOCK = "error.user.accountisblock";

	// 手机号错误
	String CODE_USER_MOBILEERROR = "11013";
	String KEY_USER_MOBILEERROR = "error.account.mobileerror";
    }

    /**
     * 内容信息
     * 
     * @author lifeix
     * 
     */
    public static interface ContentMsg {

	// 内容包含敏感信息
	String CODE_CONTENT_SENSITIVE = "12002";
	String KEY_CONTENT_SENSITIVE = "error.basic.sensitive";

	// 发表失败
	String CODE_CONTENT_POST_FAIL = "12004";
	String KEY_CONTENT_POST_FAIL = "error.content.postfail";

	// 内容不存在或已删除
	String CODE_CONTENT_NOT_FOUND = "12007";
	String KEY_CONTENT_NOT_FOUND = "error.content.notfound";

	// 心情不可更改
	String CODE_CONTENT_MOOD_EDIT = "12008";
	String KEY_CONTENT_MOOD_EDIT = "error.content.moodcantedit";

	// 内容存在
	String CODE_CONTENT_FOUND = "12009";
	String KEY_CONTENT_FOUND = "error.content.found";

	// 照片上传失败
	String CODE_CONTENT_PHOTO_NOT_FOUND = "12201";
	String KEY_CONTENT_PHOTO_NOT_FOUND = "error.content.photonotfound";

	// 内容为空
	String CODE_CONTENT_TEXT_EMPTY = "12204";
	String KEY_CONTENT_TEXT_EMPTY = "error.content.textnotfound";

    }

    /**
     * 下单信息
     * 
     * @author lifeix
     * 
     */
    public static interface BetMsg {

	// 下单失败
	String CODE_BET_FAIL = "13001";
	String KEY_BET_FAIL = "error.bet.fail";

	// 不支持的玩法类型
	String CODE_BET_NOT_SUPPORT = "13002";
	String KEY_BET_NOT_SUPPORT = "error.bet.notsupportplaytype";

	// 下单金额超过上限
	String CODE_BET_UP_LIMIT = "13003";
	String KEY_BET_UP_LIMIT = "error.bet.moneyupperlimit";

	// 押押下单金额不能
	String CODE_BET_YY_LIMIT = "13013";
	String KEY_BET_YY_LIMIT = "error.bet.yayabetlimit";

	// 下单金额不足（至少0.1）
	String CODE_BET_DOWN_LIMIT = "13004";
	String KEY_BET_DOWN_LIMIT = "error.bet.atleast1";

	// 已下过注
	String CODE_BET_HAS = "13005";
	String KEY_BET_HAS = "error.bet.hasbet";

	// 比赛停止下单
	String CODE_BET_CANT = "13006";
	String KEY_BET_CANT = "error.bet.cantbet";

	// 赔率发生改变
	String CODE_BET_ODDS_CHANGE = "13007";
	String KEY_BET_ODDS_CHANGE = "error.bet.changed";

	// 赛事只允许龙币下单
	String CODE_BET_LONGBI_LOCK = "13008";
	String KEY_BET_LONGBI_LOCK = "error.bet.longbilock";

	// 已开赛，停止下单
	String CODE_BET_CONTEST_BEGIN = "13009";
	String KEY_BET_CONTEST_BEGIN = "error.bet.contestbegin";

	// 已封盘，停止下单
	String CODE_BET_ODDS_CLOSE = "13010";
	String KEY_BET_ODDS_CLOSE = "error.odds.oddsclosed";

	// 混合下单只允许下单一方
	String CODE_BET_MIX_REPEAT = "13011";
	String KEY_BET_MIX_REPEAT = "error.bet.mixrepeat";

	// 混合下单赔率必须大于1.7
	String CODE_BET_MIX_LIMIT = "13012";
	String KEY_BET_MIX_LIMIT = "error.bet.mixlimit";

	// 可选择的比赛数目不符合
	String CODE_BET_NUM_NOT_SUPPORT = "13013";
	String KEY_BET_NUM_NOT_SUPPORT = "error.bet.not.support.num";

	// 正在结算
	String CODE_BET_SETTLING = "13014";
	String KEY_BET_SETTLING = "error.bet.settling";
    }

    public static interface CouponMsg {

	// 龙筹券不存在
	String CODE_COUPON_FAIL = "53001";
	String KEY_COUPON_FAIL = "error.coupon.notexists";

	// 龙筹券已经使用
	String CODE_COUPON_USED = "53002";
	String KEY_COUPON_USED = "error.coupon.used";

	// 龙筹券过期
	String CODE_COUPON_PASS = "53003";
	String KEY_COUPON_PASS = "error.coupon.pass";

	// 龙筹券使用范围错误
	String CODE_COUPON_FAIL_NOT_RANGE = "53004";
	String KEY_COUPON_FAIL_NOT_RANGE = "error.coupon.notrange";

	// 龙筹券已领取过
	String CODE_COUPON_RECEIVED = "53006";
	String KEY_COUPON_RECEIVED = "error.coupon.received";

	// 龙筹券库存不足
	String CODE_COUPON_NUM = "53007";
	String KEY_COUPON_NUM = "error.coupon.inventory";

	// 龙筹券对应龙币比例错误
	String CODE_COUPON_PROPORTION = "53008";
	String KEY_COUPON_PROPORTION = "error.coupon.proportion";

	// 今天的筹码已领取
	String CODE_COUPON_LOGIN = "53009";
	String KEY_COUPON_LOGIN_HAS = "error.coupon.login.has";

	// 龙筹券使用范围错误
	String CODE_COUPON_100_NOT_RANGE = "53010";
	String KEY_COUPON_100_NOT_RANGE = "error.coupon.hundrednotalone";
    }

    /**
     * 赛事信息
     * 
     * @author lifeix
     * 
     */
    public static interface ContestMsg {

	// 赛事不存在
	String CODE_CONTEST_NOT_EXIST = "14001";
	String KEY_CONTEST_NOT_EXIST = "error.contest.notexist";

	// 赛事未结束
	String CODE_CONTEST_NOT_END = "14002";
	String KEY_CONTEST_NOT_END = "error.contest.notend";

	// 赛事待定中
	String CODE_CONTEST_PEDDING = "14003";
	String KEY_CONTEST_PEDDING = "error.contest.pedding";

	// 赛事已结算
	String CODE_CONTEST_SETTLED = "14005";
	String KEY_CONTEST_SETTLED = "error.contest.settled";

	// 赛事不能删除
	String CODE_CONTEST_CANT_DROP = "14006";
	String KEY_CONTEST_CANT_DROP = "error.contest.cannotdrop";

	// 球队不存在
	String CODE_TEAM_NOT_EXIST = "14101";
	String KEY_TEAM_NOT_EXIST = "error.team.notexist";

	// 球员不存在
	String CODE_PLAYER_NOT_EXIST = "14102";
	String KEY_PLAYER_NOT_EXIST = "error.player.notexist";

	// 类型已存在
	String CODE_TYPE_EXIST = "14103";
	String KEY_TYPE_EXIST = "error.type.exist";

	// 该赛事类型没有对应cupId
	String CODE_TYPE_EXIST_CUPID = "14104";
	String KEY_TYPE_EXIST_CUPID = "error.type.notexist.cupid";
    }

    /**
     * 商品
     * 
     * @author lhx
     * 
     */
    public static interface GoodsMsg {

	// 商品售罄
	String CODE_GOODS_SELLOUT = "15001";
	String GOODS_SELLOUT = "error.goods.sell.out";

	// 购买商品成功
	String CODE_GOODS_BUY_SUCCESS = "15002";
	String GOODS_BUY_SUCCESS = "error.goods.buy.success";

	// 商品下架或不存在
	String CODE_GOODS_NO_FOUND = "15003";
	String GOODS_NO_FOUND = "error.goods.not.found";

	// 购买商品失败
	String CODE_GOODS_BUY_FAIL = "15004";
	String GOODS_BUY_FAIL = "error.goods.buy.fail";

	// 商品热销中，稍等
	String CODE_GOODS_BUY_BUSY = "15005";
	String GOODS_BUY_BUSY = "error.goods.buy.busy";

	// 购买商品日志写入数据库失败
	String CODE_GOODSLOG_INSERT_FAIL = "15006";
	String GOODSLOG_INSERT_FAIL = "error.goodslog.insert.fail";

	// 积分商城相关错误码
	String CODE_MALL_ADDRESS_SAVE_FAILED = "31001";
	String MALL_ADDRESS_SAVE_FAILED = "error.mall.address.save.failed";

	String CODE_MALL_ITEM_SAVE_FAILED = "31002";
	String MALL_ITEM_SAVE_FAILED = "error.mall.item.save.failed";

	String CODE_MALL_ITEM_NOTEXIST_FAILED = "31003";
	String MALL_ITEM_NOTEXIST_FAILED = "error.mall.item.not.exist";

	String CODE_MALL_ITEM_PRICE_ILLEGAL = "31004";
	String MALL_ITEM_PRICE_ILLEGAL = "error.mall.item.price.illegal";

	// 商品添加数据库失败
	String CODE_GOODS_INSERT_FAIL = "15007";
	String GOODS_INSERT_FAIL = "error.goods.insert.fail";

	// 商品修改数据库失败
	String CODE_GOODS_UPDATE_FAIL = "15008";
	String GOODS_UPDATE_FAIL = "error.goods.update.fail";

	String CODE_GOODS_BET_NOT_EXISTS = "15101";
	String GOODS_BET_NOT_EXISTS = "error.goods.bet.notexists";

	String CODE_GOODS_BET_UNFINISHED = "15102";
	String GOODS_BET_UNFINISHED = "error.goods.bet.unfinished";

	String CODE_GOODS_BET_INVITECODEINVALID = "15103";
	String GOODS_BET_INVITECODEINVALID = "error.goods.bet.invitecodeinvalid";

	String CODE_GOODS_BET_DISCOUNTING = "15104";
	String GOODS_BET_DISCOUNTING = "error.goods.bet.discounting";

	String CODE_GOODS_BET_CANCELLIMIT = "15105";
	String GOODS_BET_CANCELLIMIT = "error.goods.bet.cancellimit";
    }

    /**
     * 消息提醒
     * 
     * @author lifeix
     * 
     */
    public static interface MessageMsg {

	// 模板不存在
	String CODE_MESSAGE_TEMPLET_EMPTY = "16001";
	String KEY_MESSAGE_TEMPLET_EMPTY = "error.notify.templetnotfound";

	// 消息提醒添加失败
	String CODE_MESSAGE_NOTIFY_FAIL = "16002";
	String KEY_MESSAGE_NOTIFY_FAIL = "error.notify.notifyaddfailed";

    }

    /**
     * 游戏
     * 
     * @author lifeix
     * 
     */
    public static interface GameMsg {

	// 时彩查看错误
	String CODE_GAME_SHICAI_VIEW_FAILED = "17005";
	String KEY_GAME_SHICAI_VIEW_FAILED = "error.game.shicaiviewfailed";

	// 时彩开始错误
	String CODE_GAME_SHICAI_PLAY_FAILED = "17006";
	String KEY_GAME_SHICAI_PLAY_FAILED = "error.game.shicaiplayfailed";

	// 时彩已开奖无法下单
	String CODE_GAME_SHICAI_OPEN_NOT_PLAY = "17007";
	String KEY_GAME_SHICAI_OPEN_NOT_PLAY = "error.game.shicaiopennotplay";
    }

    /**
     * 生肖游戏
     */
    public static interface ZodiacMsg {

	// 本期游戏已结束，停止下单
	String CODE_GAME_ZODIAC_OPEN_NOT_PLAY = "17008";
	String KEY_GAME_ZODIAC_OPEN_NOT_PLAY = "error.game.zodiacopennotplay";
	// 本期游戏已结算
	String CODE_GAME_ZODIAC_SETTLE = "17009";
	String KEY_GAME_ZODIAC_SETTLE = "error.game.zodiaalreadysettle";
	// 本期游戏未开始，不能下单
	String CODE_GAME_ZODIAC_NOT_OPEN = "17009";
	String KEY_GAME_ZODIAC_NOT_OPEN = "error.game.zodiacnotopen";
    }

    /**
     * 猜友圈评论
     * 
     * @author jacky
     * 
     */
    public static interface CircleCommMsg {

	// 发表评论失败
	String CODE_POST_COMMEND_FAILED = "18001";
	String KEY_POST_COMMEND_FAILED = "error.circle.commendfailed";

	// 获取评论列表失败
	String CODE_COMMEND_LIST_FAILED = "18002";
	String KEY_COMMEND_LIST_FAILED = "error.circle.commendlistfailed";

	// 获取未读评论列表
	String CODE_UNREAD_LIST_FAILED = "18003";
	String KEY_UNREAD_LIST_FAILED = "error.circle.unreadlistfailed";

	// 获取历史评论列表
	String CODE_HISTORY_LIST_FAILED = "18004";
	String KEY_HISTORY_LIST_FAILED = "error.circle.historylistfailed";

    }

    public static interface PkMsg {
	String CODE_PK_OUTERACCEPTDUPLICATE = "19001";
	String KEY_PK_OUTERACCEPTDUPLICATE = "error.pk.outeracceptduplicate";

	String CODE_PK_NOT_USER = "19002";
	String KEY_PK_NOT_USER = "error.pk.user";

    }

    public static interface ExpressMsg {
	String CODE_EXPRESS_NOTFOUNT = "20001";
	String KEY_EXPRESS_NOTFOUNT = "error.express.expressnotfound";

	String CODE_EXPRESS_SUBSCIBEFAIL = "20002";
	String KEY_EXPRESS_SUBSCIBEFAIL = "error.express.subscibefail";
    }

    public static interface OrderMsg {
	// 订单已存在
	String CODE_ORDER_EXIST = "40001";
	String KEY_ORDER_EXIST = "error.order.exist";

	// 收货地址错误
	String CODE_ORDER_ERROR_ADDRESS = "40002";
	String KEY_ORDER_ERROR_ADDRESS = "error.order.erroraddress";

	// 订单已完成
	String CODE_ORDER_IS_DONE = "40003";
	String KEY_ORDER_IS_DONE = "error.order.isdone";

	// 订单已取消
	String CODE_ORDER_IS_CANCEL = "40004";
	String KEY_ORDER_IS_CANCEL = "error.order.iscancel";

	// 订单未支付
	String CODE_ORDER_NOT_PAY = "40005";
	String KEY_ORDER_NOT_PAY = "error.order.notpay";

	// 用户收货地址超过9个
	String CODE_ORDER_TO_MANY_ADDRESS = "40006";
	String KEY_ORDER_TO_MANY_ADDRESS = "error.order.tomanyaddress";

	// 快递类型错误
	String CODE_ORDER_ERROR_TYPE = "40007";
	String KEY_ORDER_ERROR_TYPE = "error.order.errortype";

	// 订单不存在
	String CODE_ORDER_NOT_FOUND = "40008";
	String KEY_ORDER_NOT_FOUND = "error.order.notfound";

    }

    public static interface ActivityMsg {
	// 首充活动记录不存在
	String CODE_FIRST_NOT_FOUND = "40009";
	String KEY_FIRST_NOT_FOUND = "error.first.notfound";

	// 首充活动已无机会参加
	String CODE_FIRST_NO_CHANCE = "40010";
	String KEY_FIRST_NO_CHANCE = "error.first.nochance";

	// 活动已結束
	String CODE_ACTIVITY_END = "40011";
	String KEY_ACTIVITY_END = "error.activity.end";
    }

    public static interface ScoreModuleMsg {
	String CODE_SCORE_MODULE_NOT_FOUND = "50001";
	String KEY_SCORE_MODULE_NOT_FOUND = "error.scoremodule.notfound";
    }

    public static interface MissionMsg {
	// 积分不够
	String CODE_POINT_NOT_ENOUGH = "60001";
	String KEY_POINT_NOT_ENOUGH = "error.point.not.enough";

	// 积分任务不存在
	String CODE_MISSION_NOT_FOUND = "60002";
	String KEY_MISSION_NOT_FOUND = "error.mission.not.found";

	// 积分任务取消
	String CODE_MISSION_CANCEL = "60003";
	String KEY_MISSION_CANCEL = "error.mission.cancel";

	// 积分任务已完成过
	String CODE_MISSION_FINISH = "60005";
	String KEY_MISSION_FINISH = "error.mission.finish";

    }
}
