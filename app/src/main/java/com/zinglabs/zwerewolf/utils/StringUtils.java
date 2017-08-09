package com.zinglabs.zwerewolf.utils;

/**
 * @user wangtonghe
 * @date 2017/8/7
 * @email wthfeng@126.com
 */

public class StringUtils {


    public static String join(int[] arr, String dec) {
        int length = arr.length;
        String str= "";
        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                str+=arr[i];
            } else {
               str+=arr[i]+dec;
            }
        }
        return str;
    }
    public static String join(Integer[] arr, String dec) {
        int length = arr.length;
        String str= "";
        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                str+=arr[i];
            } else {
                str+=arr[i]+dec;
            }
        }
        return str;
    }
    public static String join(String[] arr, String dec) {
        int length = arr.length;
        String str= "";
        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                str+=arr[i];
            } else {
                str+=arr[i]+dec;
            }
        }
        return str;
    }
}
