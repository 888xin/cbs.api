package com.lifeix.cbs.api.common.util;

public class CbsUserConstants {

    /**
     * 图片上传bucket常量
     * 
     * @author lifeix
     * 
     */
    public static final class ImageBucket {

	// 照片
	public static final String PHOTO = "lifeixphoto";

	// 头像
	public static final String AVATAR = "lifeixavatar";
    }

    /**
     * 微信常量
     * 
     * @author lifeix
     * 
     */
    public static final class UserWeixin {

	// H5应用名
	public static final String APPID = "wxd78f00d036377692";

	// APP应用名
	public static final String APPID_APP = "wx5640b8a003a5ee1e";

	// 统计redisString secret = "64283b4988a57606a86ee97edc3ddc1e";
	public static final String SECRET = "64283b4988a57606a86ee97edc3ddc1e";

	// 应用名
	public static final String ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?";

	// 应用名
	public static final String REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?";

	// 应用名
	public static final String USERINFO = "https://api.weixin.qq.com/sns/userinfo?";

	// 获取token
	public final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	// 推送消息模板
	public static String SEND_TEMP_MESS = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    }

    public static final class UserToken {
	/**
	 * 用户现有token
	 */
	public static final String USERTOKENS = "cbs:user:tokens";

	/**
	 * 客户端token后缀
	 */
	public static final String PLAINTEXT = "lifei-cbs-123!@#";

	/**
	 * token失效时间
	 */
	public static final int EXPIRSTIME = 60 * 60 * 24 * 30;//

    }

    /**
     * 查找用户类型
     * 
     * @author pengkw
     * 
     */
    public final static class UserFindType {

	public static final String ID = "id";

	public static final String LONGNO = "longno";

	public static final String EMAIL = "email";

	public static final String MOBILE = "mobile";

	public static final String DOMAIN = "domain";

    }

    /**
     * 用户注册来源
     * 
     * @author lifeix
     *
     */
    public static final class UserRegSource {

	public static final String MOBILE = "mobile";

	public static final String WEIXIN = "weixin";
    }

}
