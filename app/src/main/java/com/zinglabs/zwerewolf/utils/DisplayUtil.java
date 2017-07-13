package com.zinglabs.zwerewolf.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangxiangbo on 2016/7/9.
 */
public class DisplayUtil {
    private static DecimalFormat df_double = new DecimalFormat("#0.00");
    private static DecimalFormat df_float = new DecimalFormat("#0.0");

    public static String format2double(Double d) {
        return df_double.format(d);
    }

    public static String format2double(int d) {
        return df_double.format(d);
    }

    public static String format2float(Double d) {
        return df_float.format(d);
    }

    private static Resources resources = Resources.getSystem();

    public static int dp2px(float dpValue) {
        final float scale = resources.getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = resources.getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据设备信息获取当前分辨率下指定单位对应的像素大小；
     * px,dip,sp -> px
     */
    public static float getRawSize(Context c, int unit, float size) {
        Resources r;
        if (c == null) {
            r = Resources.getSystem();
        } else {
            r = c.getResources();
        }
        return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;//得到屏幕的宽度(像素)
    }

    /**
     * 状态栏高度获取方法
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
            return 75;
        }
    }

    static public float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getMetrics(dm);
            return dm.density;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }

    /**
     * 修正listview高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1)));
        listView.setLayoutParams(lp);
    }

    /**
     * 获取yyyy_MM-dd格式的时间
     */
    public static String getYMDDate(String ftime) {
        if (ftime == null)
            return "";
        long time = 0;
        Pattern p1 = Pattern.compile("\\((\\S+)\\)");
        String s1 = ftime;
        Matcher m1 = p1.matcher(s1);
        if (m1.find()) {
            time = Long.parseLong(m1.group(1));
            System.out.println(m1.group(1));
        }
        Date date = null;
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = new Date(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shortDateFormat.format(date);
    }

    public static String bitmap2Base64(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);//参数100表示不压缩
        return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
    }

}
