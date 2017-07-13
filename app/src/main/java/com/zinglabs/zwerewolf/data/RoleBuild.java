package com.zinglabs.zwerewolf.data;

import java.util.Random;

/**
 * 模拟生成角色信息
 * Created by Administrator on 2017/3/8.
 */

public class RoleBuild {
    private static final String baseImgUrl = "http://www.wzc25151.com/myimg/";
    private static final String baseImgEnd = ".jpg";
    private static final String[] arrName = new String[]{
            "李宇春", "张靓颖", "周笔畅", "何洁", "刘亦菲", "张含韵", "陈好", "尚雯婕", "汤唯", "张筱雨", "韩雪", "孙菲菲", "张嘉倪", "霍思燕", "陈紫函", "朱雅琼", "江一燕", "厉娜", "许飞", "胡灵", "郝菲尔", "刘力扬", "reborn", "章子怡", "谭维维", "魏佳庆", "张亚飞", "李旭丹", "孙艺心", "巩贺", "艾梦萌", "闰妮", "王蓉", "汤加丽", "汤芳", "牛萌萌", "范冰冰", "赵薇", "周迅", "金莎"
    };

    private RoleBuild() {
    }

    public static RoleData build(int num) {
        RoleData roleData = new RoleData();
        roleData.setHeadImgUrl(baseImgUrl + new Random().nextInt(20) + baseImgEnd);
        roleData.setNickName(arrName[new Random().nextInt(arrName.length)]);
        roleData.setOwner(false);
        roleData.setNumber(num);
        return roleData;
    }
    public static RoleData build(int num,UserData userData) {
        RoleData roleData = new RoleData();
//        roleData.setHeadImgUrl(userData.getHeadImgUrl());
        roleData.setHeadImgUrl(baseImgUrl + new Random().nextInt(20) + baseImgEnd);
        roleData.setNickName(userData.getNickName());
        roleData.setOwner(false);
        roleData.setNumber(num);
        return roleData;
    }
}
