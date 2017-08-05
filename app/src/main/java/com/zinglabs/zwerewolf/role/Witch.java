package com.zinglabs.zwerewolf.role;

import java.util.Random;

/**
 * 女巫
 * Created by Administrator on 2017/3/9.
 */

public class Witch extends Role {
    public static final String NAME = "女巫";
    public static final long ACTION_TIME = 15 * 1000;

    private int panacea = 1;//初始灵药数目
    private int poison = 1;//初始毒药数目

    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int doRoleAction() {
        //1/3几率弃权
        int action = new Random().nextInt(3);
        if (action == 0) {
            action = ACTION_GIVE_UP;
        } else {
            action = ACTION_KILL;
        }
        return action;
    }

    public boolean hasPanacea() {
        return panacea > 0;
    }

    public boolean usePanacea() {
        if (panacea > 0) {
            panacea--;
            return true;
        }
        return false;
    }

    public boolean hasPoison() {
        return poison > 0;
    }

    public boolean usePoison() {
        if (poison > 0) {
            poison--;
            return true;
        }
        return false;
    }

    @Override
    public void onStateChange() {
    }

    @Override
    public String getName() {
        return NAME;
    }
}
