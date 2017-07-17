package com.zinglabs.zwerewolf.manager;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.entity.Packet;
import com.zinglabs.zwerewolf.im.IMClient;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;

/**
 * author: vector.huang
 * date：2016/4/18 19:35
 */
public class IMTestManager {

    /**
     * 处理Test 响应
     * @param body
     */
    public static void testResp(ByteBuf body){
        String test = body.toString(Charset.defaultCharset());
        System.out.println(test);
    }

    /**
     * 发送Test 请求
     * @param body
     */
    public static void testReq(String body){
        byte[] bytes = body.getBytes();
        int length = bytes.length + 12;
        ByteBuf buf = IMClient.instance().channel().alloc().buffer(bytes.length);
        buf.writeBytes(bytes);
        Packet packet = new Packet(length, ProtocolConstant.SID_TEST,ProtocolConstant.CID_TEST_TEST_REQ
        ,buf);
        IMClient.instance().channel().writeAndFlush(packet);
    }

}
