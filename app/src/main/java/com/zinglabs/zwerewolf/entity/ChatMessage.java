package com.zinglabs.zwerewolf.entity;

/**
 *
 * 一条聊天消息
 *
 * author: vector.huang
 * date：2016/4/19 21:20
 */
public class ChatMessage {

    private int fromId; //发送者的ID
    private int toId; //接受者的ID，用户id，群id
    private int type; //消息类型
    private String content;//聊天消息
    private long sendTime;//发送数据，毫秒数

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
