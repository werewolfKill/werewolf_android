package com.zinglabs.zwerewolf.constant;

import android.app.Application;

import com.zinglabs.zwerewolf.entity.Room;
import com.zinglabs.zwerewolf.entity.User;

/**
 * Created by huihui on 2017/7/20.
 */

public class GlobalData extends Application {

    /**
     * 用户数据信息
     */
    private User user;


    /**
     * 房间信息
     */
    private Room room;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
