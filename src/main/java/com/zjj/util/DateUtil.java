package com.zjj.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期处理
 *
 * @author hwf
 * @remark 方法概要:
 * <p>
 * 获取本周一日期:getMondayOFWeek() 获取本周日的日期:getCurrentWeekday()
 * 获取上周一日期:getPreviousWeekday() 获取上周日日期:getPreviousWeekSunday()
 * 获取下周一日期:getNextMonday() 获取下周日日期:getNextSunday()
 * 获取本月第一天日期:getFirstDayOfMonth() 获取本月最后一天日期:getDefaultDay()
 * 获取上月第一天日期:getPreviousMonthFirst() 获取上月最后一天的日期:getPreviousMonthEnd()
 * 获取下月第一天日期:getNextMonthFirst() 获取下月最后一天日期:getNextMonthEnd()
 * 获取本年的第一天日期:getCurrentYearFirst() 获取本年最后一天日期:getCurrentYearEnd()
 * 获取去年的第一天日期:getPreviousYearFirst() 获取去年的最后一天日期:getPreviousYearEnd()
 * 获取明年第一天日期:getNextYearFirst() 获取明年最后一天日期:getNextYearEnd()
 * 获取本季度第一天到最后一天:getThisSeasonTime(int) 是否闰年:isLeapYear(int)
 */
public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDD2 = "yyyy年MM月dd日";
    public static final String YYYYMMDD3 = "yyyy.MM.dd";
    public static final String YYYYMD = "yyyy.M.d";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYYYMMDDHH = "yyyyMMddHH";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public static final String YYYY_MM_DD_HH_MM = "yyyy.MM.dd HH:mm";

    /** 用来全局控制 上一周，本周，下一周的周数变化 */
    // public static int weeks = 0;
    // public static int MaxDate;// 一月最大天数
    // public static int MaxYear;// 一年最大天数

    /**
     * 按照指定格式 把指定字符串格式化为时间类型
     *
     * @param str    需要转换的字符串,如:2009-08-01 12:21:22
     * @param format 转换格式,如:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date string2Date_format(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date d = sdf.parse(str);
            return d;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String string2String_format(String str, String Oldformat, String newformat) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Oldformat);
        try {
            Date d = sdf.parse(str);
            return date2String_format(d, newformat);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static int dateCompare(Date d1, Date d2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTime(d1);
        calendar2.setTime(d2);

        int result = calendar1.compareTo(calendar2);
        return result;
    }

    /**
     * 字符串日志转成指定格式date
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static Date string2Date(String date, String dateFormat) {
        Date d = null;
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        try {
            d = s.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return d;
    }

    /**
     * 把字符串格式成時間類型,只支持如下兩種格式轉換:yyyy-MM-dd,yyyy-MM-dd HH:mm:ss
     *
     * @param date 传入的时间字符串
     * @return 转换后的Date类型对象
     */
    public static Date string2Date_noFormat(String date) {

        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        if (date.length() == 19) {
            s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        try {
            d = s.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return d;
    }

    /**
     * 把指定时间格式化为指定格式的字符串
     *
     * @param date   日期
     * @param format 格式化字符串,如:yyyy-MM-dd HH:mm:ss
     * @return 字符串的日期
     */
    public static String date2String_format(Date date, String format) {
        String dateStr = "";
        try {
            Format formatter;
            formatter = new SimpleDateFormat(format);
            dateStr = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return dateStr;
    }

    public static String date2String_format(String format) {
        String dateStr = "";
        try {
            Format formatter;
            formatter = new SimpleDateFormat(format);
            dateStr = formatter.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return dateStr;
    }

    /**
     * 按照指定格式获取系统时间
     *
     * @param format 如:yyyy-MM-dd HH:mm:ss
     * @return 字符串的日期
     */
    public static String getSysDate(String format) {
        String dateStr = "";
        try {
            Format formatter;
            Date date = new Date();
            formatter = new SimpleDateFormat(format);
            dateStr = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return dateStr;
    }

    /**
     * 判断是否闰年
     *
     * @param year 年份
     * @return true 闰年 ,false 平年
     */
    public static boolean isLeapyear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取月份天数
     *
     * @param year  年份
     * @param month 月份
     * @return 天数
     */
    public static int getMonthDays(String year, String month) {
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        int monthdays = 31;
        switch (monthInt) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12: {
                monthdays = 31;
                break;
            }
            case 2: {
                if (isLeapyear(yearInt)) {
                    monthdays = 29;
                } else {
                    monthdays = 28;
                }
                break;
            }
            case 4:
            case 6:
            case 9:
            case 11: {
                monthdays = 30;
                break;
            }
        }
        return monthdays;
    }

    /**
     * 判断某个时间是星期几
     *
     * @param strDate 需要判断的时间
     * @return 0 表示是星期天,其他对应
     */
    public static int getWeekByDate(String strDate) {
        int dayOfWeek = 0;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            Date date = new Date();
            date = sdf.parse(strDate);
            calendar.setTime(date);
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dayOfWeek - 1;
    }

    /**
     * 判断某个时间是星期几
     *
     * @param strDate 为空用当前时间
     *                需要判断的时间
     * @return 0 表示是星期天,其他对应
     */
    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null == date) {
            calendar = Calendar.getInstance();
        } else {
            calendar.setTime(date);
        }
        Integer[] weekDays = {7, 1, 2, 3, 4, 5, 6};
        return weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * 获得距给定日期countday的字符串格式
     *
     * @param date     给定日期
     * @param countday 天数
     * @param flag     true 给定日期之前,false 给定日期之后
     * @return YYYY-MM-DD 格式字符串日期
     */
    public static String getDateString(Date date, int countday, boolean flag) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (flag) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - countday);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + countday);
        }
        return date2String_format(calendar.getTime(), "yyyy-MM-dd");
    }

    /**
     * 获取两个时间之间的相差天数
     *
     * @param date1 起始时间
     * @param date2 结束时间
     * @return 相差天数
     * @throws ParseException
     */
    public static Long getDateDifference(Date date1, Date date2) throws ParseException {
        // 日期相减得到相差的日期
        long day = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) > 0 ? (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) : (date2.getTime() - date1.getTime())
                / (24 * 60 * 60 * 1000);
        return day;
    }

    /**
     * 判断某个日期是否在两个日期之间
     *
     * @param d1 起始日期
     * @param d2 需要判断的日期
     * @param d3 结束日期
     * @return true 在两个日期之间(包括相等) , false 不在两个日期之间
     */
    public static boolean isIn(Date d1, Date d2, Date d3) {
        boolean result = false;
        if (d2.compareTo(d1) >= 0 && d2.compareTo(d3) <= 0) {
            result = true;
        }
        return result;

    }

    /**
     * 获得当前日期与上周日相差的天数
     *
     * @return
     */
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    /**
     * 获得上周星期一的日期
     *
     * @return 如2009-11-23格式字符串
     */
    public static String getPreviousWeekday() {
        int weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获得上周星期一的日期
     *
     * @return 如2009-11-23格式字符串
     */
    public static String getPreviousWeekday(String dateFormat) {
        int weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        String preMonday = new SimpleDateFormat(dateFormat).format(monday);
        return preMonday;
    }

    /**
     * 获得上周星期日的日期
     *
     * @return 如2009-11-29格式字符串
     */
    public static String getPreviousWeekSunday() {
        int weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获得上周星期日的日期
     *
     * @return 如2009-11-29格式字符串
     */
    public static String getPreviousWeekSunday(String dateFormat) {
        int weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        Date monday = currentDate.getTime();
        String preMonday = new SimpleDateFormat(dateFormat).format(monday);
        return preMonday;
    }

    /**
     * 获得本周一的日期
     *
     * @return 如:2010-12-31
     */
    public static String getMondayOFWeek() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获得本周一的日期
     *
     * @return 如:2010-12-31
     */
    public static String getMondayOFWeek(String dateFormat) {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        String preMonday = new SimpleDateFormat(dateFormat).format(monday);
        return preMonday;
    }

    /**
     * 获得本周星期日的日期
     *
     * @return 如:2010-12-31
     */
    public static String getCurrentWeekday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获得下周星期一的日期
     *
     * @return 如:2010-12-31
     */
    public static String getNextMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获得下周星期日的日期
     *
     * @return 如:2010-12-31
     */
    public static String getNextSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }


    /**
     * 获得下周星期几的日期
     *
     * @return 如:2010-12-31
     */
	public static String getNextWeekNum(int week) {
		Calendar cal = Calendar.getInstance();
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (dayWeek == 1) {
			cal.add(Calendar.DAY_OF_MONTH, week);
		} else {
			cal.add(Calendar.DAY_OF_MONTH, 8 - dayWeek + week);
		}
		return date2String_format(cal.getTime(), "yyMMdd");
	}

    /**
     * 获得本周星期几的日期
     *
     * @return 如:2010-12-31
     */
    public static String getCurrentWeekNum(int week) {
    	Calendar cal = Calendar.getInstance();
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (dayWeek == 1) {
			cal.add(Calendar.DAY_OF_MONTH, week - 7);
		} else {
			cal.add(Calendar.DAY_OF_MONTH, week - dayWeek + 1);
		}
		return date2String_format(cal.getTime(), "yyMMdd");
    }

    /**
     * 获取当月第一天
     *
     * @return 如:2010-12-31
     */
    public static String getFirstDayOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取当月第一天
     *
     * @return 如:2010-12-31
     */
    public static String getFirstDayOfMonth(String dateFormat) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 计算当月最后一天
     *
     * @return 如:2010-12-31
     */
    public static String getDefaultDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取上月第一天
     *
     * @return 如:2010-12-31
     */
    public static String getPreviousMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
        // lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取上上月第一天
     *
     * @return 如:2010-12-01
     */
    public static String getPrevious2MonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, -2);// 减两个月
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取上月第一天
     *
     * @return 如:2010-12-31
     */
    public static String getPreviousMonthFirst(String dateFormat) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
        // lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得上月最后一天
     *
     * @return 如:2010-12-31
     */
    public static String getPreviousMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得上月最后一天
     *
     * @return 如:2010-12-31
     */
    public static String getPreviousMonthEnd(String dateFormat) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得下个月第一天
     *
     * @return 如:2010-12-31
     */
    public static String getNextMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得下个月最后一天
     *
     * @return 如:2010-12-31
     */
    public static String getNextMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 加一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取今年的总天数
     *
     * @return 如:2010-12-31
     */
    public static int getYearPlus() {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            return -MaxYear;
        } else {
            return 1 - yearOfNumber;
        }
    }

    /**
     * 获得本年第一天
     *
     * @return 如:2010-12-31
     */
    public static String getCurrentYearFirst() {
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        return preYearDay;
    }

    /**
     * 获得本年最后一天
     *
     * @return 如:2010-12-31
     */
    public static String getCurrentYearEnd() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        return years + "-12-31";
    }

    /**
     * 获得上年第一天的日期
     *
     * @return 如:2010-12-31
     */
    public static String getPreviousYearFirst() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        years_value--;
        return years_value + "-1-1";
    }

    /**
     * 获得上年最后一天
     *
     * @return 如:2010-12-31
     */
    public static String getPreviousYearEnd() {
        int weeks = 0;
        weeks--;
        int yearPlus = getYearPlus();
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            MaxYear = -MaxYear;
        } else {
            MaxYear = 1 - yearOfNumber;
        }

        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks + (MaxYear - 1));
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        getThisSeasonTime(11);
        return preYearDay;
    }

    /**
     * 获得本季度第一天和最后一天
     *
     * @param month 月份
     * @return 如:2009-10-1;2009-12-31
     */
    public static String getThisSeasonTime(int month) {
        int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days + ";" + years_value + "-" + end_month + "-" + end_days;
        return seasonDate;

    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year  年
     * @param month 月
     * @return 最后一天 如:31
     */
    public static int getLastDayOfMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }

    /**
     * 是否闰年
     *
     * @param year 年
     * @return true 是,false 否
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 获得明年第一天
     *
     * @return 如:2010-12-31
     */
    public static String getNextYearFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        str = sdf.format(lastDate.getTime());
        return str;

    }

    /**
     * 获得明年最后一天
     *
     * @return 如:2010-12-31
     */
    public static String getNextYearEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得昨天的日期
     *
     * @return
     */
    public static String getYesterday() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat(YYYY_MM_DD).format(c.getTime());
        return yesterday;
    }

    /**
     * 获得昨天日期返回Date
     */
    public static Date getYesterdayDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -1);
        Date date = c.getTime();
        return date;
    }

    /**
     * 获得昨天的日期
     *
     * @return
     */
    public static String getYesterday(String dateFormat) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat(dateFormat).format(c.getTime());
        return yesterday;
    }

    /**
     * 获得当前前n天的日期
     *
     * @return
     */
    public static String getBeforday(int num, String dateFormat) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - num);
        String yesterday = new SimpleDateFormat(dateFormat).format(c.getTime());
        return yesterday;
    }

    /**
     * 获得某天前n天的日期
     *
     * @return
     */
    public static Date getBeforday(int num, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - num);
        return c.getTime();
    }

    public static int[] getDateCells(String date) {
        int[] cells = new int[6];
        String[] startSplit = date.split(" ");
        if (startSplit != null && startSplit.length > 0) {
            String dateString = startSplit[0];
            String timeString = startSplit[1];

            String[] dateStringSplit = dateString.split("-");
            cells[0] = Integer.parseInt(dateStringSplit[0]);
            cells[1] = Integer.parseInt(dateStringSplit[1]);
            cells[2] = Integer.parseInt(dateStringSplit[2]);

            String[] timeStringSplit = timeString.split(":");
            cells[3] = Integer.parseInt(timeStringSplit[0]);
            cells[4] = Integer.parseInt(timeStringSplit[1]);
            cells[5] = Integer.parseInt(timeStringSplit[2]);
        }
        return cells;
    }

    public static String getDistance(Date firstTime, Date lastTime) {
        String distance = "0秒";
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        String start = sdf.format(firstTime);
        String endDate = sdf.format(lastTime);

        int[] startCells = getDateCells(start);
        int[] endCells = getDateCells(endDate);

        int syear = startCells[0], smonth = startCells[1], sday = startCells[2], shour = startCells[3], smunite = startCells[4], ssecond = startCells[5];
        int eyear = endCells[0], emonth = endCells[1], eday = endCells[2], ehour = endCells[3], emunite = endCells[4], esecond = endCells[5];

        if (eyear > syear) {
            distance = (eyear - syear) + "年";
        } else if (eyear == syear) {
            if (emonth > smonth) {
                distance = (emonth - smonth) + "月";
            } else if (emonth == smonth) {
                if (eday > sday) {
                    distance = (eday - sday) + "天";
                } else if (eday == sday) {
                    if (ehour > shour) {
                        distance = (ehour - shour) + "小时";
                    } else if (ehour == shour) {
                        if (emunite > smunite) {
                            distance = (emunite - smunite) + "分钟";
                        } else if (emunite == smunite) {
                            if (esecond > ssecond) {
                                distance = (esecond - ssecond) + "秒";
                            }
                        }
                    }
                }
            }
        }
        return distance;
    }

    public static String getMonthString(Date date, int monthNum) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + monthNum);
        return sdf.format(calendar.getTime());
    }

    public static String getEnglishTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String[] monthsEng = {"January ", "February ", "March ", "April ", "May ", "June ", "July ", "August ", "September ", "October ", "November ", "December "};
        String time = monthsEng[month] + day + "th";
        return time;
    }

    /**
     * 获取当前时间，没有时分秒
     *
     * @return
     */
    public static Date getYearDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取当前时间，计分秒
     *
     * @return
     */
    public static Date getHourDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取当月第一天，没有时分秒
     *
     * @return
     */
    public static Date getMFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取下月第一天，没有时分秒
     *
     * @return
     */
    public static Date getNMFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取当前时间，不计时分秒
     *
     * @return
     */
    public static Date getHourDate2() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取上一天，不计时分秒
     *
     * @return
     */
    public static Date getUpHourDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取某天零点时间：day=0当天； day=1下一天； day=-1昨天
     *
     * @return
     */
    public static Date getNextDate(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date;
    }

    public static String getNextDate(int day, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return new SimpleDateFormat(dateFormat).format(calendar.getTime());
    }

    /**
     * 获取下一天，不计时分秒
     *
     * @return
     */
    public static Date getNextHourDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取下一整点，不计时分秒
     *
     * @return
     */
    public static Date getNextHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取下一天，计时分秒
     *
     * @param date
     * @return
     */
    public static Date getNextDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + 1);
        return now.getTime();
    }

    public static String getLongTime(Date newTime) {
        String name = "";
        long newLong = System.currentTimeMillis();
        long oldLong = newTime.getTime();
        long num = (newLong - oldLong) / 1000;//时间差
        System.out.println(num);
        if (num < 60) {
            name = "刚刚";
        } else if (num < 3600 && num >= 60) {
            name = (num / 60) + "分钟前";
        } else if (num >= 3600 && num < 86400) {
            name = (num / 3600) + "小时前";
        } else if (num >= 86400 && num < 2592000) {
            name = (num / 86400) + "天前";
        } else if (num >= 2592000 && num < 31104000) {
            name = (num / 2592000) + "月前";
        } else if (num >= 31104000) {
            name = (num / 31104000) + "年前";
        }
        return name;
    }

    public static String getDateString(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static String getDateString(String format, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 判断是否是同一天
     *
     * @param day1
     * @param day2
     * @return
     */
    public boolean isSameDay(String inviteTime) {
        Date nowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(inviteTime));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ds1 = sdf.format(calendar.getTime());
        String ds2 = sdf.format(nowDate);
        if (ds1.equals(ds2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 当前时间和指定时间是否在同一周
     *
     * @param timeStamp
     * @return
     */
    public static boolean inSameWeek(long timeStamp) {
        Calendar cal = Calendar.getInstance();
        int currYear = cal.get(Calendar.YEAR);
        int currWkOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        cal.setTime(new Date(timeStamp));
        int year = cal.get(Calendar.YEAR);
        int wkOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        return currYear == year && currWkOfYear == wkOfYear;
    }

    /**
     * 当前时间和指定时间是否在同一月
     *
     * @param timeStamp
     * @return
     */
    public static boolean inSameMonth(long timeStamp) {
        Calendar cal = Calendar.getInstance();
        int currYear = cal.get(Calendar.YEAR);
        int currMon = cal.get(Calendar.MONTH);
        cal.setTime(new Date(timeStamp));
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH);
        return currYear == year && currMon == mon;
    }

    /**
     * 当前时间和指定时间是否在同一天
     *
     * @param timeStamp
     * @return
     */
    public static boolean inSameDay(long timeStamp) {
        Calendar cal = Calendar.getInstance();
        int currYear = cal.get(Calendar.YEAR);
        int currDay = cal.get(Calendar.DAY_OF_YEAR);
        cal.setTime(new Date(timeStamp));
        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_YEAR);
        return currYear == year && currDay == day;
    }

    /**
     * 把字符串转化成日期型。
     *
     * @param String     字符串
     * @param DateFormat 日期格式
     * @return Date 转化后的日期
     */
    public static Date getDate(String name, String df, Date defaultValue) {
        if (name == null) {
            return defaultValue;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(df);

        try {
            return formatter.parse(name);
        } catch (ParseException e) {
        }
        return defaultValue;
    }

    public static Date getDateDf(String name, String df) {
        return getDate(name, df, null);
    }

    /**
     * 返回两个日期的时间差，返回的时间差格式可以是: Calendar.YEAR, Calendar.MONTH, Calendar.DATE
     * 注意：该功能采用递归的方法，效率还有待解决，如果两个时间之差较大，而要求返回的时间格式又很小，这时效率就很差
     * 但此方法在要求精度较高的情况下比较有效，如求月份差的时候就比较准确，考虑到了各种情况．如闰月，闰年
     *
     * @param earlyDate
     * @param lateDate
     * @param returnTimeFormat
     * @return time
     */
    public static int getBetweenTime(Date earlyDate, Date lateDate, int returnTimeFormat) {
        Calendar cnow = Calendar.getInstance();
        cnow.setTime(earlyDate);
        Calendar clast = Calendar.getInstance();
        clast.setTime(lateDate);
        return getBetweenTime(cnow, clast, returnTimeFormat);
    }

    /**
     * 返回两个日期的时间差，返回的时间差格式可以是: Calendar.YEAR, Calendar.MONTH, Calendar.DATE
     * 注意：该功能采用递归的方法，效率还有待解决，如果两个时间之差较大，而要求返回的时间格式又很小，这时效率就很差
     * 但此方法在要求精度较高的情况下比较有效，如求月份差的时候就比较准确，考虑到了各种情况．如闰月，闰年
     *
     * @param earlyDate
     * @param lateDate
     * @param returnTimeFormat
     * @return time
     */
    public static int getBetweenTime(Calendar earlyDate, Calendar lateDate,
                                     int returnTimeFormat) {
        earlyDate = (Calendar) earlyDate.clone();
        lateDate = (Calendar) lateDate.clone();
        int time = 0;
        while (earlyDate.before(lateDate)) {
            earlyDate.add(returnTimeFormat, 1);
            time++;
        }
        return time;
    }

    /**
     * 返回当前时间距离下一个整点的时间差
     *
     * @param chronoUnit 返回的时间格式对应的时长：chronoUnit.SECONDS 秒数；chronoUnit.MILLIS；chronoUnit.NANOS；chronoUnit.MINUTES
     * @return
     */
    public static long getDateBetween(Date start, Date end, ChronoUnit chronoUnit) {
        long mill = end.getTime() - start.getTime();
        if (ChronoUnit.SECONDS.equals(chronoUnit)) {
            return mill/1000;
        } else if (ChronoUnit.MINUTES.equals(chronoUnit)) {
            return mill/60000;
        }
        return mill;
    }

    /**
     * 返回当前时间距离下一个整点的时间差
     *
     * @param chronoUnit 返回的时间格式对应的时长：chronoUnit.SECONDS 秒数；chronoUnit.MILLIS；chronoUnit.NANOS；chronoUnit.MINUTES
     * @return
     */
    public static long getBetweenNextHour(ChronoUnit chronoUnit) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime nextHour = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), start.getHour() + 1, 0);
        Duration duration = Duration.between(start, nextHour);
        if (ChronoUnit.SECONDS.equals(chronoUnit)) {
            return duration.getSeconds();
        } else if (ChronoUnit.MILLIS.equals(chronoUnit)) {
            return duration.toMillis();
        } else if (ChronoUnit.NANOS.equals(chronoUnit)) {
            return duration.toNanos();
        } else if (ChronoUnit.MINUTES.equals(chronoUnit)) {
            return duration.toMinutes();
        }
        return duration.getSeconds();
    }

    /**
     * 得到给定的时间距离现在的天数
     *
     * @param last
     * @return
     */
    public static int getBetweenDays(Date begin, Date last) {
        return getBetweenTime(begin, last, Calendar.DATE);
    }

    /**
     * 得到给定的时间距离现在的小时
     *
     * @param last
     * @return
     */
    public static int getBetweenHours(Date begin, Date last) {
        return getBetweenTime(begin, last, Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取前N小时
     *
     * @param date
     * @param n
     * @return
     */
    public static Date getPrevNHour(Date date, int n) {
        if (date == null) return null;
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.HOUR, now.get(Calendar.HOUR) + n);
        return now.getTime();
    }

    /**
     * 获取day
     *
     * @param args
     */

    public static String getMonthDay() {
        Calendar cal = Calendar.getInstance();
        int currDay = cal.get(Calendar.DAY_OF_MONTH);
        String day = "";
        if (currDay < 10) {
            day = "0" + currDay;
        } else {
            day = "" + currDay;
        }
        return day;
    }

    /**
     * 获取月份
     *
     * @return
     */
    public static String getYearMonth() {
        Calendar cal = Calendar.getInstance();
        int currMonth = cal.get(Calendar.MONTH) + 1;
        String month = "";
        if (currMonth < 10) {
            month = "0" + currMonth;
        } else {
            month = currMonth + "";
        }
        return month;
    }

    public static String dateFormate(Date date, String formate) {
        return dateFormate(date, formate, false);
    }

    public static String dateFormate(Date date, String formate, boolean useUSA) {
        if (date != null) {
            SimpleDateFormat sdf = null;
            if (useUSA) {
                sdf = new SimpleDateFormat(
                        formate, Locale.US);
            } else {
                sdf = new SimpleDateFormat(
                        formate);
            }
            return sdf.format(date);
        } else {
            return null;
        }
    }

    /**
     * 判断日期格式是否为 yyyy-MM-dd
     */
    public static boolean isDateFormat(String strDate) {
        String format = "((19|20)[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(strDate);
        return matcher.matches();
    }

    /**
     * 获取指定月份的第一天
     * 格式如：2016-06-01
     */
    public static String getFirstDayByAlreadyMonth(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            Date date = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DATE, 1);

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            return sdf2.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定月份的最后一天
     * 格式如：2016-06-30
     */
    public static String getLastDayByAlreadyMonth(String dataStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            Date date = sdf.parse(dataStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DATE, 1);// 设为当前月的1号
            cal.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
            cal.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            return sdf2.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取日期数组（从开始时间到结束时间）
     *
     * @param startDate
     * @param endDate
     * @return
     * @author hwf
     * @date 2016-9-23
     */
    public static String[] getDays(Date startDate, Date endDate) {
        try {
            String daynum = (DateUtil.getDateDifference(startDate, endDate) + 1) + "";
            String[] days = new String[Integer.parseInt(daynum)];
            for (int i = 0; i < days.length; i++) {
                if (i == 0) {
                    days[i] = DateUtil.date2String_format(startDate, "yyyy-MM-dd");
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startDate);
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + i);
                    Date newDate = calendar.getTime();
                    days[i] = DateUtil.date2String_format(newDate, "yyyy-MM-dd");
                }
            }
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取某时间段的每天时间
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String[] getDates(String startDate, String endDate) {
        Date sDate = DateUtil.getDateDf(startDate, "yyyy-MM-dd");
        Date eDate = DateUtil.getDateDf(endDate, "yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sDate);
        long time1 = calendar.getTimeInMillis();
        calendar.setTime(eDate);
        long time2 = calendar.getTimeInMillis();
        int between_days = (int) ((time2 - time1) / (1000 * 3600 * 24));
        String[] dates = new String[between_days + 1];
        dates[0] = startDate;
        for (int i = 1; i <= between_days; i++) {
            dates[i] = DateUtil.getDateString(sDate, i, false);
        }
        return dates;
    }

    /**
     * “数字” 日期转换为 yyyy-MM-dd 格式的日期
     * “yyyy年MM月dd日” 日期转换为 yyyy-MM-dd 格式的日期
     *
     * @param source: 原日期
     * @param format: 要转换的日期格式
     * @throws ParseException
     */
    public static String numericToFormat(String source, String format) {
        String result = "";
        try {
            //情况1 字符串 “yyyy年MM月dd日” 类型的日期
            if (!StringUtil.isNumeric(source)) {
                Date date = new SimpleDateFormat("yyyy年MM月dd日").parse(source);
                result = date2String_format(date, format);

                //情况2   数字
            } else {
                Calendar cd = new GregorianCalendar(1900, 0, -1);
                Date date = cd.getTime();
                Date date_result = DateUtils.addDays(date, Integer.parseInt(source));
                result = date2String_format(date_result, format);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取距离今天多少天的日期
     *
     * @param distance   距离天数：5 后5天日期   -5前5天日期
     * @param dateFormat 格式：不传默认yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getTheDistanceDate(int distance, String dateFormat) {
        if (StringUtil.isEmpty(dateFormat)) {
            dateFormat = DateUtil.YYYY_MM_DD_HH_MM_SS;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, distance);
        Date date = c.getTime();
        String theDate = sdf.format(date);
        return theDate;
    }

    /**
     * 获取近三个月第一天
     *
     * @return 如:2010-12-31
     */
    public static String getThreeMonthStart() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -2);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取当前时间的一年之后的时间
     */
    public static Date getAfterYear() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
//	        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        calendar.add(Calendar.YEAR, +1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取指定时间的一年之后的时间
     */
    public static Date getAfterYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
//	        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        calendar.add(Calendar.YEAR, +1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取当前时间的几个月之后的时间
     */
    public static Date getAfterMonth(Integer monthNum) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthNum);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取指定时间的几个月之后的时间
     */
    public static Date getAfterMonth(Date date, Integer monthNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthNum);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取当前时间的N天之后的时间
     * @return Date
     */
    public static Date getAfterDate(Integer dayNum) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
        calendar.add(Calendar.DATE, dayNum);
        return calendar.getTime();
    }

    /**
     * 获取当时间的小时
     */
    public static int getHourOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前时间的秒数
     *
     * @param date
     * @return
     */
    public static String getSecondTimestamp(Date date) {
        if (null == date) {
            date = new Date();
        }
        return String.valueOf(date.getTime() / 1000);
    }

    /**
     * 时间格式化
     *
     * @param date 时间
     * @return 格式后的时间
     */
    public static String formatThisTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
        return sdf.format(date);
    }

    /**
     * 获取当天0点
     *
     * @return
     */
    public static Date getTodayStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    /**
     * 获取当天截止时间
     *
     * @return
     */
    public static Date getTodayEndTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 23);
        todayStart.set(Calendar.MINUTE, 59);
        todayStart.set(Calendar.SECOND, 59);
        todayStart.set(Calendar.MILLISECOND, 999);
        return todayStart.getTime();
    }

    /**
     * 	获取TimeMillis后的时间
     *
     * @param date
     * @param timeMillis
     * @param format
     * @return
     */
    public static String getTimeAfterTimeMillis(Date date, long timeMillis, String format) {
        long time = date.getTime() + timeMillis;
        Date nowDate = new Date(time);
        return DateUtil.date2String_format(nowDate, format);
    }

    /** 
     * 	时间戳转换成日期格式字符串 
     * @param seconds 精确到秒的字符串 
     * @param formatStr 
     * @return 
     */  
    public static String timeStamp2Date(Long seconds,String format) {  
        if(seconds == null){  
            return "";  
        }  
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }   
        SimpleDateFormat sdf = new SimpleDateFormat(format);  
        return sdf.format(new Date(Long.valueOf(seconds)));  
    }
    
    public static void main(String[] args) {
        long time = getDateBetween(string2Date_format("20191016094922", "yyyyMMddHHmmss"), new Date(), ChronoUnit.MINUTES);
        System.out.println(time);
    }
}
