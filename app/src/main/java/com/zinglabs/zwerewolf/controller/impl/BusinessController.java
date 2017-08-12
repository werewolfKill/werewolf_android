package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.config.Constants;
import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.data.BusinessData;
import com.zinglabs.zwerewolf.entity.RequestBody;
import com.zinglabs.zwerewolf.entity.Room;
import com.zinglabs.zwerewolf.event.HomeFragmentEvent;
import com.zinglabs.zwerewolf.event.MsgEvent;
import com.zinglabs.zwerewolf.service.BusinessService;
import com.zinglabs.zwerewolf.ui.HomeFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Objects;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * @user wangtonghe
 * @date 2017/7/25
 * @email wthfeng@126.com
 */

public class BusinessController implements BaseController {


    private BusinessService businessService = new BusinessService();


    @Override
    public void doAccept(short command, ByteBuf body) {
        BusinessData businessData = businessService.receiveRoomMsg(body);
        HomeFragmentEvent event;
        int code, modalId, roomId;
        Room room;
        int fromId = businessData.getFromId();
        int reply = businessData.getReply();
        Map<String, Object> param = businessData.getParam();
        switch (command) {

            case ProtocolConstant.CID_BNS_CRE_ROOM_RESP: //创建房间
                room = (Room) param.get("room");
                modalId = room.getModelId();
                code = reply > 0 ? HomeFragmentEvent.CREATE_ROOM_SUC : HomeFragmentEvent.CREATE_ROOM_FAIL;
                event = new HomeFragmentEvent(reply, fromId, modalId, code);
                event.setRoom(room);
                EventBus.getDefault().post(event);
                break;
            case ProtocolConstant.CID_BNS_FIND_ROOM_RESP:  //搜索房间

                if (reply == Constants.ROOM_SEARCH_SUCCESS) {
                    room = (Room) param.get("room");
                    modalId = room.getModelId();
                    roomId = room.getRoomId();
                    event = new HomeFragmentEvent(roomId, fromId, modalId, HomeFragmentEvent.SEARCH_ROOM_SUC);
                    event.setRoom(room);
                } else if (reply == Constants.ROOM_NOT_EXIST) {
                    event = new HomeFragmentEvent();
                    event.setCode(HomeFragmentEvent.SEARCH_ROOM_NOT_EXIST);
                } else if (reply == Constants.ROOM_ALREADY_FULL) {
                    event = new HomeFragmentEvent();
                    event.setCode(HomeFragmentEvent.SEARCH_ROOM_ALREADY_FULL);
                } else {
                    event = new HomeFragmentEvent();
                    event.setCode(HomeFragmentEvent.SEARCH_ROOM_FAIL);
                }
                EventBus.getDefault().post(event);
                break;
        }

    }

    @Override
    public void doSend(short command, Channel channel, Object param) {
        Map map = (Map) param;
        Integer fromId = (Integer) map.get("fromId");
        Integer content = (Integer) map.get("content");
        RequestBody reqBody = new RequestBody(ProtocolConstant.SID_BNS, command, fromId, content);
        businessService.send4Business(channel, reqBody);

    }
}
