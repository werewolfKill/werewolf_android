package com.zinglabs.zwerewolf.controller;

/**
 * Created by Administrator on 2017/3/9.
 */

public interface GameListener {
    /**
     * 游戏开始时
     */
    void onGameStart();

    /**
     * 游戏结束时
     */
    void onGameOver();

    /**
     * 游戏回合初始
     */
    void onBoutClear();

    /**
     * 游戏回合变动时
     *
     * @param bout 表示变为第几个回合
     */
    void onBoutChange(int bout);
}
