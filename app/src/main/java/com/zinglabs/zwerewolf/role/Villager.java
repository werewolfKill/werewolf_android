package com.zinglabs.zwerewolf.role;

/**
 * 村民
 * Created by Administrator on 2017/3/9.
 */

public class Villager extends Role {
    public static final String NAME = "村民";

    public Villager(int number) {
        this.number = number;
    }

    @Override
    public int doRoleAction() {
        return ACTION_GIVE_UP;
    }

    @Override
    public void onStateChange() {
    }

    @Override
    public String getName() {
        return NAME;
    }

}
