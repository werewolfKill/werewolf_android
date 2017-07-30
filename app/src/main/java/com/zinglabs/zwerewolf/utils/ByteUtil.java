package com.zinglabs.zwerewolf.utils;

import com.zinglabs.zwerewolf.data.BusinessData;
import com.zinglabs.zwerewolf.entity.Room;
import com.zinglabs.zwerewolf.role.UserRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.netty.buffer.ByteBuf;

/**
 * @user wangtonghe
 * @date 2017/7/24
 * @email wthfeng@126.com
 */

public class ByteUtil {

    /**
     * 解析业务类型消息体
     *
     * @param body 消息体
     * @return BusinessData
     */
    public static BusinessData resolveBusiness(ByteBuf body) {

        BusinessData msgBody = new BusinessData();
        msgBody.setFromId(body.readInt());
        msgBody.setReply(body.readInt());
        return msgBody;
    }

    /**
     * 解析房间信息
     *
     * @param body 消息体
     * @return BusinessData
     */
    public static BusinessData resolveRoomMsg(ByteBuf body) {

        BusinessData msgBody = new BusinessData();
        msgBody.setFromId(body.readInt());
        msgBody.setReply(body.readInt());
        int flag = body.readInt();
        Map<String, Object> param = new HashMap<>();
        if (flag>0) {
            Room room = new Room();
            room.setRoomId(body.readInt()); //房间id
            room.setOwnerId(body.readInt()); //房主id
            room.setModelId(body.readInt()); //模式id
            room.setCurNumber(body.readInt()); //当前人数
            Map<Integer, UserRole> userRoleMap = new HashMap<>();
            int curNum = room.getCurNumber();
            for (int i = 0; i < curNum; i++) {
                UserRole ur = new UserRole();
                ur.setUsrId(body.readInt());
                ur.setPosition(body.readInt());
                userRoleMap.put(ur.getUsrId(),ur);
            }
            room.setPlayers(userRoleMap);
            param.put("room",room);
            msgBody.setParam(param);
        }
        return msgBody;
    }

    public static BusinessData resolveStartMsg(ByteBuf body){
        BusinessData msgBody = new BusinessData();
        msgBody.setFromId(body.readInt());
        msgBody.setReply(body.readInt());
        int num = body.readInt();
        Map<String, Object> param = new HashMap<>();
        if (num>0) {
            Integer[] wolfs = new Integer[num];
            for(int i=0;i<num;i++){
               wolfs[i]= body.readInt();
            }
            param.put("wolfs",wolfs);
            msgBody.setParam(param);
        }
        return msgBody;

    }


}
