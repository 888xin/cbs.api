package com.lifeix.cbs.api.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;

public class CbsTimeUtils {

    protected static Logger LOG = LoggerFactory.getLogger(CbsTimeUtils.class);

    private static final TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");

    /**
     * 转换UTC时间
     * 
     * @param time
     * @return
     */
    public static String getUtcTimeForTime(String time) {
	if (time == null)
	    return null;
	Date data = getDateByFormatDate(time);
	SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH);
	formatter.setTimeZone(timeZone);
	return formatter.format(data);
    }

    /**
     * 转换UTC时间
     * 
     * @param time
     * @return
     */
    public static String getUtcTimeForDate(Date time) {
	if (time == null)
	    return null;
	SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH);
	formatter.setTimeZone(timeZone);
	return formatter.format(time);
    }

    /**
     * 转换UTC时间
     * 
     * @param time
     * @return
     */
    public static String getTimeForDate(Date time) {
	if (time == null)
	    return null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	sdf.setTimeZone(timeZone);
	return sdf.format(time);
    }

    /**
     * 根据UTC时间字符串生成Date对象 
     * 
     * @param timeStr
     *            UTC时间字符串，例如 Thu Jan 31 13:47:20 +0800 2013
     * @return 在传入参数有误时，将返回当前Date对象
     */
    public static Date getDateByUtcFormattedDate(String timeStr) {
	if (timeStr == null || timeStr.equalsIgnoreCase(""))
	    return new Date();
	try {
	    SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH);
	    formatter.setTimeZone(timeZone);
	    return formatter.parse(timeStr);
	} catch (ParseException e) {

	}

	return new Date();
    }

    public static Date getSolrDateByUtcFormattedDate(String timeStr) {
	if (timeStr == null || timeStr.equalsIgnoreCase(""))
	    return new Date();
	try {
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
	    Calendar c = Calendar.getInstance();
	    c.setTime(formatter.parse(timeStr));
	    c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + 8);
	    return c.getTime();
	    // formatter.setTimeZone(timeZone);
	    // return formatter.parse(timeStr);
	} catch (ParseException e) {

	}

	return new Date();
    }

    /**
     * 通过格式化的日期来获取日期对象
     * 
     * @param timeStr
     *            日期字符串 (yyyy-MM-dd HH:mm:ss)
     * @return 日期对象
     */
    public static Date getDateByFormatDate(String timeStr) {
	if (timeStr == null) {
	    return new Date();
	}
	if (timeStr.length() > 19) {
	    timeStr = timeStr.substring(0, 19);
	}
	boolean isDate = false;
	try {
	    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
	} catch (ParseException e) {
	    isDate = true;
	}

	if (isDate) {
	    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    try {
		return sdf.parse(timeStr);
	    } catch (ParseException e) {
		return new Date();
	    }
	}
	return new Date();
    }

    /**
     * 根据比赛时间算出一天比赛的范围(足球 当天11.00 - 明天11.00 篮球 当天12.00 - 明天12.00)
     * 
     * @param time
     * @return
     */
    public static Date[] findDayRange(Date time, int contestType) {
	Date[] times = new Date[2];
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(time);
	calendar.set(Calendar.HOUR_OF_DAY, contestType == ContestType.BASKETBALL ? 12 : 11);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.SECOND, 0);
	times[0] = calendar.getTime();
	calendar.add(Calendar.DAY_OF_YEAR, 1);
	times[1] = calendar.getTime();
	return times;
    }

    /**
     * 返回一天的开头和结尾
     * 
     * @param time
     * @return
     */
    public static Date[] findBasicDayRange(Date time) {
	Date[] times = new Date[2];
	Calendar now = Calendar.getInstance();
	now.setTime(time);
	now.set(Calendar.HOUR_OF_DAY, 0);
	now.set(Calendar.MINUTE, 0);
	now.set(Calendar.SECOND, 0);
	times[0] = now.getTime();
	now.set(Calendar.HOUR_OF_DAY, 23);
	now.set(Calendar.MINUTE, 59);
	now.set(Calendar.SECOND, 59);
	times[1] = now.getTime();
	return times;
    }

    /**
     * yyyy/MM/dd hh:mm:ss
     * 
     * @param string
     * @return
     */
    public static Date formatDateA(String string) {
	Date date = new Date();
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	try {
	    date = dateFormat.parse(string);
	} catch (ParseException e) {
	    LOG.error(e.getMessage(), e);
	}
	return date;
    }

    /**
     * yyyy/MM/dd
     * 
     * @param string
     * @return
     */
    public static Date formatDateB(String timeStr) {
	if (StringUtils.isEmpty(timeStr)) {
	    return new Date();
	}
	if (timeStr.length() > 10) {
	    timeStr = timeStr.substring(0, 10);
	}
	timeStr = timeStr.replaceAll("-", "/");
	Date date = new Date();
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	try {
	    date = dateFormat.parse(timeStr);
	} catch (ParseException e) {
	    LOG.error(e.getMessage(), e);
	}
	return date;
    }

    /**
     * 比较传入日期跟当前日期的时间差是否在diff天之内
     * 
     * @param date
     * @param diff
     * @return
     */
    public static boolean compare(Date date, int diff) {
	return compare(date, new Date(), diff);
    }

    /**
     * 比较传入的两个日期的日期时间差是否在diff天之内
     * 
     * @param date
     * @param diff
     * @return
     */
    public static boolean compare(Date dateFrom, Date dateTo, int diff) {
	long time = dateFrom.getTime();
	long curr = dateTo.getTime();
	return Math.abs((time - curr)) <= (diff * 24 * 3600 * 1000);
    }

    /**
     * 计算内容时间的组合差值
     * 
     * @param currTime
     * @return 昨天22:08 | 06-20 15:36
     * @throws ParseException
     */
    public static String getContentTimeDiff(String contentTime) {
	if (contentTime == null) {
	    return "刚刚";
	}
	Date time = getDateByUtcFormattedDate(contentTime);
	return getContentTimeDiff(time);
    }

    public static String getSolrContentTimeDiff(String contentTime) {
	if (contentTime == null) {
	    return "刚刚";
	}
	Date time = getSolrDateByUtcFormattedDate(contentTime);
	return getContentTimeDiff(time);
    }

    /**
     * 返回向前的时间差
     * 
     * @param time
     * @return
     */
    public static String getContentTimeDiff(Date time) {
	if (time == null) {
	    return "刚刚";
	}

	Calendar currCalendar = Calendar.getInstance();
	Calendar lastCalendar = Calendar.getInstance();

	lastCalendar.setTime(time);
	if (currCalendar.before(lastCalendar)) {
	    return "刚刚";
	}

	int currYear = currCalendar.get(Calendar.YEAR);
	int currDay = currCalendar.get(Calendar.DAY_OF_YEAR);
	int lastYear = lastCalendar.get(Calendar.YEAR);
	int lastDay = lastCalendar.get(Calendar.DAY_OF_YEAR);
	int diff = lastDay - currDay;
	String tmp = "";

	if (currYear == lastYear) {
	    switch (diff) {
	    case 0:
		long t1 = currCalendar.getTime().getTime();
		long t2 = lastCalendar.getTime().getTime();
		int hours = (int) ((t1 - t2) / 3600000);
		int minutes = (int) (((t1 - t2) / 1000 - hours * 3600) / 60);
		int second = (int) (((t1 - t2) / 1000));
		if (second <= 0)
		    second = 1;
		if (hours == 0 && minutes == 0) {
		    tmp = "刚刚";
		    break;
		}
		if (hours <= 0) {
		    if (minutes < 0) {
			minutes = 0;
		    }
		    tmp = minutes + "分钟前";
		    break;
		}
		if (hours >= 1) {
		    tmp = "今天 " + completionSingle(lastCalendar.get(Calendar.HOUR_OF_DAY)) + ":"
			    + completionSingle(lastCalendar.get(Calendar.MINUTE));
		    break;
		}
	    case -1:
		tmp = "昨天 " + completionSingle(lastCalendar.get(Calendar.HOUR_OF_DAY)) + ":"
		        + completionSingle(lastCalendar.get(Calendar.MINUTE));
		break;
	    default:
		tmp = completionSingle((lastCalendar.get(Calendar.MONTH) + 1)) + "-"
		        + completionSingle(lastCalendar.get(Calendar.DAY_OF_MONTH)) + " "
		        + completionSingle(lastCalendar.get(Calendar.HOUR_OF_DAY)) + ":"
		        + completionSingle(lastCalendar.get(Calendar.MINUTE));
		break;
	    }
	} else {
	    tmp = lastCalendar.get(Calendar.YEAR) + "-" + completionSingle((lastCalendar.get(Calendar.MONTH) + 1)) + "-"
		    + completionSingle(lastCalendar.get(Calendar.DAY_OF_MONTH));
	}

	return tmp;
    }

    /**
     * 返回向后的时间差
     * 
     * @param time
     * @return
     */
    public static String getLastTimeDiff(Date time) {
	long difference = time.getTime() - System.currentTimeMillis();
	if (difference > 1000L * 60 * 60 * 24 * 365 * 10) {
	    return "很久";
	} else if (difference > 1000L * 60 * 60 * 24 * 365) {
	    return difference / (1000L * 60 * 60 * 24 * 365) + "年";
	} else if (difference > 1000L * 60 * 60 * 24 * 30) {
	    return difference / (1000L * 60 * 60 * 24 * 30) + "月";
	} else if (difference > 1000L * 60 * 60 * 24) {
	    return difference / (1000L * 60 * 60 * 24) + "天";
	} else if (difference > 1000L * 60 * 60) {
	    return difference / (1000L * 60 * 60) + "小时";
	} else if (difference > 1000 * 60) {
	    return difference / (1000 * 60) + "分钟";
	} else if (difference > 1000) {
	    return difference / 1000 + "秒";
	}
	return "1秒";
    }

    public static String getSimpleFormatUTCString(String utcTime) {
	try {
	    Date time = new SimpleDateFormat("E MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH).parse(utcTime);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    return sdf.format(time);
	} catch (ParseException e) {
	    LOG.error(e.getMessage(), e);
	}
	return null;
    }

    /**
     * 补全单个数字
     * 
     * @param data
     * @return
     */
    public static String completionSingle(int data) {
	return data <= 9 ? ("0" + data) : String.valueOf(data);
    }

    /**
     * 开场时间在当前之前 并且 赛事状态为上半场、下半场、加时时求剩余时间
     * 
     * @param status
     * @param startTime
     * @return
     */
    public static String calRemainTime(int status, Date startTime) {
	long interval = new Date().getTime() - startTime.getTime();
	int hour = (int) (interval / 1000 / 3600);
	int minute = (int) (interval / 1000 % 3600 / 60) + hour * 60;
	if (status == ContestStatu.HALF_NEXT) {
	    minute = (minute - 15) < 45 ? 45 : (minute - 15);
	}
	int second = (int) (interval / 1000 % 60);
	String remainTime = (String.format("%s:%s", (minute < 10 ? "0" + minute : ("" + minute)), (second < 10 ? "0"
	        + second : "" + second)));
	if (status == ContestStatu.HALF_PREV && minute > 45) {
	    return "45+";
	}
	if (minute > 90) {
	    return "90+";
	}
	return remainTime;
    }

    /**
     * 判断一个时间是否是今天
     * 
     * @param inDate
     * @return
     */
    public static boolean inNow(Date inDate) {
	if (inDate == null)
	    return false;
	Calendar calendar = Calendar.getInstance();
	Calendar inCalendar = Calendar.getInstance();
	inCalendar.setTime(inDate);
	int nowYear = calendar.get(Calendar.YEAR);
	int nowMonth = calendar.get(Calendar.MONTH);
	int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
	int inYear = inCalendar.get(Calendar.YEAR);
	int inMonth = inCalendar.get(Calendar.MONTH);
	int inDay = inCalendar.get(Calendar.DAY_OF_MONTH);
	if (nowYear == inYear && nowMonth == inMonth && nowDay == inDay) {
	    return true;
	}
	return false;
    }

    /**
     * 判断一个时间是否是昨天
     * 
     * @param inDate
     * @return
     */
    public static boolean inYerstoday(Date inDate) {
	if (inDate == null)
	    return false;
	Calendar inCalendar = Calendar.getInstance();
	inCalendar.setTime(inDate);
	inCalendar.add(Calendar.DAY_OF_YEAR, 1);
	return inNow(inCalendar.getTime());
    }

    /***
     * 返回一天的起始日期 ，如Wed Nov 04 00:00:00 CST 2015
     * 
     * @param date
     * @return
     */
    public static Date dayOfStart(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.HOUR_OF_DAY, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.SECOND, 0);
	Date start = calendar.getTime();
	return start;
    }

    /***
     * 返回一天的起始日期 ，如Wed Nov 04 23:59:59 CST 2015
     * 
     * @param date
     * @return
     */
    public static Date dayOfEnd(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.HOUR_OF_DAY, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.SECOND, 0);
	calendar.add(Calendar.DAY_OF_MONTH, 1);
	calendar.add(Calendar.SECOND, -1);
	Date end = calendar.getTime();
	return end;

    }

    /**
     * 获取两个时间之间的所有时间列表
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<Date> rangeDates(Date startDate, Date endDate) {
	List<Date> dates = new ArrayList<Date>();
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(startDate);
	while (!calendar.getTime().after(endDate)) {
	    dates.add(calendar.getTime());
	    calendar.add(Calendar.DAY_OF_YEAR, 1);
	}
	return dates;
    }

    /**
     * 判断一个时间是否在创建房间的时间范围内
     * 
     * @param inDate
     *            赛前两天到赛后十五天
     * @return
     */
    public static boolean roomCreateRange(Date inDate) {
	if (inDate == null) {
	    return false;
	}
	long timeDiff = (System.currentTimeMillis() - inDate.getTime()) / 1000;
	return (timeDiff < 15 * 24 * 3600) && (timeDiff > -(48 * 3600));
    }

    /** 本周一00:00:00 **/
    public static Date getWeekStartTime(Date time) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(time);
	cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	Date weekStart = cal.getTime();
	if (weekStart.compareTo(time) <= 0)
	    return weekStart;
	else {
	    cal.add(Calendar.DAY_OF_MONTH, -7);
	    return cal.getTime();
	}
    }

    /** 本周日23:59:59 **/
    public static Date getWeekEndTime(Date time) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(time);
	cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
	cal.set(Calendar.HOUR_OF_DAY, 23);
	cal.set(Calendar.MINUTE, 59);
	cal.set(Calendar.SECOND, 59);
	Date weekEnd = cal.getTime();
	if (weekEnd.compareTo(time) >= 0)
	    return weekEnd;
	else {
	    cal.add(Calendar.DAY_OF_MONTH, 7);
	    return cal.getTime();
	}
    }

    /**
     * 客户端显示特定需求的时间差 add by lhx on 2015-12-25
     */
    public static String getCbsTimeDiff(Date createTime) {
	if (createTime == null) {
	    return "刚刚";
	}
	Date now = new Date();
	long diff = now.getTime() - createTime.getTime();
	long minutes = diff / 60000;
	if (minutes < 60) {
	    if (minutes < 6) {
		return "刚刚";
	    }
	    return minutes + "分钟前";
	}
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	Calendar calendarOld = Calendar.getInstance();
	Calendar calendarNew = Calendar.getInstance();
	calendarOld.setTime(createTime);
	calendarNew.setTime(now);
	if (calendarOld.get(Calendar.DAY_OF_YEAR) == calendarNew.get(Calendar.DAY_OF_YEAR)) {
	    return "今天 " + sdf.format(createTime);
	}
	if (calendarOld.get(Calendar.DAY_OF_YEAR) == calendarNew.get(Calendar.DAY_OF_YEAR) - 1) {
	    return "昨天 " + sdf.format(createTime);
	}

	if (calendarOld.get(Calendar.YEAR) == calendarNew.get(Calendar.YEAR)) {
	    sdf = new SimpleDateFormat("MM-dd HH:mm");
	    return sdf.format(createTime);
	} else {
	    sdf = new SimpleDateFormat("yyyy-MM-dd");
	    return sdf.format(createTime);
	}
    }

    /**
     * 修改时间
     * 
     * @param time
     * @param calendarType
     *            {@link Calendar} 需要调节的时间类型
     * @param values
     *            修改的值
     * @return
     */
    public static Date upgradeTime(Date time, int calendarType, int values) {
	if (time == null) {
	    return time;
	}
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(time);
	calendar.add(calendarType, values);
	return calendar.getTime();
    }

    /**
     * 获取计算的时间
     * 
     * @param time
     * @param calendarType
     *            {@link Calendar} 需要调节的时间类型
     * @param values
     *            计算天数
     * @return
     */
    public static Date getCalDate(Date time, int values) {
	if (time == null) {
	    return time;
	}
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(time);
	calendar.set(Calendar.HOUR_OF_DAY, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.SECOND, 0);
	calendar.add(Calendar.DAY_OF_YEAR, values);
	return calendar.getTime();
    }

}
