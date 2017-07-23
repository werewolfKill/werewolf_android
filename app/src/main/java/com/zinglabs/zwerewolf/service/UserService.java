package com.zinglabs.zwerewolf.service;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.entity.Packet;
import com.zinglabs.zwerewolf.entity.User;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * author: vector.huang
 * date：2016/4/18 22:28
 */
public class UserService {

    public void loginReq(Channel channel,String username, String password) {
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

    public  int loginResponse(ByteBuf body) {
        int userId = body.readInt();
        return userId;
    }


    public void onlineUserReq(Channel channel,int reqType) {
        ByteBuf body = channel.alloc().buffer();
        body.writeInt(reqType);
        Packet packet = new Packet(body.readableBytes() + 12
                , ProtocolConstant.SID_USER
                , ProtocolConstant.CID_USER_ONLINE_REQ, body);
        channel.writeAndFlush(packet);

    }

    public List<User> onlineUserResponse(ByteBuf body) {
        int total = body.readInt();
        List<User> users = new ArrayList<>(total);

        for (int i = 0; i < total; i++) {
            int userId = body.readInt();
            String username = body.readBytes(body.readInt()).toString(Charset.defaultCharset());

            User user = new User(userId,username);
            users.add(user);
        }
        return users;
    }

}
