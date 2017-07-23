package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.entity.User;
import com.zinglabs.zwerewolf.event.UserLoginEvent;
import com.zinglabs.zwerewolf.service.UserService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * Created by huihui on 2017/7/18.
 */

public class UserController implements BaseController {
    UserService userService = new UserService();
    @Override
    public void doAccept(short command, ByteBuf body) {
        switch (command){
            case ProtocolConstant.CID_USER_LOGIN_RESP:
                int userId = userService.loginResponse(body);
                UserLoginEvent userLoginEvent = new UserLoginEvent();
                userLoginEvent.setUserId(userId);
                EventBus.getDefault().post(userLoginEvent);
                break;
            case ProtocolConstant.CID_USER_ONLINE_RESP:
                List<User> users = userService.onlineUserResponse(body);
                EventBus.getDefault().post(users);
                break;
        }
    }
    @Override
    public void doSend(short command,Channel channel,Object param){
        switch (command){
            case ProtocolConstant.CID_USER_LOGIN_REQ:
                Map<String,String> userMap = (Map<String,String>)param;
                userService.loginReq(channel,userMap.get("name"),userMap.get("password"));
                break;
            case ProtocolConstant.CID_USER_ONLINE_REQ:
                userService.onlineUserReq(channel,(int)param);
                break;
        }
    }
}
