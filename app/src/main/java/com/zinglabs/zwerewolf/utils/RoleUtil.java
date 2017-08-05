package com.zinglabs.zwerewolf.utils;

import com.zinglabs.zwerewolf.config.Constants;
import com.zinglabs.zwerewolf.role.Guard;
import com.zinglabs.zwerewolf.role.Huntsman;
import com.zinglabs.zwerewolf.role.Idiot;
import com.zinglabs.zwerewolf.role.Prophet;
import com.zinglabs.zwerewolf.role.Role;
import com.zinglabs.zwerewolf.role.Villager;
import com.zinglabs.zwerewolf.role.Witch;
import com.zinglabs.zwerewolf.role.Wolf;


/**
 * 定义角色对应关系
 *
 * @user wangtonghe
 * @date 2017/7/27
 * @email wthfeng@126.com
 */

public class RoleUtil {

    public static Role getRole(int roleCode) {
        Role role = null;
        switch (roleCode) {
            case Constants.ROLE_CODE_OF_VILLAGER:
                role = new Villager();
                break;
            case Constants.ROLE_CODE_OF_GUARD:
                role = new Guard();
                break;

            case Constants.ROLE_CODE_OF_HUNTSMAN:
                role = new Huntsman();
                break;

            case Constants.ROLE_CODE_OF_IDIOT:
                role = new Idiot();
                break;

            case Constants.ROLE_CODE_OF_PROPHET:
                role = new Prophet();
                break;

            case Constants.ROLE_CODE_OF_WITCH:
                role = new Witch();
                break;

            case Constants.ROLE_CODE_OF_WOLF:
                role = new Wolf();
                break;

        }
        return role;

    }

    public static long getWaitTime(int roleCode, int modal) {
        int num = RoomUtil.getNumByModal(modal);
        long action_time = 0, tmp_time = 0;

        switch (roleCode) {
            case Constants.ROLE_CODE_OF_GUARD:
                action_time = Prophet.ACTION_TIME + Witch.ACTION_TIME + Wolf.ACTION_TIME;
                break;
            case Constants.ROLE_CODE_OF_PROPHET:
                tmp_time =  Witch.ACTION_TIME + Wolf.ACTION_TIME;
                if (num < 12) {
                    action_time = tmp_time;
                } else if (num == 12&&modal==Constants.MODEL_12_YNLS) {
                    action_time = tmp_time+ Guard.ACTION_TIME;
                }else {
                    action_time = tmp_time;
                }
                break;

            case Constants.ROLE_CODE_OF_WITCH:
                tmp_time = Prophet.ACTION_TIME + Wolf.ACTION_TIME;
                if (num < 12) {
                    action_time = tmp_time;
                } else if (num == 12&&modal==Constants.MODEL_12_YNLS) {
                    action_time = tmp_time+ Guard.ACTION_TIME;
                }else{
                    action_time = tmp_time;

                }
                break;

            case Constants.ROLE_CODE_OF_WOLF:
                tmp_time = Prophet.ACTION_TIME + Witch.ACTION_TIME ;
                if (num < 12) {
                    action_time = tmp_time;
                } else if (num == 12&&modal==Constants.MODEL_12_YNLS) {
                    action_time = tmp_time+ Guard.ACTION_TIME;
                }else{
                    action_time = tmp_time;
                }
                break;
            default:
                tmp_time = Prophet.ACTION_TIME + Witch.ACTION_TIME +Wolf.ACTION_TIME;
                if (num < 12) {
                    action_time = tmp_time;
                } else if (num == 12&&modal==Constants.MODEL_12_YNLS) {
                    action_time = tmp_time+ Guard.ACTION_TIME;
                }else{
                    action_time = tmp_time;
                }
        }
        return action_time;


    }


}
