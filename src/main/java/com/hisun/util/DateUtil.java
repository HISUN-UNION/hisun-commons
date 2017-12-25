package com.hisun.util;

import org.apache.commons.lang3.*;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>类名称：</p>
 * <p>类描述: 日期时间转换工具类</p>
 * <p>公司：湖南海数互联信息技术有限公司</p>
 *
 * @创建人：XueZiWei
 * @创建时间：2015/4/20 16:52
 * @创建人联系方式：
 */
public class DateUtil {

    public static final String FILE_NAME = "yyyyMMddHHmmssSSS";
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd";
    public static final String DIR_PATTERN = "yyyy/MM/dd/";
    public static final String DIR_PATTERN2 = "yyyy.MM.dd";
    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMESTAMP_PATTERN3 = "yyyy-MM-dd HH:mm:ss.SSSS";
    public static final String TIMESTAMP_PATTERN2 = "yyyy-MM-dd HH:mm";
    public static final String TIMES_PATTERN = "HH:mm:ss";
    public static final String NOCHAR_PATTERN = "yyyyMMddHHmmss";
    public static final String NOCHAR_PATTERN2 = "yyyyMMdd";
    public static final String yyyydotMM="yyyy.MM";
    public static final String yyyyMMdd = "yyyyMMdd";

    private static final Logger logger = Logger.getLogger(DateUtil.class);

    /**
     * 获取当前时间戳
     * @return
     */
    public static String formatDefaultFileName() {
        return formatDateByFormat(new Date(), FILE_NAME);
    }

    /**
     * 日期转换为字符串
     * @param date
     *            日期
     * @param format
     *            日期格式
     * @return 指定格式的日期字符串
     */
    public static String formatDateByFormat(Date date, String format) {
        String result = "";
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception e) {
                logger.error(e,e);
            }
        }
        return result;
    }

    /**
     * 转换为默认格式(yyyy-MM-dd)的日期字符串
     * @param date
     * @return
     */
    public static String formatDefaultDate(Date date) {
        return formatDateByFormat(date, DEFAULT_PATTERN);
    }

    /**
     * 转换为目录格式(yyyy/MM/dd/)的日期字符串
     *
     * @param date
     *
     * @return
     */
    public static String formatDirDate(Date date) {
        return formatDateByFormat(date, DIR_PATTERN);
    }

    /**
     * 转换为完整格式(yyyy-MM-dd HH:mm:ss)的日期字符串
     *
     * @param date
     *
     * @return
     */
    public static String formatTimesTampDate(Date date) {
        return formatDateByFormat(date, TIMESTAMP_PATTERN);
    }

    /**
     * 转换为时分秒格式(HH:mm:ss)的日期字符串
     *
     * @param date
     *
     * @return
     */
    public static String formatTimesDate(Date date) {
        return formatDateByFormat(date, TIMES_PATTERN);
    }

    /**
     * 转换为时分秒格式(HH:mm:ss)的日期字符串
     *
     * @param date
     *
     * @return
     */
    public static String formatNoCharDate(Date date) {
        return formatDateByFormat(date, NOCHAR_PATTERN);
    }

    /**
     * 日期格式字符串转换为日期对象
     *
     * @param strDate
     *            日期格式字符串
     * @param pattern
     *            日期对象
     * @return
     */
    public static Date parseDate(String strDate, String pattern) {
        Date returnDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            returnDate = format.parse(strDate);
            return returnDate;
        } catch (Exception e) {
            logger.error(e,e);
            returnDate = null;
        }
        return returnDate;
    }

    /**
     * 字符串转换为默认格式(yyyy-MM-dd)日期对象
     *
     * @param date
     *
     * @return
     *
     * @throws Exception
     */
    public static Date parseDefaultDate(String date) {
        return parseDate(date, DEFAULT_PATTERN);
    }

    /**
     * 字符串转换为完整格式(yyyy-MM-dd HH:mm:ss)日期对象
     *
     * @param date
     *
     * @return
     *
     * @throws Exception
     */
    public static Date parseTimesTampDate(String date) {
        return parseDate(date, TIMESTAMP_PATTERN);
    }

    /**
     * 获得当前时间
     *
     * @return
     */
    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 获取年份
     *
     * @param date
     *
     * @return
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取月份
     *
     * @param date
     *
     * @return
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取星期
     *
     * @param date
     *
     * @return
     */
    public static int getWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = dayOfWeek - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        return dayOfWeek;
    }

    /**
     * 获取在年中的星期数
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date){
        return getDateOfYear(date, Calendar.WEEK_OF_YEAR);
    }

    public static int getDayOfYear(Date date){
        return getDateOfYear(date, Calendar.DAY_OF_YEAR);
    }

    public static int getDateOfYear(Date date,int field){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(field);
    }

    /**
     * 获取指定年份中的某周的时间
     * @param year 年份
     * @param num 周数 （365/7）
     * @return
     */
    public static Date getDateNumOfWeek(int year,int num){
        return getDateByNum(year, null, num, Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取指定年份中的某天的时间
     * @param year 年份
     * @param num 天数（365天）
     * @return
     */
    public static Date getDateNumOfDay(int year,int num){
        return getDateByNum(year, null, num, Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取指定年份中的某月的时间
     * @param year 年份
     * @param num 月份
     * @return
     */
    public static Date getDateNumOfMonth(int year,int num){
        return getDateByNum(year, null, num, Calendar.MONTH);
    }

    /**
     * 获取当前年份中的某周的时间，例如：(今天所在年份)-(周所在月份)-（今天所在日期）
     * @param num 周数
     * @return
     */
    public static Date getDefaultDateNumOfWeek(int num){
        return getDateByNum(null, null, num, Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取当前年份中的某日的时间，例如：(今天所在年份)-(今天所在月份)-5日
     * @param num 日数
     * @return
     */
    public static Date getDefaultDateNumOfDay(int num){
        return getDateByNum(null, null, num, Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取当前年份中的某月的时间，例如：(今天所在年份)-5月-（今天所在日期）
     * @param num 月份
     * @return
     */
    public static Date getDefaultDateNumOfMonth(int num){
        return getDateByNum(null, null, num, Calendar.MONTH);
    }

    /**
     * 获取指定某年今天的日期，例如：2013年-(今天所在月份)-（今天所在日期）
     * @param num 年份
     * @return
     */
    public static Date getDefaultDateNumOfYear(int num){
        return getDateByNum(null, null, num, Calendar.YEAR);
    }

    public static Date getDateByNum(Integer year,Integer month,int num,int field){
        Calendar c = Calendar.getInstance();
        if (year != null) {
            c.set(Calendar.YEAR,year);
        }
        if (month != null) {
            c.set(Calendar.MONTH,year);
        }
        c.set(field, num);
        return c.getTime();
    }

    /**
     * 获取日期(多少号)
     *
     * @param date
     *
     * @return
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前时间(小时)
     *
     * @param date
     *
     * @return
     */
    public static int getHour(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前时间(分)
     *
     * @param date
     *
     * @return
     */
    public static int getMinute(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    /**
     * 获取当前时间(秒)
     *
     * @param date
     *
     * @return
     */
    public static int getSecond(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.SECOND);
    }

    /**
     * 获取当前毫秒
     *
     * @param date
     *
     * @return
     */
    public static long getMillis(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    /**
     * 日期增加
     *
     * @param date
     *            Date
     *
     * @param day
     *            int
     *
     * @return Date
     */
    public static Date addDate(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
        return c.getTime();
    }

    public static Date addWeek(Date date,int week){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.WEEK_OF_YEAR, week);
        return c.getTime();
    }


    public static Date addSecond(Date date,int second){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, second);
        return c.getTime();
    }

    public static Date addHour(Date date,int hour){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, hour);
        return c.getTime();
    }

    public static Date addMonth(Date date,int month){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONDAY, month);
        return c.getTime();
    }

    public static Date addYear(Date date,int year){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR,year);
        return c.getTime();
    }

    public static int diffWeek(Date date1,Date date2){
        int result = getWeek(date1)-getWeek(date2);
        return Math.abs(result);
    }

    public static int diffWeekOfYear(Date date1,Date date2){
        int result = getWeekOfYear(date1)-getWeekOfYear(date2);
        return Math.abs(result);
    }

    public static int diffMonth(Date date1,Date date2){
        int result = getMonth(date1)-getMonth(date2);
        return Math.abs(result);
    }

    public static int diffYear(Date date1,Date date2){
        int result = getYear(date1)-getYear(date2);
        return Math.abs(result);
    }

    /**
     * 日期相减(返回天数)
     *
     * @param date
     *            Date
     *
     * @param date1
     *            Date
     *
     * @return int 相差的天数
     */
    public static int diffDate(Date date, Date date1) {
        return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
    }

    /**
     * 日期相减(返回秒值)
     *
     * @param date
     *            Date
     * @param date1
     *            Date
     * @return int
     * @author
     */
    public static Long diffDateTime(Date date, Date date1) {
        return (Long) ((getMillis(date) - getMillis(date1)) / 1000);
    }


    /**
     * 从字符串转换
     * @Description
     * @param dateStr
     * @param pattern
     * @param defaultDate  如果转换出错就用这个时间返回
     * @return
     */
    public static Date parseDate(String dateStr, String pattern, Date defaultDate){
        Date returnDate;
        try {
            returnDate = new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            returnDate = defaultDate;
            logger.error(e,e);
        }
        return returnDate;
    }

    /**
     * 得到本周周一
     *
     * @return Date
     */
    public static Date getMondayOfThisWeek(Date time) {
        Date returnDate = null;
        try {
            if(time != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
                Calendar cal = Calendar.getInstance();
                cal.setTime(time);
                //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
                int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
                if (1 == dayWeek) {
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                }
                cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
                int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
                cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
                returnDate = cal.getTime();
                //String imptimeBegin = sdf.format(cal.getTime());
                //System.out.println("所在周星期一的日期："+imptimeBegin);
                //cal.add(Calendar.DATE, 6);
                //String imptimeEnd = sdf.format(cal.getTime());
                //System.out.println("所在周星期日的日期："+imptimeEnd);
            }
        }catch (Exception e) {
            returnDate = time;
            logger.error(e,e);
        }
        return returnDate;


    }
    /**
     * 得到本周第一天
     * 周日～周六
     * @return Date
     */
    public static Date getFirstDayOfWeek(Date time){
        Date returnDate = null;
        try {
            if (time != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
                Calendar cal = Calendar.getInstance();
                cal.setTime(time);
                //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
                int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
                cal.add(Calendar.DATE, 1 - day);

                returnDate = cal.getTime();
            }
        }catch (Exception e){
            returnDate = time;
            logger.error(e,e);
        }
        return returnDate;
    }

    public static String covertPatternStringToOtherPatternString(String source,String sourcePattern,
                                                                 String destPattern) {

        if(StringUtils.isEmpty(source)==false){
            SimpleDateFormat sdf = new SimpleDateFormat(sourcePattern);
            try {
                Date sourceDate = sdf.parse(source);
                SimpleDateFormat desSdf = new SimpleDateFormat(destPattern);
                return desSdf.format(sourceDate);
            }catch (Exception e){
                return "";
            }
        }else{
            return "";
        }
    }

}
