package com.zinglabs.zwerewolf.utils;

import com.zinglabs.zwerewolf.data.BusinessData;

import io.netty.buffer.ByteBuf;

/**
 * @user wangtonghe
 * @date 2017/7/24
 * @email wthfeng@126.com
 */

public class ByteUtil {

    /**
     * 解析业务类型消息体(包括业务及游戏流程服务类型)
     *
     * @param body 消息体
     * @return BNSRequest
     */
    public static BusinessData encodeBnsType(ByteBuf body) {

        BusinessData msgBody = new BusinessData();
        msgBody.setFromId(body.readInt());
        msgBody.setReply(body.readInt());

        return msgBody;
    }
}
