package com.zinglabs.zwerewolf.entity;

import com.zinglabs.zwerewolf.config.Constants;
import com.zinglabs.zwerewolf.role.UserRole;
import com.zinglabs.zwerewolf.utils.RoomUtil;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * 当前用户位置
     */
    private int curUserPos;

    /**
     * 当前用户人数
     */
    private int curNumber;

    /**
     * 房间玩家集合
     */
    private Map<Integer,UserRole> players;

    /**
     * 死亡名单
     */
    private Map<Integer,List<Integer>> deadList = new HashMap<>();


    /**
     * 游戏是否结束标识
     */
    private int over;
    /**
     * 表示第几天
     */
    private int bout ;

    /**
     * 房间人数
     */
    private int number;

    /**
     * 表示警长
     */
    private int chief;

    private boolean atChiefVote;

    public boolean isAtChiefVote() {
        return atChiefVote;
    }

    public void setAtChiefVote(boolean atChiefVote) {
        this.atChiefVote = atChiefVote;
    }

    public int getChief() {
        return chief;
    }

    /**
     * 幸存者列表
     */
    private List<Integer> liveList  = new ArrayList<>();

    public void setChief(int chief) {
        this.chief = chief;
    }

    public int getNumber() {
        return number;
    }

    public List<Integer> getLiveList() {
        Collections.sort(liveList);
        return  liveList;
    }



    private List<Integer> policeList = new ArrayList<>();

    public int getOver() {
        return over;
    }

    public void setOver(int over) {
        this.over = over;
    }

    public boolean isOver(){
        return over!= Constants.GAME_STATUS_PROCESS;
    }

    public Map<Integer, List<Integer>> getDeadList() {
        return deadList;
    }

    public void addDeadList(int bout, List<Integer> list) {
        this.deadList.put(bout,list);
        for(Integer id:list){
            liveList.remove(id);
        }
    }

    public int getCurUserPos() {
        return curUserPos;
    }

    public void setCurUserPos(int curUserPos) {
        this.curUserPos = curUserPos;
    }

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
        if(!policeList.contains(pos)){
            this.policeList.add(pos);
        }
    }

    public void quitPoliceList(int pos){
        if(policeList.contains(pos)){
            policeList.remove((Integer) pos);
        }
    }

    public void setPlayers(Map<Integer, UserRole> players) {

        this.players = players;
        for(UserRole ur :players.values()){
            liveList.add(ur.getPosition());
        }
    }

    public void addPlayer(int userId,int pos){
        UserRole ur = new UserRole();
        ur.setUserId(userId);
        ur.setPosition(pos);
        this.players.put(userId,ur);
        liveList.add(pos);
    }
}



