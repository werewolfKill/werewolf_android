package com.zinglabs.zwerewolf;

import java.io.PrintStream;
import java.net.Socket;

/**
 * @user wangtonghe
 * @date 2017/9/7
 * @email wthfeng@126.com
 */

public class SocketTest {


    public static void main(String[] args) throws Exception{
        Socket client = new Socket("183k141i84.imwork.net", 31260);
        client.setSoTimeout(5000);
        PrintStream out = new PrintStream(client.getOutputStream());
        out.print("hello");
    }
}
