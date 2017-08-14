package com.zinglabs.zwerewolf.utils;

import java.util.List;

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

    public static String[] trans2StrArr(Integer[] arr,String suffix){
        String [] strArr = new String[arr.length];
        for(int i=0;i<arr.length;i++){
            strArr[i]=arr[i]+suffix;
        }
        return strArr;
    }
    public static String[] trans2StrArr(List<Integer> list,String suffix){
        int size = list.size();

        String [] strArr = new String[size];
        for(int i=0;i<size;i++){
            strArr[i]=list.get(i)+suffix;
        }
        return strArr;
    }
}
