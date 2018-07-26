package com.haier.im.base;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    /**
     * 将String日期的字符串数据转为日期格式
     *
     * @param strTime
     * @return
     */
    public static Date str2Date(String strTime) {
        if (strTime == null || strTime.length() <= 0) {
            return null;
        }
        SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = pattern.parse(strTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将时间戳转为data类型
     *
     * @param intTime (13位时间戳)
     * @return
     */
    public static Date getIntTime2Date(long intTime) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long time = new Long(intTime);
            String d = format.format(time);
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当前时间之前或之后几小时 hour
     */
    public static Date getTimeByHour(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);
        return calendar.getTime();
    }

    /**
     * 将一个日期型转换为yyyyMMddHH格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toPointDtmPart(Date aDate) {
        return toFormatDateString(aDate, "yyyyMMddHH");
    }

    /**
     * 将一个日期型转换为指定格式字串
     *
     * @param aDate
     * @param formatStr
     * @return
     */
    public static final String toFormatDateString(Date aDate, String formatStr) {
        if (aDate == null)
            return StringUtils.EMPTY;
        Assert.hasText(formatStr);
        return new SimpleDateFormat(formatStr).format(aDate);
    }

    /**
     * 获取某天前几天零点的时间
     *
     * @param nowDate
     * @param beforeNum
     * @return
     */
    public static Date getBeforeDate(Date nowDate, Integer beforeNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.DAY_OF_MONTH, -beforeNum);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当前时间 yyyyMMddHHmmssSSS
     *
     * @return String
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String s = outFormat.format(now);
        return s;
    }

    /**
     * 年月日时分秒(无下划线) yyyyMMddHHmmss
     */
    public static final String dtLong = "yyyyMMddHHmmss";

    /**
     * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
     *
     * @return 以yyyyMMddHHmmss为格式的当前系统时间
     */
    public static String getOrderNum() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(dtLong);
        return df.format(date);
    }

    /**
     * 在指定时间上加多少分钟
     *
     * @param minute
     * @return
     */
    public static String addMinute(Date date, Integer minute) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date.setTime(date.getTime() + minute * 60 * 1000);
        return sdf.format(date);
    }

    /**
     * 判断时间date1是否在时间date2之前
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isDateBefore(String date1,String date2){
        try{
            DateFormat df = DateFormat.getDateTimeInstance();
            return df.parse(date1).before(df.parse(date2));
        }catch(ParseException e){
            System.out.print("[SYS] " + e.getMessage());
            return false;
        }
    }

    /**
     * 判断时间date1是否在date2之后
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isDateAfter(String date1,String date2){
        try{
            DateFormat df = DateFormat.getDateTimeInstance();
            return df.parse(date1).after(df.parse(date2));
        }catch(ParseException e){
            System.out.print("[SYS] " + e.getMessage());
            return false;
        }
    }

    /**
     * 时间转换成字符串
     *
     * @param date       时间
     * @param formatType 格式化类型
     * @return String
     */
    public static String date2String(Date date, String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        return sdf.format(date);
    }
}
