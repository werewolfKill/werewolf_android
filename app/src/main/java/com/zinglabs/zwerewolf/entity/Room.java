package com.zinglabs.zwerewolf.entity;

import com.zinglabs.zwerewolf.role.UserRole;
import com.zinglabs.zwerewolf.utils.RoomUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    /**
     * 游戏是否结束
     */
    private boolean isOver;

    /**
     * 房间人数
     */
    private int number;

    public int getNumber() {
        return number;
    }

    /**
     * 死亡名单
     */
    private List<Integer> deadList = new ArrayList<>();

    private List<Integer> policeList = new ArrayList<>();

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    public List<Integer> getDeadList() {
        return deadList;
    }

    public void addDeadList(Integer[] arr) {
        this.deadList.addAll(Arrays.asList(arr));
    }

    /**
     * 表示第几天
     */
    private int bout ;

    public int getBout() {
        return bout;
    }

    public void setBout(int bout) {
        this.bout = bout;
    }

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
        this.number = RoomUtil.getNumByModal(modelId);
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

    public List<Integer> getPoliceList() {
        return policeList;
    }

    public void addPoliceList(int pos) {
        this.policeList.add(pos);
    }

    public void setPlayers(Map<Integer, UserRole> players) {
        this.players = players;
    }

    public void addPlayer(int userId,int pos){
        UserRole ur = new UserRole();
        ur.setUserId(userId);
        ur.setPosition(pos);
        this.players.put(userId,ur);
    }
}



