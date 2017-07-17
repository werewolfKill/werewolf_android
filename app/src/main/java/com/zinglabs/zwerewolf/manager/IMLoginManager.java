package com.zinglabs.zwerewolf.manager;


import com.zinglabs.zwerewolf.im.IMClient;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;

/**
 * author: vector.huang
 * date：2016/4/20 20:34
 */
public class IMLoginManager {

    public static void inIpPort(ByteBuf body){
        int hostLen = body.readInt();
        String host = body.readBytes(hostLen).toString(Charset.defaultCharset());
        int port = body.readInt();

        //获取到主机和端口那么就连接
        IMClient.instance().connect(host,port);
    }

}
