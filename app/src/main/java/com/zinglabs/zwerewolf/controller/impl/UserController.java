package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.manager.IMUserManager;

import io.netty.buffer.ByteBuf;

/**
 * Created by huihui on 2017/7/18.
 */

public class UserController implements BaseController {

    @Override
    public void doAccept(short command, ByteBuf body) {
        switch (command){
            case ProtocolConstant.CID_USER_LOGIN_RESP:
                IMUserManager.loginResp(body);
                break;
            case ProtocolConstant.CID_USER_ONLINE_RESP:
                IMUserManager.onlineUserResp(body);
                break;
        }
    }
    @Override
    public void doSend(){

    }
}
