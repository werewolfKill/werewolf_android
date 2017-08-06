package com.zinglabs.zwerewolf.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zinglabs.zwerewolf.R;
import com.zinglabs.zwerewolf.config.Constants;
import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.role.Witch;
import com.zinglabs.zwerewolf.utils.AppUtil;
import com.zinglabs.zwerewolf.utils.IMClientUtil;
import com.zinglabs.zwerewolf.utils.MathUtil;
import com.zinglabs.zwerewolf.utils.RoleUtil;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by wangxiangbo on 2016/7/22.
 */
public class DialogManager {
    private static PopupWindow pw;
    private static PopupWindow room_day_pw;
    private static View room_day_v;
    private static ProgressDialog progressDialog;

    /**
     * 显示自定义的加载对话框
     */
    public static void showGameDay(Activity context, View view, String str1, String str2, String str3) {
        if (!AppUtil.isSafe(context)) {
            return;
        }
        if (room_day_pw == null) {
            room_day_v = LayoutInflater.from(context).inflate(R.layout.pw_room_day, null);
            room_day_pw = new PopupWindow(room_day_v, view.getWidth(), WindowManager.LayoutParams.WRAP_CONTENT, true);
            room_day_pw.setTouchable(false);
            room_day_pw.setBackgroundDrawable(null);
        }
        ImageView day_iv = (ImageView) room_day_v.findViewById(R.id.room_day_iv);
        TextView day_tv = (TextView) room_day_v.findViewById(R.id.room_day_tv);
        TextView day_info_tv = (TextView) room_day_v.findViewById(R.id.room_day_info_tv);
        TextView day_timer_tv = (TextView) room_day_v.findViewById(R.id.room_day_timer_tv);
        if (!TextUtils.isEmpty(str1)) {
            if (str1.contains("天")) {
                day_iv.setImageResource(R.mipmap.room_day);
            } else if (str1.contains("夜")) {
                day_iv.setImageResource(R.mipmap.room_night);
            } else if (str1.contains("死")) {
                day_iv.setImageResource(R.mipmap.room_udie);
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            day_info_tv.setText(str2);
        }
        if (!TextUtils.isEmpty(str3)) {
            day_timer_tv.setText(str3);
        }

        room_day_pw.setOutsideTouchable(false);
        room_day_pw.setFocusable(false);

        room_day_pw.showAsDropDown(view);
    }


    public static void showWaitDialog(Activity activity, View view, long time) {
        if (!AppUtil.isSafe(activity)) {
            return;
        }
        if (room_day_pw == null) {
            room_day_v = LayoutInflater.from(activity).inflate(R.layout.pw_room_day, null);
            room_day_pw = new PopupWindow(room_day_v, view.getWidth(), WindowManager.LayoutParams.WRAP_CONTENT, true);
            room_day_pw.setTouchable(false);
            room_day_pw.setBackgroundDrawable(null);
        }
        TextView day_timer_tv = (TextView) room_day_v.findViewById(R.id.room_day_timer_tv);

        if (time > 0) {
            day_timer_tv.setText(time + "s");
        }
        room_day_pw.setOutsideTouchable(false);
        room_day_pw.setFocusable(false);

        room_day_pw.showAsDropDown(view);

    }

    /**
     * 显示自定义的加载对话框
     */
    public static void showLoadDialog(Activity context) {
        if (context == null) {
            return;
        }
        if (pw == null) {
//            View v = LayoutInflater.from(context).inflate(R.layout.wm_dialog_progress, null);
            View v = new View(context);
            pw = new PopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
            pw.setTouchable(false);
//            pw.setAnimationStyle(R.style.AnimBottom);
            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        pw.showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 显示系统加载对话框
     */
    public static void showProgressDialog(Activity context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在加载，请稍候...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void showWolfKillDialog(Activity activity) {


    }

    /**
     * 隐藏所有对话框
     */
    public static void dismissDialog(Activity activity) {
        if (room_day_pw != null) {
            if (activity != null && !activity.isFinishing()) {
                room_day_pw.dismiss();
            }
        }
        if (progressDialog != null) {

            progressDialog.dismiss();
        }
        if (pw != null) {
            pw.dismiss();
        }
    }

    /**
     * 显示去应用市场评分对话框
     */
    public static void showAppMarkDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("去应用市场为本软件评分？");
        builder.setNeutralButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    //App.showToast("未发现已安装的应用市场");
                    e.printStackTrace();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 显示缺少权限提示信息对话框
     */
    public static void showMissingPermissionDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限。");
        builder.setNeutralButton("取消", null);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 显示联网设置对话框
     */
    public static void showNetSettingDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("联网状态下才能体验本应用功能。若未开启网络：\n\n请点击\"设置\"去开启网络连接。");
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                context.startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public static void showOperateDialog(Activity activity, String title, short cid, Map<String, Integer> param, int time) {

        final EditText et = new EditText(activity);

        new android.support.v7.app.AlertDialog.Builder(activity).setTitle(title)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String replyStr = et.getText().toString();
                        if (replyStr.trim().length() == 0) {
                            replyStr = String.valueOf(0);
                        }
                        if (!MathUtil.isNumeric(replyStr) || (Integer.parseInt(replyStr) < 0 || Integer.parseInt(replyStr) > 12)) {
                            new android.support.v7.app.AlertDialog.Builder(activity).setTitle("提示").setMessage("请输入正确玩家编号")
                                    .setPositiveButton("确定", null).show();
                            return;
                        }
                        int reply = Integer.parseInt(replyStr);
                        param.put("content", reply);
                        IMClientUtil.sendMsg(ProtocolConstant.SID_GAME, cid, param);
                        showWaitOtherDialog(activity, time);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public static void showWaitOtherDialog(Activity activity, int time) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity, android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("请等待其他玩家操作...");
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    public static void showWitchSaveDialog(Activity activity, int killed, Map<String, Integer> param, int time, Witch witch) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity, android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle(killed + "号玩家死亡，你是否要救？")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        witch.usePanacea();
                        param.put("content", killed);
                        IMClientUtil.sendMsg(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_SAVE_REQ, param);
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(witch.hasPoison()){
                            showWitchPoisonDialog(activity, param, time,witch);
                        }
                    }
                });
        builder.show();
    }

    public static void showWitchPoisonDialog(Activity activity, Map<String, Integer> param, int time,Witch witch) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity, android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("您是否要毒人？")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        witch.usePoison();
                        String title = "请选择您要毒的人：";
                        showOperateDialog(activity, title, ProtocolConstant.CID_GAME_POISON_REQ, param, time);
                    }
                })
                .setNegativeButton("否",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showWaitOtherDialog(activity, time);
                    }
                } );
        builder.show();
    }

    public static void showToast(Activity activity, String str) {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
        if (Looper.myLooper() == null) {
            Looper.loop();
        }
    }

    public static void showToast(Activity activity, String str, final int time) {
        Toast toast = Toast.makeText(activity, str, Toast.LENGTH_LONG);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, time);
    }


}
