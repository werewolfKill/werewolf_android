package com.zinglabs.zwerewolf.im;


import com.zinglabs.zwerewolf.manager.IMTestManager;
import com.zinglabs.zwerewolf.manager.IMUserManager;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.channel.Channel;

/**
 * author: vector.huang
 * date：2016/4/20 21:42
 */
public class IMClient {

    private static IMClient instance;
    private static Lock lock = new ReentrantLock();

    private int userId;

    private IMClient() {
    }

    public static IMClient instance() {
        if (instance == null) {
            lock.lock();
            if (instance == null) {
                instance = new IMClient();
            }
            lock.unlock();
            lock = null;
        }
        return instance;
    }

    private ThreadSocket threadSocket;

    public void connect(String host, int port) {
        threadSocket = new ThreadSocket();
        threadSocket.setOnChannelActiveListener(ctx -> {
            System.out.println("连接的业务服务器可以开始发送请求了");
            IMTestManager.testReq("Test");
            IMUserManager.loginReq("黄廉温","1234567890");
        });
        threadSocket.connect(host, port);
    }

    public void close(){
        System.out.println("业务服务器开始关闭...");
        threadSocket.close();
    }

    public Channel channel(){
        return threadSocket.channel();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
