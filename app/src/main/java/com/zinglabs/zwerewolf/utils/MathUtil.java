package com.zinglabs.zwerewolf.utils;

/**
 * @user wangtonghe
 * @date 2017/7/30
 * @email wthfeng@126.com
 */

public class MathUtil {
    /**
     * 判断字符串是否为数字
     * @param str  字符串
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


}
