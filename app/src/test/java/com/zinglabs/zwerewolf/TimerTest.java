package com.zinglabs.zwerewolf;


import java.util.Timer;
import java.util.TimerTask;

/**
 * @user wangtonghe
 * @date 2017/8/30
 * @email wthfeng@126.com
 */

public class TimerTest {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("哈哈哈");

            }
        },2000);
    }
}
