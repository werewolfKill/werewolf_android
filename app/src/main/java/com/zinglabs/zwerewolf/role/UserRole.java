package com.zinglabs.zwerewolf.role;

import java.io.Serializable;

/**
 * @user wangtonghe
 * @date 2017/7/29
 * @email wthfeng@126.com
 */

public class UserRole implements Serializable{
    private int userId;

    private String userName;

    private Role role;

    /**
     * 玩家在房间的位置编号
     */
    private int position;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRole userRole = (UserRole) o;

        return userId == userRole.userId;
    }

    @Override
    public int hashCode() {
        return userId;
    }
}
