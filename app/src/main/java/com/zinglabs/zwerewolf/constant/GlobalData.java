package com.zinglabs.zwerewolf.constant;

import android.app.Application;

import com.zinglabs.zwerewolf.entity.User;

/**
 * Created by huihui on 2017/7/20.
 */

public class GlobalData extends Application {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
