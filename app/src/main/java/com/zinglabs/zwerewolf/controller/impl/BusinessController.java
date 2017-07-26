package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.data.BusinessData;
import com.zinglabs.zwerewolf.entity.RequestBody;
import com.zinglabs.zwerewolf.event.MsgEvent;
import com.zinglabs.zwerewolf.service.BusinessService;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

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
        BusinessData businessData = businessService.receive(body);
        int fromId = businessData.getFromId();
        int reply = businessData.getReply();

        switch (command) {
            case ProtocolConstant.CID_BNS_CRE_ROOM_RESP: //创建房间
                int code = reply > 0 ? MsgEvent.ROOM_CREATE_SUC : MsgEvent.ROOM_CREATE_FAIL;
                MsgEvent msgEvent = new MsgEvent(code, null, businessData);
                EventBus.getDefault().post(msgEvent);
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
