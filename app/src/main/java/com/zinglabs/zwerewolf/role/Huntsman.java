package com.zinglabs.zwerewolf.role;



/**
 * 猎人
 * Created by Administrator on 2017/3/9.
 */

public class Huntsman extends Role {
    public static final String NAME = "猎人";
    private boolean isHunting;//是否行动中
    public static final long ACTION_TIME = 15 * 1000;

    public Huntsman(int number) {
        this.number = number;
    }

    @Override
    public int doRoleAction() {
        return ACTION_KILL;
    }

    @Override
    public void onStateChange() {
       // LogUtil.e("猎人死亡触发技能");
    }

    public boolean isHunting() {
        return isHunting;
    }

    public void hunting() {
        isHunting = false;
    }

    @Override
    public String getName() {
        return NAME;
    }

}
