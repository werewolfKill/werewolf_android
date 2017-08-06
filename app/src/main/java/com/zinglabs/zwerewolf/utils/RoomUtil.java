package com.zinglabs.zwerewolf.utils;

import com.zinglabs.zwerewolf.config.Constants;
import com.zinglabs.zwerewolf.role.UserRole;

import java.util.Map;

/**
 * @user wangtonghe
 * @date 2017/7/29
 * @email wthfeng@126.com
 */

public class RoomUtil {
    public static int getNumByModal(int modalId) {
        int number ;
        switch (modalId) {
            case Constants.MODEL_9:
                number = 9;
                break;
            case Constants.MODEL_10:
                number = 10;
                break;
            case Constants.MODEL_12_YNLB:
                number = 12;
                break;
            case Constants.MODEL_12_YNLS:
                number = 12;
                break;
            default:
                number = 12;
                break;
        }
        return number;
    }


}


