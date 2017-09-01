package com.zinglabs.zwerewolf.service;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.data.BusinessData;
import com.zinglabs.zwerewolf.entity.RequestBody;
import com.zinglabs.zwerewolf.entity.Packet;
import com.zinglabs.zwerewolf.utils.ByteUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * 业务消息接收发送Service
 *
 * @user wangtonghe
 * @date 2017/7/24
 * @email wthfeng@126.com
 */

public class BusinessService {

    public void send4Game(Channel channel, RequestBody reqBody) {
        ByteBuf body = channel.alloc().buffer();


        body.writeInt(reqBody.getFromId())
                .writeInt(reqBody.getRoomId())
                .writeInt(reqBody.getBout())
                .writeInt(reqBody.getContent());

        Packet packet = new Packet(body.readableBytes() + 12, reqBody.getServiceId()
                , reqBody.getCommandId(), body);
        channel.writeAndFlush(packet);

    }

    public void send4Business(Channel channel, RequestBody reqBody) {
        ByteBuf body = channel.alloc().buffer();

        body.writeInt(reqBody.getFromId())
                .writeInt(reqBody.getContent());

        Packet packet = new Packet(body.readableBytes() + 12, reqBody.getServiceId()
                , reqBody.getCommandId(), body);
        channel.writeAndFlush(packet);

    }

    public BusinessData receive(ByteBuf byteBuf) {
        return ByteUtil.resolveBusiness(byteBuf);

    }
    public BusinessData receiveVoteMsg(ByteBuf byteBuf){
        return ByteUtil.resolveVoteMsg(byteBuf);

    }
    public BusinessData receiveRoomMsg(ByteBuf byteBuf){
        return ByteUtil.resolveRoomMsg(byteBuf);
    }

    public BusinessData receiveStartMsg(ByteBuf byteBuf){

        return ByteUtil.resolveStartMsg(byteBuf);
    }

    public BusinessData receiveDawnMsg(ByteBuf byteBuf){

        return ByteUtil.resolveDawnMsg(byteBuf);
    }



}
