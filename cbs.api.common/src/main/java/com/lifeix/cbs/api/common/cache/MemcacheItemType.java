package com.lifeix.cbs.api.common.cache;

public enum MemcacheItemType {

    /**
     * 心跳检测
     */
    HEALTH_CHECK("v2_health_check_", "lifeix", 1, "健康检测"),

    AUTHEN_LOGIN("bed_authen_login", "account_id", 7200, "自定义登录缓存"),

    TICKET_LOGIN("bed_ticket_login", "account_id", 3600, "ticket自定义登录缓存"),

    USERLASTPUBLISHACTION("bed_user_last_publish_action", "conn_id", 3600, "用户最近发表的内容"),

    UNCHECKED_PRESENT_COUNT("bed_unchecked_present_count", "account_id", 7200, "用户未查看的礼品数量"),

    ACCOUNT_SIGN_INFO("account_sign_info", "account_id", 7200, "用户签到信息"),

    ACCOUNT_LOGIN_INFO("account_login_info", "account_id", 7200, "用户连续登录信息"),

    ITEM_LIST("item_list", "id", 7200, "道具列表"),

    ITEM("item", "item_id", 7200, "单个道具"),

    ITEM_SHOP("item_shop", "shop_id", 7200, "道具商城"),

    ITEM_SHOP_LIST("item_shop_list", "id", 7200, "道具商城列表"),

    CONTENT("content", "dashboard_id", 7200, "内容缓存"),

    CHARM_INC_RANKS("charm_inc_leaderboards", "cir_key", 86400, "魅力上升榜缓存"),

    CHARM_INC_RANKS_LOCKER("charm_inc_leaderboards_locker", "cir_locker_id", 60, "魅力上升榜写入锁"),

    CHARM_INC_RANKS_VERSION("charm_inc_ranks_version", "cir_version", 86400, "魅力上升榜数据版本"),

    TUHAO_RANKS("tuhao_leaderboards", "tr_key", 86400, "土豪榜缓存"),

    TUHAO_RANKS_LOCKER("tuhao_leaderboards_locker", "tr_locker_id", 60, "土豪榜写入锁"),

    TUHAO_RANKS_VERSION("tuhao_ranks_version", "tr_version", 86400, "土豪榜数据版本"),

    REC_LIST("rec_user_list", "rec_key", 21600, "推荐榜缓存"), REC_NEW_LIST("rec_new_user_list", "rec_key", 21600, "推荐榜缓存"),

    REC_LIST_LOCKER("rec_user_list_locker", "rec_locker_key", 60, "推荐榜请求锁"), REC_NEWLIST_LOCKER("recNew_user_list_locker",
	    "rec_locker_key", 60, "推荐榜请求锁"),

    REC_TYPE_LIST("rec_type_list", "id", 86400, "推荐类型列表"), REC_NEW_TYPE_LIST("rec_new_type_list", "id", 86400, "推荐类型列表"),

    REC_TYPE_LIST_LOCKER("rec_type_list_locker", "id", 60, "推荐类型列表写入锁"),

    REC_NEW_TYPE_LIST_LOCKER("recnew_type_list_locker", "id", 60, "推荐类型列表写入锁"),

    CONTENT_GUIDE_TYPE("guide_type", "all", 0, "导读内容分类"),

    CONTENT_GUIDE_CHOICE("guide_choice", "all", 3600, "精选首页缓存"),

    CONTENT_GUIDE_CHOICE_LOCKER("guide_choice_locker", "all", 60, "精选首页缓存加载锁"),

    CONTENT_GUIDE_TYPE_PAGE("guide_type_content_load", "id", 60, "导读分类下内容缓存"),

    CONTENT_GUIDE_TYPE_LOCKER("guide_type_content_loacker", "id", 60, "导读分类下内容缓存锁"),

    CONTENT_SECRET("content_secret", "id", 1200, "秘密列表缓存"),

    CONTENT_SECRET_LOCKER("content_secret_locker", "id", 60, "秘密列表缓存锁"),

    FIND_USER("find_user", "id", 3600, "找人缓存"),

    FIND_USER_LOCKER("find_user_locker", "id", 60, "找人缓存锁"),

    USER_CHARM_RANK("user_charm_rank", "account_id", 86400, "用户魅力上升榜排名"),

    USER_CHARM_RANK_LOCKER("user_charm_rank_locker", "account_id", 60, "用户魅力上升榜排名写入锁"),

    USER_CHARM_RANK_VALUE("user_charm_rank_value", "account_id", 43200, "用户魅力上升榜魅力值及排名"),

    USER_CHARM_RANK_VALUE_LOCKER("user_charm_rank_value_locker", "account_id", 60, "用户魅力上升榜魅力值及排名写入锁"),

    CREATE_IM_LOCKER("im_locker", "id", 60, "创建环信帐号锁"),

    ACCOUNT_ROLE_LIST("account_role_list", "id", 86400, "用户角色列表"),

    ACCOUNT_ROLE_LIST_LOCKER("account_role_list_locker", "id", 60, "用户角色列表写入锁"),

    PRESENT_LIST("present_list", "id", 86400, "礼物列表"),

    ACTIVITY_LIMIT_LIST("activity_limit_list", "id", 86400, "限时活动列表"),

    PRESENT_LIST_LOCKER("present_list_locker", "id", 60, "礼物列表写入锁"),

    ADVERT_LIST("advert_list", "type", 86400, "广告列表缓存"),

    NOTIFY_UNREAD("notify_unread", "account_id", 120, "未读消息缓存"),

    TUHAO_EVERYDAY("task_tuhao_everyday", "account_id", 24 * 60 * 60, "天天土豪"),

    BAD_BEHAVIOR_REPORT("task_bad_behavior_report", "account_id", 24 * 60 * 60, "举报恶意行为"),

    CONTENT_HOT_EVERYDAY("task_content_hot_everyday", "account_id", 24 * 60 * 60, "每日热帖"),

    LOTTERY_PRIZE_ITEMS("lottery_prize_items", "id", 24 * 60 * 60, "抽奖奖项"),

    CONTENT_GUIDE_EVERYDAY("task_content_guide_everyday", "account_id", 24 * 60 * 60, "每日精选"),

    SELF_TIMER("task_self_timer", "account_id", 24 * 60 * 60, "自拍进入秀场"),

    CHARM_STATISTICS("charm_statistics", "account_id", 24 * 60 * 60, "魅力值统计对象"),

    PRIZE_LIMIT_EVERYDAY("prize_limit_everyday", "lottery_id", 24 * 60 * 60, "奖品中奖次数限制"),

    RED_PACKAGE_LIST("red_package_list", "red_key", 21600, "红包获取充值列表"),

    RED_PACKAGE_LOCKER("red_package_locker", "rec_locker_key", 60, "红包获取充值列表"),

    VIP_EXPIRE_LOCKER("vip_expire_locker", "id", 60, "修改vip过期信息锁"),

    LOTTERY_TIMES("lottery_times_everyday", "account_id", 24 * 60 * 60, "每日抽奖次数"),

    LOTTERY_LATEST_LOGS("lottery_latest_logs", "id", 12 * 60 * 60, "最新中奖名单");

    private final String keyPrefix;

    private final String primaryKey;

    private final int expire;

    private final String description;

    public String getDescription() {
	return description;
    }

    MemcacheItemType(String keyPrefix, String primaryKey, int expire, String decription) {
	this.keyPrefix = keyPrefix;
	this.primaryKey = primaryKey;
	this.expire = expire;
	this.description = decription;
    }

    public String getKeyPrefix() {
	return keyPrefix;
    }

    public String getPrimaryKey() {
	return primaryKey;
    }

    public int getExpire() {
	return expire;
    }

}
