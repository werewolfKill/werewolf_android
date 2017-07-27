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

    public static final Role getRole(int roleCode) {
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


}
