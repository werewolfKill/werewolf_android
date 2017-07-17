package com.zinglabs.zwerewolf.entity;

/**
 *
 * 一个聊天项，首页的聊天列表
 *
 * author: vector.huang
 * date：2016/4/19 21:15
 */
public class Chat {

    private String headImage; //聊天头像
    private int unreadCount;//未读消息数
    private int id;
    private String name;
    private ChatMessage newestMsg;//最新的一条消息

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatMessage getNewestMsg() {
        return newestMsg;
    }

    public void setNewestMsg(ChatMessage newestMsg) {
        this.newestMsg = newestMsg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
