package com.zinglabs.zwerewolf.controller;

import io.netty.buffer.ByteBuf;

/**
 * Created by huihui on 2017/7/18.
 */

public interface BaseController {
    void doAccept(short command, ByteBuf body);

    void doSend();
}
