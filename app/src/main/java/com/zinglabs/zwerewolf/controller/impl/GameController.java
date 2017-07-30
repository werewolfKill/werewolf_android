package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.config.Constants;
import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.data.BusinessData;
import com.zinglabs.zwerewolf.entity.RequestBody;
import com.zinglabs.zwerewolf.event.MsgEvent;
import com.zinglabs.zwerewolf.role.Role;
import com.zinglabs.zwerewolf.service.BusinessService;
import com.zinglabs.zwerewolf.utils.RoleUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * @user wangtonghe
 * @date 2017/7/24
 */

public class GameController implements BaseController {

    private BusinessService businessService = new BusinessService();

    @Override
    public void doAccept(short command, ByteBuf body) {
        BusinessData businessData;

        MsgEvent msgEvent = null;
        int fromId, reply,code;

        switch (command) {
            case ProtocolConstant.CID_GAME_READY_RESP: //准备游戏
                businessData = businessService.receive(body);
                reply = businessData.getReply();
                if(reply==0){
                    code= MsgEvent.GAME_LEAVE;
                }else{
                    code= MsgEvent.GAME_READY;
                }
                msgEvent = new MsgEvent(code, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_GAME_START_RESP:  //开始游戏
                businessData = businessService.receiveStartMsg(body);
                fromId = businessData.getFromId();
                reply = businessData.getReply();
                Role role = RoleUtil.getRole(reply);
                System.out.println("开始游戏,您的角色是" + role.getName());
                msgEvent = new MsgEvent(MsgEvent.GAME_START, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_GAME_START_FAIL:  //开始游戏失败
                businessData = businessService.receiveStartMsg(body);
                reply = businessData.getReply();
                if(reply== Constants.ROOM_NOT_ENOUGH_NUM){
                    msgEvent = new MsgEvent(MsgEvent.GAME_NOT_ENOUGH_NUM, null, businessData);
                    EventBus.getDefault().post(msgEvent);
                }else{
                    msgEvent = new MsgEvent(MsgEvent.GAME_START_FAIL, null, businessData);
                    EventBus.getDefault().post(msgEvent);
                }
            case ProtocolConstant.CID_GAME_KILL_RES_RESP:  //狼人杀人信息
                break;
        }

    }

    @Override
    public void doSend(short command, Channel channel, Object param) {
        Map map = (Map) param;
        Integer fromId = (Integer) map.get("fromId");
        Integer roomId = (Integer) map.get("roomId");
        Integer content = (Integer) map.get("content");
        RequestBody reqBody = new RequestBody(ProtocolConstant.SID_GAME, command, fromId, roomId, content);
        businessService.send4Game(channel, reqBody);

    }
}
