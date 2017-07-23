package com.zinglabs.zwerewolf.utils;

/**
 * Created by wangxiangbo on 2016/7/5.
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.zinglabs.zwerewolf.R;

import java.io.File;

/**
 * 软件工具类，获取软件的各种属性
 */
public class AppUtil {
    //private static Context app = App.app;

    private AppUtil() {
    }


    /**
     * 检查activity是否安全
     */
    public static boolean isSafe(Activity activity) {
        return !(activity == null || activity.isFinishing() || activity.isDestroyed());
    }

    /**
     * 获取当前应用程序的版本versionName
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前应用程序的版本versionCode
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int version = packInfo.versionCode;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 安装指定文件路径的apk文件
     */
    public static void installApk(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        context.startActivity(intent); // 安装新版本
    }

    /**
     * 创建桌面快捷方式
     *
     * @param resId 应用图标
     *              <uses-permission android:NAME="com.android.launcher.permission.INSTALL_SHORTCUT" />
     */
    public static void createShortcut(Context context, int resId) {
        Intent shortcut = new Intent("com.android.launcher.doRoleAction.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        shortcut.putExtra("duplicate", false);
        ComponentName comp = new ComponentName(context.getPackageName(), "." + ((Activity) context).getLocalClassName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, resId);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        context.sendBroadcast(shortcut);
    }

    /**
     * 判断是否平板
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 判断SD卡是否可用
     */
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 隐藏软键盘
     */
    public static void closeBoard(Context context) {
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        // imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
//        if (imm.isActive()) {
//            //一直是true
//            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
//        }

        if (context == null) {
            return;
        }
        final View v = ((Activity) context).getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

}
