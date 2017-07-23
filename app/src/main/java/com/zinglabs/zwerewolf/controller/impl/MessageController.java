package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.data.GameChatData;
import com.zinglabs.zwerewolf.event.MsgEvent;
import com.zinglabs.zwerewolf.service.MessageService;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * Created by huihui on 2017/7/18.
 */

public class MessageController implements BaseController {
    MessageService messageService = new MessageService();
    @Override
    public void doAccept(short command, ByteBuf body) {
        switch (command){
            case ProtocolConstant.CID_MSG_TEXT_RESP:
                GameChatData gameChatData = messageService.receiveText(body);
                MsgEvent msgEvent = new MsgEvent(MsgEvent.ROOM_CHAT, null, gameChatData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_MSG_VOICE_RESP:
                messageService.receiveVoice(body);
                break;
        }
    }
    @Override
    public void doSend(short command,Channel channel,Object param){
        switch (command){
            case ProtocolConstant.CID_MSG_TEXT_REQ:
                Map map = (Map) param;
                messageService.sendText(channel,(Integer) map.get("from"),(String)map.get("content"));
                //MessageService.receiveMsg(body);
                break;
            case ProtocolConstant.CID_MSG_VOICE_REQ:
                messageService.sendVoiceStart(channel,(Integer) ((Map)param).get("from"));
                break;
            case ProtocolConstant.CID_MSG_VOICE_INTERRUPT_REQ:
                messageService.sendVoiceInterrupt(channel);
                break;
            case ProtocolConstant.CID_MSG_VOICE_END_REQ:
                messageService.sendVoiceInterrupt(channel);
                break;
        }
    }
}
