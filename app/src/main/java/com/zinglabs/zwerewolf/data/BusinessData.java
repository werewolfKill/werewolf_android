package com.zinglabs.zwerewolf.data;

import java.util.Map;
import java.util.Objects;

/**
 * 业务通用消息体
 * @user wangtonghe
 * @date 2017/7/24
 * @email wthfeng@126.com
 */

public class BusinessData {

    private int fromId; //发送者的ID，为0表示服务器
    private int  reply; //回复码
    private int over;  //游戏是否结束，用于投票时的参数

    public int getOver() {
        return over;
    }

    public void setOver(int over) {
        this.over = over;
    }

    private Map<String,Object> param;  //其他参数

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

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
