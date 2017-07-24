package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.data.BusinessData;
import com.zinglabs.zwerewolf.data.GameChatData;
import com.zinglabs.zwerewolf.entity.BnsRequest;
import com.zinglabs.zwerewolf.event.MsgEvent;
import com.zinglabs.zwerewolf.service.BusinessService;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

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
        BusinessData businessData = businessService.receive(body);
        int fromId = businessData.getFromId();
        int reply = businessData.getReply();

        switch (command){
            case ProtocolConstant.CID_GAME_READY_RESP: //准备游戏
                System.out.println(fromId+"号玩家已准备好");
                break;
            case ProtocolConstant.CID_GAME_KILL_RES_RESP:  //狼人杀人信息
                System.out.println(fromId+"击杀"+reply+"号玩家");
                break;
        }

    }

    @Override
    public void doSend(short command, Channel channel, Object param) {
        Map map = (Map)param;
        Integer fromId = (Integer) map.get("fromId");
        Integer roomId = (Integer)map.get("roomId");
        Integer content = (Integer)map.get("content");
        BnsRequest reqBody = new BnsRequest(ProtocolConstant.SID_GAME,command,fromId,roomId,content);
        businessService.send(channel,reqBody);

    }
}
