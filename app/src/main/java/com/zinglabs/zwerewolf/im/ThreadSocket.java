package com.zinglabs.zwerewolf.im;

import com.zinglabs.zwerewolf.handler.ByteToPacketCodec;
import com.zinglabs.zwerewolf.handler.PacketChannelHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * author: vector.huang
 * date：2016/4/18 19:42
 */
public class ThreadSocket {

    private EventLoopGroup worker;
    private ChannelFuture channelFuture;
    private OnChannelActiveListener listener;

    public void connect(String host, int port) {
        System.out.println("开始连接 " + host + ":" + port);
        worker = new NioEventLoopGroup();
        Bootstrap boot = new Bootstrap();
        boot.group(worker);

        boot.channel(NioSocketChannel.class);

        boot.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, -4, 0, false));
                ch.pipeline().addLast(new ByteToPacketCodec());
                ch.pipeline().addLast(new PacketChannelHandler(listener));
            }
        });
        boot.option(ChannelOption.SO_KEEPALIVE, true);
        channelFuture = boot.connect(host, port);
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功...");
            }else{
                future.cause().printStackTrace();
                System.out.println("连接失败："+future.cause().getMessage());
            }
        });
    }

    public void close() {
        if (channelFuture != null && channelFuture.channel() != null) {
            if (channelFuture.channel().isOpen()) {
                try {
                    channelFuture.channel().close().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        worker.shutdownGracefully();
    }

    public Channel channel() {
        if (channelFuture == null) {
            return null;
        }
        return channelFuture.channel();
    }


    public void setOnChannelActiveListener(OnChannelActiveListener listener){
        this.listener = listener;
    }

    public interface OnChannelActiveListener{
        void onChannelActive(ChannelHandlerContext ctx);
    }
}
