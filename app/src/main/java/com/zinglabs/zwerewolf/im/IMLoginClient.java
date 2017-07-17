package com.zinglabs.zwerewolf.im;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author: vector.huang
 * date：2016/4/20 21:42
 */
public class IMLoginClient {

    private static IMLoginClient instance;
    private static Lock lock = new ReentrantLock();

    private IMLoginClient() {
    }

    public static IMLoginClient instance() {
        if (instance == null) {
            lock.lock();
            if (instance == null) {
                instance = new IMLoginClient();
            }
            lock.unlock();
            lock = null;
        }
        return instance;
    }

    private ThreadSocket threadSocket;

    /**
     * 异步连接
     */
    public void connect(String host, int port) {
        threadSocket = new ThreadSocket();
        threadSocket.setOnChannelActiveListener(ctx -> {
            System.out.println("连接的登录服务器可以开始发送请求了");
        });
//        threadSocket.connect(Config.LOGIN_HOST, Config.LOGIN_PORT);
        threadSocket.connect(host,port);
    }

    public void close(){
        System.out.println("登录服务器开始关闭...");
        threadSocket.close();
    }

}
