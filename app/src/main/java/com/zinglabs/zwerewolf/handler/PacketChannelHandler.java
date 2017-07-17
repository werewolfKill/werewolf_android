package com.zinglabs.zwerewolf.handler;


import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.entity.Packet;
import com.zinglabs.zwerewolf.im.ThreadSocket;
import com.zinglabs.zwerewolf.manager.IMLoginManager;
import com.zinglabs.zwerewolf.manager.IMMessageManager;
import com.zinglabs.zwerewolf.manager.IMTestManager;
import com.zinglabs.zwerewolf.manager.IMUserManager;

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

        //测试服务
        if(packet.getServiceId() == ProtocolConstant.SID_TEST){
            switch (packet.getCommandId()){
                case ProtocolConstant.CID_TEST_TEST_REQ:
                    IMTestManager.testResp(packet.getBody());
                    break;
            }
            return;
        }

        if(packet.getServiceId() == ProtocolConstant.SID_LOGIN){
            switch (packet.getCommandId()){
                case ProtocolConstant.CID_LOGIN_OUT:
                    IMLoginManager.inIpPort(packet.getBody());
                    break;
            }
            return;
        }

        //用户服务
        if(packet.getServiceId() == ProtocolConstant.SID_USER){
            switch (packet.getCommandId()){
                case ProtocolConstant.CID_USER_LOGIN_RESP:
                    IMUserManager.loginResp(packet.getBody());
                    break;
                case ProtocolConstant.CID_USER_ONLINE_RESP:
                    IMUserManager.onlineUserResp(packet.getBody());
                    break;
            }
            return;
        }

        //消息服务
        if(packet.getServiceId() == ProtocolConstant.SID_MSG){
            switch (packet.getCommandId()){
                case ProtocolConstant.CID_MSG_RECEIVE_SINGLE_OUT:
                    IMMessageManager.receiveMsgSingleOut(packet.getBody());
                    break;
            }
            return;
        }
    }
}
