package com.zinglabs.zwerewolf.handler;


import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.config.ProcessorTable;
import com.zinglabs.zwerewolf.entity.Packet;
import com.zinglabs.zwerewolf.im.ThreadSocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * author: vector.huang
 * date：2016/4/18 19:25
 */
public class PacketChannelHandler extends ChannelInboundHandlerAdapter {


    private ThreadSocket.OnChannelActiveListener listener;

    public PacketChannelHandler(ThreadSocket.OnChannelActiveListener listener) {
        this.listener = listener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if(listener != null){
            listener.onChannelActive(ctx);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Packet packet = (Packet) msg;
        BaseController action = ProcessorTable.get(packet.getServiceId());
        if(action != null){
            action.doAccept(packet.getCommandId(),packet.getBody());
        }
    }
}
