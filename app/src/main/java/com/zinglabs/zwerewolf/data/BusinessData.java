package com.zinglabs.zwerewolf.data;

/**
 * 业务通用消息体
 * @user wangtonghe
 * @date 2017/7/24
 * @email wthfeng@126.com
 */

public class BusinessData {

    private int fromId; //发送者的ID，为0表示服务器
    private int  reply; //回复码

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }
}
