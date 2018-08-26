package com.lifeix.cbs.api.common.util;

public class BootConstants {

    /**
     * 
     * @author lifeix
     * 
     */
    public static final class typeScheme {
	/**
	 * 新闻
	 */
	public static final String NEWS = "cbs://news?contentId=%s";

	/**
	 * 赛事
	 */
	public static final String CONTEST = "cbs://caicai?contestId=%s&contestType=%s";

	/**
	 * 押押
	 */
	public static final String YAYA = "cbs://yaya?contestId=%s";

	/**
	 * 公告
	 */
	public static final String TEMPLET = "cbs://placard?templetId=%s";

	public static String getNews(String id) {
	    return String.format(NEWS, id);
	}

	public static String getYaya(String id) {
	    return String.format(YAYA, id);
	}
	
	public static String getTemplet(String id) {
	    return String.format(TEMPLET, id);
	}
	
	public static String getFbContest(String id) {
	    return String.format(CONTEST, id,ContestConstants.ContestType.FOOTBALL);
	}
	
	public static String getBbContest(String id) {
	    return String.format(CONTEST, id,ContestConstants.ContestType.BASKETBALL);
	}
    }
    
    //1.文章 2.足球比赛 3.篮球比赛 4.押押 5.公告
    public final static class BootType {


 	/**
 	 * 新闻
 	 */
 	public static final int CONTENT = 1;

 	/**
 	 * 足球
 	 */
 	public static final int FOOTBALL = 2;

 	/**
 	 * 篮球
 	 */
 	public static final int BASKETBALL = 3;

 	/**
 	 * 押押
 	 */
 	public static final int YAYA = 4;

 	/**
 	 * 公告
 	 */
 	public static final int TEMPLET = 5;

     }

}
