package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.manager.IMMessageManager;

import io.netty.buffer.ByteBuf;

/**
 * Created by huihui on 2017/7/18.
 */

public class MessageController implements BaseController {

    @Override
    public void doAccept(short command, ByteBuf body) {
        switch (command){
            case ProtocolConstant.CID_MSG_RECEIVE_SINGLE_OUT:
                IMMessageManager.receiveMsgSingleOut(body);
                break;
        }
    }
    @Override
    public void doSend(){

    }
}
