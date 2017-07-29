package com.zinglabs.zwerewolf.entity;

import com.zinglabs.zwerewolf.role.UserRole;

import java.io.Serializable;
import java.util.Map;

/**
 * 房间信息
 * @user wangtonghe
 * @date 2017/7/27
 * @email wthfeng@126.com
 */

public class Room implements Serializable {

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
     * 当前用户id
     */
    private int curUserId;

    /**
     * 当前用户人数
     */
    private int curNumber;

    /**
     * 房间玩家集合
     */
    private Map<Integer,UserRole> players;


    public int getCurNumber() {
        return curNumber;
    }

    public void setCurNumber(int curNumber) {
        this.curNumber = curNumber;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Room(){

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


    public int getCurUserId() {
        return curUserId;
    }

    public void setCurUserId(int curUserId) {
        this.curUserId = curUserId;
    }

    public boolean isOwner(){

        return this.ownerId!=0&&this.curUserId==this.ownerId;
    }

    public Map<Integer, UserRole> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Integer, UserRole> players) {
        this.players = players;
    }
}
