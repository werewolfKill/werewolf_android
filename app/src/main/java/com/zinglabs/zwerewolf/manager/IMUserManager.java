package com.zinglabs.zwerewolf.manager;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.entity.Packet;
import com.zinglabs.zwerewolf.entity.User;
import com.zinglabs.zwerewolf.event.UserLoginEvent;
import com.zinglabs.zwerewolf.im.IMClient;

import org.greenrobot.eventbus.EventBus;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * author: vector.huang
 * date：2016/4/18 22:28
 */
public class IMUserManager {

    public static void loginReq(String username, String password) {
        Channel channel = IMClient.instance().channel();
        ByteBuf body = channel.alloc().buffer();
        byte[] uBytes = username.getBytes();
        byte[] pBytes = password.getBytes();

        body.writeInt(uBytes.length)
                .writeBytes(uBytes)
                .writeInt(pBytes.length)
                .writeBytes(pBytes);

        Packet packet = new Packet(body.readableBytes() + 12, ProtocolConstant.SID_USER
                , ProtocolConstant.CID_USER_LOGIN_REQ, body);
        channel.writeAndFlush(packet);

    }

    public static void loginResp(ByteBuf body) {
        int id = body.readInt();
        IMClient.instance().setUserId(id);
        System.out.println("登陆成功，用户Id 为 " + id);
        IMTestManager.testReq("Test 通过");

        EventBus.getDefault().post(new UserLoginEvent());
    }


    public static void onlineUserReq(int reqType) {
        Channel channel = IMClient.instance().channel();
        ByteBuf body = channel.alloc().buffer();

        body.writeInt(reqType);

        Packet packet = new Packet(body.readableBytes() + 12
                , ProtocolConstant.SID_USER
                , ProtocolConstant.CID_USER_ONLINE_REQ, body);
        channel.writeAndFlush(packet);

    }

    public static void onlineUserResp(ByteBuf body) {
        int total = body.readInt();
        System.out.println("在线用户总数："+total);

        List<User> users = new ArrayList<>(total);

        for (int i = 0; i < total; i++) {
            int userId = body.readInt();
            String username = body.readBytes(body.readInt()).toString(Charset.defaultCharset());

            User user = new User();
            user.setId(userId);
            user.setUsername(username);
            users.add(user);
        }

        EventBus.getDefault().post(users);
    }

}
