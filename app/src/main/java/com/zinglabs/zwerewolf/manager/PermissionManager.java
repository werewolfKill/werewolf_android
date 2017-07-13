package com.zinglabs.zwerewolf.manager;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxiangbo on 2016/7/19.
 */
public class PermissionManager implements ActivityCompat.OnRequestPermissionsResultCallback {
    /**
     * 需要进行检测的权限数组
     */
    private final static String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,//定位
            Manifest.permission.ACCESS_FINE_LOCATION,//定位
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//文件读写
            Manifest.permission.READ_EXTERNAL_STORAGE,//文件访问
            Manifest.permission.READ_PHONE_STATE//电话状态
    };
    private static final int PERMISSON_REQUESTCODE = 0;
    private static Activity context;

    /**
     * 开始检查权限，暂时用
     */
    public static void checkPermissions(Activity con) {
        context = con;
        checkPermissions(needPermissions);
    }

    private static void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(context, needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]), PERMISSON_REQUESTCODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private static List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                needRequestPermissonList.add(perm);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(context, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private static boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                DialogManager.showMissingPermissionDialog(context);
            }
        }
    }


}
