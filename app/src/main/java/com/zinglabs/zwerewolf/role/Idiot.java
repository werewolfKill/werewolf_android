package com.zinglabs.zwerewolf.role;

/**
 * 角色 白痴类
 * @user wangtonghe
 * @date 2017/7/27
 * @email wthfeng@126.com
 */

public class Idiot extends Role {

    public static final String NAME = "白痴";
    public static final long ACTION_TIME = 15 * 1000;


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
