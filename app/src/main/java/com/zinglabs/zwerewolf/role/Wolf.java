package com.zinglabs.zwerewolf.role;

import java.util.Random;

/**
 * 狼人
 * Created by Administrator on 2017/3/9.
 */

public class Wolf extends Role {
    public static final String NAME = "狼人";
    public static final long ACTION_TIME = 15 * 1000;

    public Wolf(int number) {
        this.number = number;
    }

    /**
     * 独有行动是否弃权
     */
    @Override
    public int doRoleAction() {
        int action = new Random().nextInt(5);
        if (action == 0) {
            action = ACTION_GIVE_UP;
        } else {
            action = ACTION_CONFIRM;
        }
        return action;
    }


    @Override
    public void onStateChange() {
    }

    @Override
    public String getName() {
        return NAME;
    }

}
