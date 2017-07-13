package com.zinglabs.zwerewolf.role;

/**
 * 预言家
 * Created by Administrator on 2017/3/9.
 */

public class Prophet extends Role {
    public static final String NAME = "预言家";
    public static final long ACTION_TIME = 15 * 1000;

    public Prophet(int number) {
        this.number = number;
    }

    @Override
    public int doRoleAction() {
        return ACTION_KILL;
    }

    @Override
    public void onStateChange() {
    }


    @Override
    public String getName() {
        return NAME;
    }

}
