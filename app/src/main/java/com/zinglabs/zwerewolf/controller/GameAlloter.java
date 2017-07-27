package com.zinglabs.zwerewolf.controller;


import com.zinglabs.zwerewolf.role.Huntsman;
import com.zinglabs.zwerewolf.role.Prophet;
import com.zinglabs.zwerewolf.role.Role;
import com.zinglabs.zwerewolf.role.Villager;
import com.zinglabs.zwerewolf.role.Witch;
import com.zinglabs.zwerewolf.role.Wolf;
import com.zinglabs.zwerewolf.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * 游戏角色分配器
 * Created by Administrator on 2017/3/9.
 */

public class GameAlloter {

    /**
     * 返回角色数量分配Map
     */
    public static Map<String, Integer> getElementMap(int players) {
        Map<String, Integer> elementMap = new HashMap<>();
        if (players == 6) {
            elementMap.put(Wolf.NAME, 2);
            elementMap.put(Witch.NAME, 1);
            elementMap.put(Prophet.NAME, 1);
            elementMap.put(Huntsman.NAME, 1);
            elementMap.put(Villager.NAME, 1);
        }
        if (players >= 7 && players <= 10) {
            elementMap.put(Wolf.NAME, 3);
            elementMap.put(Witch.NAME, 1);
            elementMap.put(Prophet.NAME, 1);
            elementMap.put(Huntsman.NAME, 1);
            elementMap.put(Villager.NAME, players - 6);
        }
        if (players >= 11 && players <= 14) {
            elementMap.put(Wolf.NAME, 4);
            elementMap.put(Witch.NAME, 1);
            elementMap.put(Prophet.NAME, 1);
            elementMap.put(Huntsman.NAME, 1);
            elementMap.put(Villager.NAME, players - 7);
        }
        if (players >= 15 && players <= 16) {
            elementMap.put(Wolf.NAME, 5);
            elementMap.put(Witch.NAME, 1);
            elementMap.put(Prophet.NAME, 1);
            elementMap.put(Huntsman.NAME, 1);
            elementMap.put(Villager.NAME, players - 8);
        }
        return elementMap;
    }

    /**
     * 返回角色详细部署Map
     */
    public static Map<Integer, Role> getDeployMap(Map<String, Integer> elementMap, int players) {
        Map<Integer, Role> deployMap = new TreeMap<Integer, Role>();
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= players; i++) {
            list.add(i);
        }
        for (Map.Entry<String, Integer> entry : elementMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                Integer index = list.remove(new Random().nextInt(list.size()));
//                deployMap.put(index, Role.newInstance(entry.getKey(), index));
            }
        }
        String srt_deploy = "";
        for (Map.Entry<Integer, Role> entry : deployMap.entrySet()) {
            srt_deploy += entry.getKey() + "是" + entry.getValue().getName() + "，";
        }
        LogUtil.e(srt_deploy);
        return deployMap;
    }

    /**
     * 更新幸存者集合
     */
    public static List<Integer> updateAliver(List<Integer> aliveList, Map<Integer, Role> deployMap) {
        if (aliveList == null) {
            aliveList = new ArrayList<Integer>();
        } else {
            aliveList.clear();
        }
        for (Map.Entry<Integer, Role> entry : deployMap.entrySet()) {
            if (entry.getValue().isAlive()) {
                aliveList.add(entry.getKey());
            }
        }
        LogUtil.e("幸存者" + aliveList.toString());
        return aliveList;
    }

    /**
     * 从幸存者中选出一人，指定排除号码
     */
    public static int chooseAliver(List<Integer> aliveList, Integer outlier) {
        while (true) {
            int i = new Random().nextInt(aliveList.size());
            if (outlier.equals(aliveList.get(i))) {
                continue;
            }
            return aliveList.get(i);
        }
    }

    /**
     * 从一个号码后挑选未死玩家开始发言
     */
    public static int pickSpeak(List<Integer> aliveList, List<Integer> nightDielist) {
        if (nightDielist != null && nightDielist.size() > 0) {
            for (Integer i : aliveList) {
                if (i > nightDielist.get(nightDielist.size() - 1)) {
                    return i;
                }
            }
        }
        return aliveList.get(0);
    }

    /**
     * 从一个号码后挑选未死玩家开始发言
     */
    public static int pickSpeak(List<Integer> aliveList, int number) {
        for (Integer i : aliveList) {
            if (i > number) {
                return i;
            }
        }
        return aliveList.get(0);
    }

    /**
     * 返回格式化的玩家号码
     */
    public static String textNumerFormat(List<Integer> nightDielist) {
        String s = "";
        if (nightDielist != null && nightDielist.size() > 0) {
            for (Integer i : nightDielist) {
                s += String.format("[%d]", i);
            }
        }
        return s;
    }

    /**
     * 检测阵营胜利
     */
    public static int checkWinner(Map<Integer, Role> deployMap) {
        int wolfs = 0;
        int persons = 0;
        for (Map.Entry<Integer, Role> entry : deployMap.entrySet()) {
            if (!entry.getValue().isAlive()) {
                continue;
            }
            if (entry.getValue() instanceof Wolf) {
                wolfs++;
            } else {
                persons++;
            }
        }
//        LogUtil.e("阵营存活检测：狼人" + wolfs + "人，好人" + persons + "人");
        //狼人胜利
        if (wolfs >= persons && persons <= 1) {
            return Role.WIN_WOLF;
        }
        //好人胜利
        if (wolfs == 0) {
            return Role.WIN_PERSON;
        }
        return Role.WIN_NO;
    }

}
