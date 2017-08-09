package com.zinglabs.zwerewolf.utils;

import com.zinglabs.zwerewolf.config.Constants;
import com.zinglabs.zwerewolf.role.UserRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    /**
     * 调整发言顺序
     * @param speaker 所有发言者
     * @param start 开始发言的人
     * @return 调整后发言者
     */
    public static List<Integer> adjustSpeakOrder(List<Integer> speaker,int start){
        Collections.sort(speaker);
        int size = speaker.size();
        List<Integer> list = new ArrayList<>(size);
        int index = 0;
        for (int i = 0; i < size ; i++) {
            if(start>=speaker.get(i)){
                if(start==speaker.get(i)){
                    index = i;
                }
                list.add(speaker.get(i));
            }
        }
        if(index>1){
            list.addAll(speaker.subList(0,index));
        }
        return list;
    }
}


