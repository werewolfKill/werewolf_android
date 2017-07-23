package com.zinglabs.zwerewolf.role;

import java.util.Random;

/**
 * 基础角色
 * Created by Administrator on 2017/3/9.
 */

public abstract class Role {
    public static final int ID_BASE = 0xde1;//基础ID，最好避免使用
    public static final int ID_VOTE = 0xde2;//公开投票
    public static final int ID_TALK = 0xde3;//公开讨论
    public static final int TIME_VOTE = 15000;//每个人15s投票时间
    public static final int TIME_TALK = 45000;//每个人45s说话时间

    public static final int WIN_WOLF = 0xbb1;
    public static final int WIN_PERSON = 0xbb2;
    public static final int WIN_NO = 0xbb3;

    //定义行为
    public static final int ACTION_KILL = 0x1a;//杀人-调查-猎杀
    public static final int ACTION_SAVE = 0x1b;//救人
    public static final int ACTION_POISON = 0x1c;//毒杀

    public static final int ACTION_CONFIRM = 0x1d;//动
    public static final int ACTION_GIVE_UP = 0x1e;//不动
    //定义状态
    public static final String STATE_ACTION = "行动";//行动中
    public static final String STATE_HOLD_ON = "存活";//待机中
    public static final String STATE_VOTE = "投票";//投票中
    public static final String STATE_TALK = "说话";//说话中
    public static final String STATE_DIE_VOTE = "票杀";//票死中
    public static final String STATE_DIE_POISON = "毒杀";//毒死中
    public static final String STATE_DIE_HUNTING = "猎杀";//猎死中
    public static final String STATE_DIE_WOLF_KILL = "狼杀";//狼死中
    public static final String STATE_LOST = "掉线";//掉线中

    protected boolean isAlive = true;

    public boolean isAlive() {
        return isAlive;
    }

    /**
     * 角色公共行动最长花费时间，默认表示投票时间
     */
    public static long getCommonActionTime() {
        return 45 * 1000;
    }

    /**
     * 角色独有行动最长花费时间
     */
    public static long getRoleActionTime() {
        return 15 * 1000;
    }

    /**
     * 返回该角色日常结果
     */
    public static int doCommonAction() {
        int action = new Random().nextInt(5);
        if (action == 0) {
            action = ACTION_GIVE_UP;
        } else {
            action = ACTION_KILL;
        }
        return action;
    }

    /**
     * 返回该角色行动结果
     */
    public abstract int doRoleAction();

    protected String state = STATE_HOLD_ON;//当前状态值


    public void setState(String state) {
        this.state = state;

        switch (state) {
            case STATE_ACTION:
            case STATE_HOLD_ON:
            case STATE_VOTE:
            case STATE_TALK:
                isAlive = true;
                if (onRoleStateChangeListener != null) {
                    onRoleStateChangeListener.onStateChange();
                }
                break;
            case STATE_DIE_VOTE:
            case STATE_DIE_POISON:
            case STATE_DIE_HUNTING:
            case STATE_DIE_WOLF_KILL:
            case STATE_LOST:
                isAlive = false;
                if (onRoleStateChangeListener != null) {
                    onRoleStateChangeListener.onStateChange();
                }
                break;
        }

        onStateChange();
    }

    public String getState() {
        return state;
    }

    /**
     * 状态变化
     */
    public abstract void onStateChange();

    protected int number;

    public int getNumber() {
        return number;
    }

    /**
     * 获得角色名
     */
    public abstract String getName();

    public static Role newInstance(String name, int number) {
        switch (name) {
            case Wolf.NAME:
                return new Wolf(number);
            case Witch.NAME:
                return new Witch(number);
            case Prophet.NAME:
                return new Prophet(number);
            case Villager.NAME:
                return new Villager(number);
            case Huntsman.NAME:
                return new Huntsman(number);
            default:
                return null;
        }
    }

    private OnRoleStateChangeListener onRoleStateChangeListener;

    public void setOnRoleStateChangeListener(OnRoleStateChangeListener onRoleStateChangeListener) {
        this.onRoleStateChangeListener = onRoleStateChangeListener;
    }

    public interface OnRoleStateChangeListener {
        void onStateChange();
    }
}
