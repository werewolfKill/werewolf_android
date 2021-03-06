package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.config.Constants;
import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.data.BusinessData;
import com.zinglabs.zwerewolf.entity.RequestBody;
import com.zinglabs.zwerewolf.event.HomeFragmentEvent;
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
        int fromId, reply, code;

        switch (command) {
            case ProtocolConstant.CID_GAME_READY_RESP: //准备游戏
                businessData = businessService.receive(body);
                reply = businessData.getReply();
                if (reply == 0) {
                    code = MsgEvent.GAME_LEAVE;
                } else {
                    code = MsgEvent.GAME_READY;
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
                if (reply == Constants.ROOM_NOT_ENOUGH_NUM) {
                    msgEvent = new MsgEvent(MsgEvent.GAME_NOT_ENOUGH_NUM, null, businessData);
                    EventBus.getDefault().post(msgEvent);
                } else if (reply == Constants.ROOM_NOT_ALL_READY) {
                    msgEvent = new MsgEvent(MsgEvent.GAME_NOT_ALL_READY, null, businessData);
                    EventBus.getDefault().post(msgEvent);

                } else {
                    msgEvent = new MsgEvent(MsgEvent.GAME_START_FAIL, null, businessData);
                    EventBus.getDefault().post(msgEvent);

                }
                break;
            case ProtocolConstant.CID_GAME_KILL_RESP:  //狼人同伴杀人信息
                businessData = businessService.receive(body);
                reply = businessData.getReply();
                System.out.println("同伴杀的是" + reply);
                msgEvent = new MsgEvent(MsgEvent.GAME_KILL_INFO, null, businessData);
                EventBus.getDefault().post(msgEvent);

                break;
            case ProtocolConstant.CID_GAME_NOTIFY_WITCH_KILLED:  //通知女巫狼人杀人信息
                businessData = businessService.receive(body);
                reply = businessData.getReply();
                System.out.println("狼人杀的是" + reply);
                msgEvent = new MsgEvent(MsgEvent.GAME_NOTIFY_WITCH, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_GAME_VERIFY_RESP:  //验人响应
                businessData = businessService.receive(body);
                msgEvent = new MsgEvent(MsgEvent.GAME_VERIFY, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_GAME_DAWN:    //天亮了
                businessData = businessService.receiveDawnMsg(body);
                msgEvent = new MsgEvent(MsgEvent.GAME_DAWN, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_GAME_ASK_CHIEF_RESP:  //竞选警长响应
                businessData = businessService.receive(body);
                fromId = businessData.getFromId();
                System.out.println(fromId + "号玩家竞选警长");
                msgEvent = new MsgEvent(MsgEvent.GAME_ASK_CHIEF, null, businessData);
                EventBus.getDefault().post(msgEvent);

                break;
            case ProtocolConstant.CID_GAME_POLICE_START_SPEAKING: //警上发言
                businessData = businessService.receive(body);

                msgEvent = new MsgEvent(MsgEvent.GAME_POLICE_SPEAKING, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;

            case ProtocolConstant.CID_GAME_OTHER_ENTER: //他人进入房间
                businessData = businessService.receive(body);
                msgEvent = new MsgEvent(MsgEvent.GAME_OTHER_ENTER, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_GAME_START_CHIEF_VOTE:  //开始警下投票
                businessData = businessService.receive(body);
                msgEvent = new MsgEvent(MsgEvent.GAME_VOTE_CHIEF, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_GAME_ELECT_CHIEF_RESP://选出警长
                businessData = businessService.receiveVoteMsg(body);
                msgEvent = new MsgEvent(MsgEvent.GAME_SET_CHIEF, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_GAME_START_SPEAKING:  //开始发言
                businessData = businessService.receive(body);
                msgEvent = new MsgEvent(MsgEvent.GAME_SPEAK, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_GAME_CHIEF_REQ_SUM_TICKET://要求警长归票
                businessData = businessService.receive(body);
                msgEvent = new MsgEvent(MsgEvent.GAME_CHIEF_SUM_TICKET, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_GAME_REQ_VOTE_RESP://请求投票
                businessData = businessService.receive(body);
                msgEvent = new MsgEvent(MsgEvent.GAME_VOTE, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_GAME_VOTE_RESP://投票结果
                businessData = businessService.receiveVoteMsg(body);
                msgEvent = new MsgEvent(MsgEvent.GAME_VOTE_RESULT, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_GAME_DARK://天黑
                businessData = businessService.receive(body);
                msgEvent = new MsgEvent(MsgEvent.GAME_DARK, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_GAME_CHANGE_CHIEF_RESP://移交警徽
                businessData = businessService.receive(body);
                msgEvent = new MsgEvent(MsgEvent.GAME_CHANGE_CHIEF, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case  ProtocolConstant.CID_GAME_QUIT_POLICE_RESP://取消警长竞选
                businessData = businessService.receive(body);
                msgEvent = new MsgEvent(MsgEvent.GAME_QUIT_VOTE_CHIEF, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
            case ProtocolConstant.CID_GAME_TURN_SPEAK: //轮到某人发言
                businessData = businessService.receive(body);
                msgEvent = new MsgEvent(MsgEvent.GAME_TURN_SPEAK, null, businessData);
                EventBus.getDefault().post(msgEvent);
                break;
        }

    }

    @Override
    public void doSend(short command, Channel channel, Object param) {
        Map map = (Map) param;
        Integer fromId = (Integer) map.get("fromId");
        Integer roomId = (Integer) map.get("roomId");
        Integer content = (Integer) map.get("content");
        Integer bout = (Integer) map.get("bout");

        RequestBody reqBody = new RequestBody(ProtocolConstant.SID_GAME, command, fromId, roomId, content, bout);
        businessService.send4Game(channel, reqBody);

    }
}
