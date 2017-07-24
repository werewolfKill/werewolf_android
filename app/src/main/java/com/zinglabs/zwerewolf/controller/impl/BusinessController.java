package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.controller.BaseController;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * @user wangtonghe
 * @date 2017/7/25
 * @email wthfeng@126.com
 */

public class BusinessController implements BaseController {
    @Override
    public void doAccept(short command, ByteBuf body) {

    }

    @Override
    public void doSend(short command, Channel channel, Object param) {

    }
}
