package com.zinglabs.zwerewolf.entity;

import java.util.Set;

/**
 * 房间信息
 * @user wangtonghe
 * @date 2017/7/27
 * @email wthfeng@126.com
 */

public class Room {

    /**
     * 房间id
     */
    private int roomId;

    /**
     * 房间模式
     */
    private int modelId;

    /**
     * 房主id
     */
    private int ownerId;

    /**
     * 房间玩家集合
     */
    private Set<User> userSet;

    /**
     * 当前用户id
     */
    private int curUserId;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Room(int roomId, int modelId, int ownerId, int curUserId) {
        this.roomId = roomId;
        this.modelId = modelId;
        this.ownerId = ownerId;
        this.curUserId = curUserId;
    }

    public Room(int roomId, int modelId) {
        this.roomId = roomId;
        this.modelId = modelId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }

    public int getCurUserId() {
        return curUserId;
    }

    public void setCurUserId(int curUserId) {
        this.curUserId = curUserId;
    }

    public boolean isOwner(){

        return this.ownerId!=0&&this.curUserId==this.ownerId;
    }
}
