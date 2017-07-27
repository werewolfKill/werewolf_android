package com.zinglabs.zwerewolf.role;

/**
 * @user wangtonghe
 * @date 2017/7/27
 * @email wthfeng@126.com
 */

public class Guard extends Role {
    public static final String NAME = "守卫";
    private boolean isHunting;//是否行动中
    public static final long ACTION_TIME = 15 * 1000;



    public Guard() {
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
