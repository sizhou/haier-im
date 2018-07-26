package com.haier.im.base;

import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.lang.StringUtils;

import java.text.Collator;
import java.util.*;

public class Utils {


    /**
     * 得到汉字首字母的拼音
     *
     * @param str
     * @return
     */
    public static String getPinYinHeaderChar(String str) {
        String convert = "";
        for (int i = 0; i < str.length(); i++) {
            char word = str.charAt(i);
            String[] pinYinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinYinArray != null) {
                convert += pinYinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }


    public static String getPinYinFirstChar(String str) {
        if (str != null && str.length() > 0) {
            String convert = "";
            char word = str.charAt(0);
            String[] pinYinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinYinArray != null) {
                convert += pinYinArray[0].charAt(0);
            }
            return convert;
        }
        return null;
    }


}
