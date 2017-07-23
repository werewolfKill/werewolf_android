package com.zinglabs.zwerewolf.im;


import com.zinglabs.zwerewolf.config.Constants;
import com.zinglabs.zwerewolf.config.ProcessorTable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.channel.ChannelHandlerContext;

/**
 * author: vector.huang
 * date：2016/4/20 21:42
 */
public class IMClient {

    private static IMClient instance;
    private int userId;
    private ThreadSocket threadSocket;
    private  Lock lock = new ReentrantLock();

    public void setUserId(int userId) {
        this.userId = userId;
    }
    static {
        instance = new IMClient();
    }

    private IMClient(){

    }

    public static IMClient getInstance() {
        return instance;
    }

    public void connect(String host, int port) {

    }

    public void close() {
        System.out.println("业务服务器开始关闭...");
        threadSocket.close();
    }

    public void send(String requestMapping,Object param) {
        String[] requestMappingArr = requestMapping.split("/");
        if (threadSocket == null) {
            lock.lock();
            if (threadSocket == null) {
                threadSocket = new ThreadSocket();
                threadSocket.setOnChannelActiveListener(ctx -> {
                    ProcessorTable.get(Short.parseShort(requestMappingArr[0])).doSend(Short.parseShort(requestMappingArr[1]),threadSocket.channel(),param);
                });
                threadSocket.connect(Constants.app_host, Constants.app_port);
            }else{
                lock = null;
                ProcessorTable.get(Short.parseShort(requestMappingArr[0])).doSend(Short.parseShort(requestMappingArr[1]),threadSocket.channel(),param);
            }
            lock.unlock();
        }else{
            ProcessorTable.get(Short.parseShort(requestMappingArr[0])).doSend(Short.parseShort(requestMappingArr[1]),threadSocket.channel(),param);
        }
    }
    public interface ConnectCallListener extends ThreadSocket.OnChannelActiveListener {
        void onChannelActive(ChannelHandlerContext ctx);
    }
}
