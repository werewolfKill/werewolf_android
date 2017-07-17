package com.zinglabs.zwerewolf.manager;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.data.GameChatData;
import com.zinglabs.zwerewolf.entity.Packet;
import com.zinglabs.zwerewolf.event.MsgEvent;
import com.zinglabs.zwerewolf.im.IMClient;

import org.greenrobot.eventbus.EventBus;

import java.nio.charset.Charset;
import java.util.Date;

import io.netty.buffer.ByteBuf;

/**
 * author: vector.huang
 * date：2016/4/18 22:06
 */
public class IMMessageManager {

    public static void sendSingleMsgReq(int to,String content){
        byte[] bytes = content.getBytes();
        ByteBuf buf = IMClient.instance().channel().alloc().buffer(bytes.length+4);
        buf.writeInt(to);
        buf.writeBytes(bytes);
        Packet packet = new Packet(buf.readableBytes() + 12,
                ProtocolConstant.SID_MSG,ProtocolConstant.CID_MSG_SEND_SINGLE_REQ
                ,buf);
        IMClient.instance().channel().writeAndFlush(packet);
    }

    public static void receiveMsgSingleOut(ByteBuf body){
        int from = body.readInt();
        String msg = body.toString(Charset.defaultCharset());
        GameChatData gameChatData = new GameChatData(GameChatData.CHAT, new Date().getTime() + "", from + "", "", msg);
        MsgEvent msgEvent = new MsgEvent(MsgEvent.ROOM_CHAT, null, gameChatData);
        EventBus.getDefault().post(msgEvent);
    }

}
