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
    public static final int ROOM_ENTER = 0xb5;//玩家进入房间


    public static final int GAME_LEAVE=0xc0; //玩家离开
    public static final int GAME_READY=0xc1; //玩家准备
    public static final int GAME_START=0xc2; //房主开始游戏
    public static final int GAME_NOT_ENOUGH_NUM=0xc3; //没有足够人数
    public static final int GAME_START_FAIL=0xc4; //未知原因导致开始游戏失败
    public static final int GAME_NOTIFY_WITCH = 0xc5; //通知女巫
    public static final int GAME_DAWN = 0xc6; //天亮了
    public static final int GAME_ASK_CHIEF = 0xc7; //申请警长
    public static final int GAME_POLICE_SPEAKING = 0xc8; //警上发言
    public static final int GAME_VERIFY= 0xc9; //预言家验人
    public static final int GAME_OTHER_ENTER= 0xca; //他人进入房间
    public static final int GAME_VOTE_CHIEF= 0xcb; //投票选出警长
    public static final int GAME_SET_CHIEF= 0xcc; //设置警长
    public static final int GAME_SPEAK= 0xcd; //发言
    public static final int GAME_CHIEF_SUM_TICKET= 0xce; //警长归票
    public static final int GAME_VOTE= 0xcf; //投票
    public static final int GAME_VOTE_RESULT= 0xd0; //投票结果
    public static final int GAME_DARK= 0xd1; //天黑



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
