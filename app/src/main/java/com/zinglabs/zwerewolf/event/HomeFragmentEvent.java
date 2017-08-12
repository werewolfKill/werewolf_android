package com.zinglabs.zwerewolf.event;

import com.zinglabs.zwerewolf.entity.Room;

/**
 * 主页面Event通知类
 * @user wangtonghe
 * @date 2017/7/26
 * @email wthfeng@126.com
 */

public class HomeFragmentEvent {

    public static final int CREATE_ROOM_SUC = 1;

    public static final int CREATE_ROOM_FAIL = 2;

    public static final int SEARCH_ROOM_SUC = 3;

    public static final int SEARCH_ROOM_NOT_EXIST = 4;

    public static final int SEARCH_ROOM_ALREADY_FULL = 5;

    public static final int SEARCH_ROOM_FAIL = 6;

    public static final int ENTER_ROOM =7;






    private int roomId;

    private int userId;  //该用户id

    private int modelId;  //游戏模式

    private Room room;



    /**
     * 命令模式
     */
    private int code;

    public HomeFragmentEvent(){

    }

    public HomeFragmentEvent(int roomId, int userId, int modelId,int code) {
        this.roomId = roomId;
        this.userId = userId;
        this.modelId = modelId;
        this.code=code;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }
}
