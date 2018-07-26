package com.haier.im.base;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {
//    public static final String REGEX_PHONE = "^(0[0-9]{2,3})?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$|(^400[0-9]{7}$)";
    public static final String REGEX_PHONE = "^0?(13|14|15|17|18|19)[0-9]{9}$";
    /**
     * 判断是否为32位字符的票据格式
     * @param token
     * @return
     */
    public static boolean isToken(String token) {
        return noNilEqualLength(token, 80);
    }

    /**
     * 判断参数str不为空并且长度等于要求的长度
     * @param str
     * @param length
     * @return
     */
    public static boolean noNilEqualLength(String str, int length) {

        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.length() == length;
    }

    /**
     * 判断参数str不为空并且长度小于等于要求的最大长度
     * @param str
     * @param maxLength
     * @return
     */
    public static boolean noNilLessEqualMaxLength(String str, int maxLength) {

        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.length() <= maxLength;
    }

    /**
     * <b>Description:</b><br>
     * 判断日期格式是否为YYYYMMDD
     *
     * <pre>
     * Validate.isDate("000000") = false
     * Validate.isDate("19990507") = true
     * </pre>
     *
     * @param dateStr
     *            日期 String
     * @return 匹配 true 不匹配 false Boolean
     * @since 1.0.0<br>
     *        <b>cartier</b><br>
     *        <b>Date:</b> 2018-03-20 13:51:50
     */
    public static final boolean isDateYYYYMMDD(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(dateStr);
            return dateStr.equals(sdf.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * <b>Description:</b><br>
     * 判断日期格式是否为YYYY-MM-DD
     *
     * <pre>
     * Validate.isDate("000000") = false
     * Validate.isDate("19990507") = true
     * </pre>
     *
     * @param dateStr
     *            日期 String
     * @return 匹配 true 不匹配 false Boolean
     * @since 1.0.0<br>
     *        <b>cartier</b><br>
     *        <b>Date:</b> 2018-03-20 13:51:50
     */
    public static final boolean isDate1YYYY_MM_DD(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateStr);
            return dateStr.equals(sdf.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * <b>Description:</b><br>
     * 判断日期格式是否为YYYY-MM-DD HH:mm:ss
     *
     * <pre>
     * Validate.isDate("000000") = false
     * Validate.isDate("19990507") = true
     * </pre>
     *
     * @param dateStr
     *            日期 String
     * @return 匹配 true 不匹配 false Boolean
     * @since 1.0.0<br>
     *        <b>cartier</b><br>
     *        <b>Date:</b> 2018-03-20 13:51:50
     */
    public static final boolean isDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateStr);
            return dateStr.equals(sdf.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断字符串是不是null或无字符（trim后）
     * @param o
     * @return
     */
    public static Boolean isEmpty(String o) {
        return (o == null || o.trim().length() == 0);
    }

    /**
     * 根据指定的正则表达式校验字符串
     * @param reg       正则表达式
     * @param string    拼配的字符串
     * @return
     */
    public static boolean startCheck(String reg, String string) {
        if (isEmpty(string)) {
            return false;
        }
        boolean tem = false;

        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);

        tem = matcher.matches();
        return tem;
    }

    public static boolean checkTel(String phone) {
        return startCheck(REGEX_PHONE, phone);
    }

    public static void main(String[] args) {
        System.out.println(startCheck("^0?(13|14|15|17|18|19)[0-9]{9}$","17768601406"));
    }
}
