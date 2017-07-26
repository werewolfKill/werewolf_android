package com.zinglabs.zwerewolf.event;

import android.text.TextUtils;

/**
 * Created by wangxiangbo on 2016/7/18.
 */
public class MsgEvent {
    public static final int ROOM_CHAT = 0xb0;//游戏界面聊天内容
    public static final int ROOM_START = 0xb1;//游戏开局
    public static final int ROOM_OVER = 0xb2;//游戏结束
    public static final int ROOM_ROLE = 0xb3;//游戏角色分配
    public static final int ROOM_ROLE_STATE_CHANGE = 0xb4;//角色状态变化，掉线，死亡
    public static final int ROOM_CREATE_SUC = 0xb5;  //创建房间成功
    public static final int ROOM_CREATE_FAIL = 0xb6;  //创建房间失败

    public static final int GAME_READY=0xc1; //玩家准备


    private int msgType;
    private String msgStr;
    private Object obj;

    public MsgEvent(int msgType) {
        this(msgType, null, null);
    }

    public MsgEvent(int msgType, String msgStr) {
        this(msgType, msgStr, null);
    }

    public MsgEvent(int msgType, String msgStr, Object obj) {
        this.msgType = msgType;
        this.msgStr = msgStr;
        this.obj = obj;
    }

    public int getMsgType() {
        return msgType;
    }

    public String getMessageStr() {
        return TextUtils.isEmpty(msgStr) ? "" : msgStr;
    }

    public Object getObj() {
        return obj;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsgStr() {
        return msgStr;
    }

    public void setMsgStr(String msgStr) {
        this.msgStr = msgStr;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
