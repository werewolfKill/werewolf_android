package com.zinglabs.zwerewolf.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间解析工具
 * Created by Administrator on 2017/3/8.
 */

public class DateUtil {
    public static String nowLongStr() {
        return "" + new Date().getTime();
    }

    public static String dateFormat(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        String time = "";
        try {
            date = new Date((Long.parseLong(str)));
            time = sdf.format(date);
        } catch (Exception e) {
//            格式化日期失败
            e.printStackTrace();
        }

        return time;
    }
}
