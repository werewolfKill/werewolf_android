package com.zinglabs.zwerewolf.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * @user wangtonghe
 * @date 2017/7/30
 * @email wthfeng@126.com
 */

public class ToastUtil {

    public static void showToast(Activity activity,String str) {

        Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();

    }
}
