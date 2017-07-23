package com.zinglabs.zwerewolf.utils;

import com.google.gson.internal.ObjectConstructor;
import com.zinglabs.zwerewolf.im.IMClient;

/**
 * Created by wangtonghe on 2017/7/24.
 */

public class IMClientUtil {
    private static final IMClient imClinet;

    static {
        imClinet = IMClient.getInstance();
    }

    public static void sendMsg(short serviceId, short typeId, Object param){
        imClinet.send(serviceId+"/"+typeId,param);
    }


}
