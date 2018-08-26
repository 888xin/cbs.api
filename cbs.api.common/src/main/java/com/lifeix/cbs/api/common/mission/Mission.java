package com.lifeix.cbs.api.common.mission;

public enum Mission {

    FIRST_LONGBI(0B0000_0001, "首次充值龙币", 300, 0),

    FIRST_AVATAR(0B0000_0010, "首次上传头像", 200, 0),

    FAN_20(0B0000_0100, "粉丝数达到20人", 200, 0),

    CONCERN_20(0B0000_1000, "关注数达到20人", 200, 0),


    /**
     * 首次任务
     * ==============================
     * 每日任务
     */

    EVERY_DAY_LOGIN(0B0000_0001, "每日登录", 50, 1),

    SHARE_TO_WEIXIN(0B0000_0010, "每日分享内容至微信", 100, 1),

    SEND_RESON(0B0000_0100, "每日发布下单理由", 100, 1),

    COMMENT_UNDER_CONTEST(0B0000_1000, "每日在比赛页面评论", 50, 1),

    COMMENT_UNDER_RESON(0B0001_0000, "每日评论下单理由", 50, 1),



    /**
     * 每日任务
     * ==============================
     * 床上任务
     */

    NYX_LOGIN(0B0000_0001, "每日登录", 0, 3),

    NYX_BET(0B0000_0010, "每日下注比赛", 0, 3),

    NYX_RECHARGE(0B0000_0100, "每日充值", 0, 3);





    /**
     * 成就ID
     */
    private int value;
    /**
     * 名称
     */
    private String name;
    /**
     * 积分
     */
    private int point;
    /**
     * 类型
     */
    private int type;

    Mission(int value, String name, int point, int type) {
        this.value = value;
        this.name = name;
        this.point = point;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public int getPoint() {
        return point;
    }
}
