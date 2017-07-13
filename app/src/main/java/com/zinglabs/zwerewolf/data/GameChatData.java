package com.zinglabs.zwerewolf.data;

/**
 * Created by Administrator on 2017/3/8.
 */

public class GameChatData {
    //定义消息类型
    public static final int DATE = 0xc0;//显示时间
    public static final int ENTRY = 0xc1;//显示玩家进出房间
    public static final int CHAT = 0xc2;//显示聊天内容，分为系统、其他玩家、本玩家
    //聊天角色
    public static final String SYSTEM_CHAT = "LRSwzc25151";
    public static final String SYSTEM_NAME = "法官";//系统角色-法官

    public GameChatData() {
    }

    private int type;//内容类型
    private String date;//发送时间
    private String from;//发件人
    private String to;//收件人
    private String text;//文本

    public GameChatData(int type, String date, String from, String to, String text) {
        this.type = type;
        this.date = date;
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
