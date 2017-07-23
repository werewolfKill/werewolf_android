package com.zinglabs.zwerewolf.data;

import android.app.Application;

/**
 * 用户信息
 * Created by Administrator on 2017/3/8.
 */

public class UserData {
    private String userId;//用户id
    private String headImgUrl;//头像
    private String nickName;//昵称
    private String level;//等级
    private String cion;//金币

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCion() {
        return cion;
    }

    public void setCion(String cion) {
        this.cion = cion;
    }

}
