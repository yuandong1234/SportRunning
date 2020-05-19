package com.yuong.running.common.utils;

public class StringUtils {
    /**
     * 判断s字符串是否为空
     */
    public static String isEmpry(String s) {
        if (null == s) {
            return "";
        } else if (s.equals("")) {
            return "";
        } else if (s.equals("null")) {
            return "";
        } else {
            return s;
        }
    }

    /**
     * 判断s字符串是String
     */
    public static boolean isString(String s) {
        if (null == s) {
            return false;
        } else if (s.equals("")) {
            return false;
        } else if (s.equals("null")) {
            return false;
        } else return s.length() > 0;
    }
}
