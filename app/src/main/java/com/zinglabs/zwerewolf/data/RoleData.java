package com.zinglabs.zwerewolf.data;

/**
 * 用于存放角色信息
 * Created by Administrator on 2017/3/8.
 */

public class RoleData {
    private String headImgUrl;//头像
    private int number;//号码
    private String nickName;//昵称
    private int state;//状态，未准备，准备，死亡，发言
    private boolean isOwner;//是否房主

    public RoleData() {
    }

    /**
     * 模拟生成角色信息
     *
     * @param headImgUrl
     * @param number
     * @param nickName
     * @param state
     * @param isOwner
     */
    public RoleData(String headImgUrl, int number, String nickName, int state, boolean isOwner) {
        this.headImgUrl = headImgUrl;
        this.number = number;
        this.nickName = nickName;
        this.state = state;
        this.isOwner = isOwner;
    }

    @Override
    public String toString() {
        return
                "headImgUrl='" + headImgUrl + '\'' +
                        ", number='" + number + '\'' +
                        ", nickName='" + nickName + '\'' +
                        ", state='" + state + '\'' +
                        ", isOwner=" + isOwner;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }
}
