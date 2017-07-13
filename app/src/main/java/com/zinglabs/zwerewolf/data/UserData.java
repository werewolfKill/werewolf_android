package com.zinglabs.zwerewolf.data;

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


    private UserData() {
    }
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


    private static UserData userData;

    /**
     * 模拟用户信息
     *
     * @return
     */
    public static UserData user() {
        if (userData == null) {
            userData = new UserData();
            userData.setUserId("25151");
            userData.setHeadImgUrl("http://www.wzc25151.com/myimg/1.jpg");
            userData.setNickName("wzc25151");
            userData.setCion("666");
        }
        return userData;
    }
}
