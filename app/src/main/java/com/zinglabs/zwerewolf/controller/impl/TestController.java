package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.manager.IMTestManager;

import io.netty.buffer.ByteBuf;

/**
 * Created by huihui on 2017/7/18.
 */

public class TestController implements BaseController {

    @Override
    public void doAccept(short command, ByteBuf body) {
        switch (command){
            case ProtocolConstant.CID_TEST_TEST_REQ:
                IMTestManager.testResp(body);
                break;
        }
    }
    @Override
    public void doSend(){

    }
}
