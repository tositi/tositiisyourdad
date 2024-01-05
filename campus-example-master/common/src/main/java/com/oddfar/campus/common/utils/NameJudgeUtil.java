package com.oddfar.campus.common.utils;

import java.util.regex.Pattern;

/**
 * 判断 昵称、账号 是否合法工具包
 *
 * @author zhiyuan
 */
public class NameJudgeUtil {

    /**
     * 判断是否 字母+数字 组合
     * 长度为 3-10
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str.length() > 10 || str.length() < 3) {
            return false;
        }
        str = str.toLowerCase();
        for (int i = str.length(); --i >= 0; ) {

            int chr = str.charAt(i);
            //数字和字母
            if ((chr >= 48 && chr <= 57) || (chr >= 97 && chr <= 122) || (chr == 95)) {

            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 用户名是否合法：英文字母+数字+汉字+下划线
     * 长度为2-8
     *
     * @param str
     * @return
     */
    public static boolean isName(String str) {
        if (str.length() > 10 || str.length() < 2) {
            return false;
        }
        boolean matches = Pattern.matches("^[\\w\\u4E00-\\u9FA5]{1,8}$", str);
        return matches;
    }
}
