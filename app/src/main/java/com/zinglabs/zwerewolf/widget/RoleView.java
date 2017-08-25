package com.zinglabs.zwerewolf.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zinglabs.zwerewolf.R;
import com.zinglabs.zwerewolf.config.Constants;
import com.zinglabs.zwerewolf.controller.SimpleController;
import com.zinglabs.zwerewolf.data.GameChatData;
import com.zinglabs.zwerewolf.data.RoleData;
import com.zinglabs.zwerewolf.entity.User;
import com.zinglabs.zwerewolf.event.MsgEvent;
import com.zinglabs.zwerewolf.utils.AppUtil;
import com.zinglabs.zwerewolf.utils.DateUtil;
import com.zinglabs.zwerewolf.utils.GlideUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

/**
 * Created by Administrator on 2017/3/7.
 */

public class RoleView extends RelativeLayout implements View.OnClickListener {
    public static final int STATE_LOCK = 0xa0;//位置锁定；只显示锁定
    public static final int STATE_NOBODY = 0xa1;//位置解锁，无人；显示无人，号码
    public static final int STATE_DIE = 0xa2;//位置解锁，有人，已死亡；显示死亡，号码
    public static final int STATE_UNREADY = 0xa3;//位置解锁，有人，未准备；显示头像，号码，语音
    public static final int STATE_READY = 0xa4;//位置解锁，有人，已准备；显示头像，号码，语音，准备状态
    public static final int STATE_SPEAKING = 0xa5;//位置解锁，有人，正在说话；显示头像，号码，语音
    public static final int STATE_ME = 0xa6;//位置解锁，有人，表示自己；显示头像，号码，语音
    public static final int STATE_VOTE_CHIEF = 0xa7;//位置解锁，有人，竞选警长；显示头像，号码，语音
    public static final int STATE_QUIT_CHIEF = 0xa8;//位置解锁，有人，放弃警长；显示头像，号码，语音
    public static final int STATE_SET_CHIEF = 0xa9;//位置解锁，有人，设置警长；显示头像，号码，语音
    public static final int STATE_WOLF = 0xaa;//位置解锁，有人，狼；显示头像，号码，语音
    public static final int STATE_SPEAKING_END = 0xab;//位置解锁，有人，语音结束；显示头像，号码，语音
    public static final int STATE_AT_GAME = 0xac;//位置解锁，有人，游戏中；显示头像，号码，语音

    private AppCompatActivity activity;
    private View root;
    private RoleData mRoleData;

    private ImageView head_iv;
    private ImageView number_iv;
    private TextView number_tv;
    private ImageView die_iv;
    private ImageView speaking_iv;
    private ImageView ready_iv;
    private ImageView lock_iv;
    private ImageView hand_iv;
    private ImageView quit_chief_iv;
    private ImageView chief_iv;
    protected ImageView wolf_iv;

    //模拟聊天内容
    private String[] arrChat = Constants.ARR_TEXT_PLAYER_CHAT;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!AppUtil.isSafe(activity)) {
//                mHandler = null;
                return;
            }
            if (mRoleData != null) {
                mRoleData.setState(msg.what);
            }
            switch (msg.what) {
                //锁定
                case STATE_LOCK:
                    lock_iv.setVisibility(View.VISIBLE);
                    quit_chief_iv.setVisibility(View.GONE);
                    wolf_iv.setVisibility(View.GONE);
                    break;
                //无人
                case STATE_NOBODY:
                    lock_iv.setVisibility(View.GONE);
                    ready_iv.setVisibility(View.GONE);
                    speaking_iv.setVisibility(View.GONE);
                    head_iv.setVisibility(View.VISIBLE);
                    number_iv.setVisibility(View.VISIBLE);
                    number_tv.setVisibility(View.VISIBLE);
                    head_iv.setImageResource(R.mipmap.icon_vacancy);
                    die_iv.setVisibility(View.GONE);
                    hand_iv.setVisibility(View.GONE);
                    chief_iv.setVisibility(View.GONE);
                    quit_chief_iv.setVisibility(View.GONE);
                    wolf_iv.setVisibility(View.GONE);
                    if (mRoleData == null) {
                        return;
                    }
                    number_tv.setText("" + mRoleData.getNumber());
                    break;
                //游戏中，死亡
                case STATE_DIE:
                   die_iv.setVisibility(View.VISIBLE);
                    break;
                //玩家未准备
                case STATE_UNREADY:
                    lock_iv.setVisibility(View.GONE);
                    die_iv.setVisibility(View.GONE);
                    speaking_iv.setVisibility(View.GONE);
                    head_iv.setVisibility(View.VISIBLE);
                    number_iv.setVisibility(View.VISIBLE);
                    number_tv.setVisibility(View.VISIBLE);
                    head_iv.setImageResource(R.mipmap.app_icon);
                    hand_iv.setVisibility(View.GONE);
                    chief_iv.setVisibility(View.GONE);
                    quit_chief_iv.setVisibility(View.GONE);
                    wolf_iv.setVisibility(View.GONE);
                    if (mRoleData == null) {
                        return;
                    }
                    number_tv.setText("" + mRoleData.getNumber());
                    if (mRoleData.isOwner() && !SimpleController.isGameing()) {
                        ready_iv.setVisibility(View.VISIBLE);
                        ready_iv.setImageResource(R.mipmap.icon_room_homeowners);
                    } else {
                        ready_iv.setVisibility(View.GONE);
                    }
                    break;
                //玩家准备
                case STATE_READY:
                    lock_iv.setVisibility(View.GONE);
                    die_iv.setVisibility(View.GONE);
                    speaking_iv.setVisibility(View.GONE);
                    head_iv.setVisibility(View.VISIBLE);
                    number_iv.setVisibility(View.VISIBLE);
                    number_tv.setVisibility(View.VISIBLE);
                    ready_iv.setVisibility(View.VISIBLE);
                    hand_iv.setVisibility(View.GONE);
                    quit_chief_iv.setVisibility(View.GONE);
                    chief_iv.setVisibility(View.GONE);
//                    head_iv.setImageResource(arr_role[number % arr_role.length]);
                    //判断角色是否房主
                    if (mRoleData.isOwner()) {
                        ready_iv.setImageResource(R.mipmap.icon_room_homeowners);
                    } else {
                        ready_iv.setImageResource(R.mipmap.room_ready);
                    }
                    break;
                case STATE_AT_GAME://
                    ready_iv.setVisibility(View.GONE);
                    break;
                //游戏中语音
                case STATE_SPEAKING:
                    speaking_iv.setVisibility(View.VISIBLE);
                    break;
                case STATE_SPEAKING_END:
                    speaking_iv.setVisibility(View.GONE);
                    break;
                case STATE_ME:
                    break;  //设置我
                case STATE_VOTE_CHIEF:  //竞选警长
                    hand_iv.setVisibility(View.VISIBLE);
                    break;
                case STATE_QUIT_CHIEF:  //取消竞选警长
                    hand_iv.setVisibility(View.GONE);
                    break;
                case STATE_SET_CHIEF:  //设置警长
                    chief_iv.setVisibility(View.VISIBLE);
                    break;
                case STATE_WOLF:// 设置狼
                    wolf_iv.setVisibility(View.VISIBLE);




            }
        }
    };

    public RoleView(Context context) {
        super(context);
        activity = (AppCompatActivity) context;
        init();
    }

    private void init() {
        root = LayoutInflater.from(activity).inflate(R.layout.view_role, this);
        root.setOnClickListener(this);

        head_iv = (ImageView) root.findViewById(R.id.role_head_iv);
        number_iv = (ImageView) root.findViewById(R.id.role_number_iv);
        number_tv = (TextView) root.findViewById(R.id.role_number_tv);
        die_iv = (ImageView) root.findViewById(R.id.role_die_iv);
        speaking_iv = (ImageView) root.findViewById(R.id.role_speaking_iv);
        ready_iv = (ImageView) root.findViewById(R.id.role_ready_iv);
        lock_iv = (ImageView) root.findViewById(R.id.role_lock_iv);
        hand_iv = (ImageView) root.findViewById(R.id.role_hand_iv);
        quit_chief_iv = (ImageView) root.findViewById(R.id.role_quit_chief_iv);
        chief_iv = (ImageView) root.findViewById(R.id.role_chief_iv);
        wolf_iv = (ImageView) root.findViewById(R.id.role_wolf);

    }

    /**
     * 设置玩家信息
     */
    public void setup(RoleData roleData) {
        mRoleData = roleData;
        if (mRoleData != null) {
            mHandler.sendEmptyMessage(STATE_UNREADY);
            //发送消息玩家进场
            GameChatData gameChatData = new GameChatData(GameChatData.ENTRY, DateUtil.nowLongStr(), new User(mRoleData.getNickName()), 111, "");
            MsgEvent msgEvent = new MsgEvent(MsgEvent.ROOM_CHAT, null, gameChatData);
            EventBus.getDefault().post(msgEvent);
        } else {
            clear(false);
        }
    }

    /**
     * 设置玩家信息
     */
    public void setNoBody(RoleData roleData) {
        mRoleData = roleData;
        if (mRoleData != null) {
            mHandler.sendEmptyMessage(STATE_NOBODY);
        } else {
            clear(false);
        }
    }

    public void setAtGame(){
        mHandler.sendEmptyMessage(STATE_AT_GAME);

    }

    public RoleData getmRoleData() {
        return mRoleData;
    }

    /**
     * 玩家离开位置，或者锁定位置
     *
     * @param isLock
     */
    public void clear(boolean isLock) {
        mRoleData = null;
        if (isLock) {
            mHandler.sendEmptyMessage(STATE_LOCK);
        } else {
            mHandler.sendEmptyMessage(STATE_NOBODY);
        }
    }

    @Override
    public void onClick(View v) {
//        if (GameController.isGameing()) {
//            return;
//        }
        if (SimpleController.isGameing()) {
            return;
        }
        if (mRoleData != null) {
            //判断当前是否准备
//            if (mRoleData.getState() == STATE_LOCK) {
//                mHandler.sendEmptyMessage(STATE_NOBODY);
//            } else if (mRoleData.getState() == STATE_NOBODY) {
//                mHandler.sendEmptyMessage(STATE_UNREADY);
//            } else if (mRoleData.getState() == STATE_UNREADY) {
//                mHandler.sendEmptyMessage(STATE_READY);
//            } else if (mRoleData.getState() == STATE_READY) {
//                mHandler.sendEmptyMessage(STATE_LOCK);
//            }
//            if (mRoleData.isOwner()) {
//                MsgEvent msgEvent = new MsgEvent(MsgEvent.ROOM_START);
//                EventBus.getDefault().post(msgEvent);
//            }
        } else {
            //activity.showToast("位置不可开启");
        }
    }

    /**
     * 模拟发言，可以删除该方法
     */
    public void chat() {
//        if (mRoleData != null) {
//
//            GameChatData gameChatData = new GameChatData(GameChatData.CHAT, DateUtil.nowLongStr(), new User(mRoleData.getNumber() + "号" + mRoleData.getNickName()), 111, arrChat[new Random().nextInt(arrChat.length)]);
//            MsgEvent msgEvent = new MsgEvent(MsgEvent.ROOM_CHAT, null, gameChatData);
//            EventBus.getDefault().post(msgEvent);
//        }
    }

    /**
     * 准备状态
     */
    public void ready() {
        mHandler.sendEmptyMessage(STATE_READY);
    }

    /**
     * 设置警长
     */
    public void setChief() {
        mHandler.sendEmptyMessage(STATE_SET_CHIEF);
    }

    public void setMe() {
        mHandler.sendEmptyMessage(STATE_ME);

    }

    public void setChiefVote() {
        mHandler.sendEmptyMessage(STATE_VOTE_CHIEF);

    }

    public void setWolf(){
        mHandler.sendEmptyMessage(STATE_WOLF);
    }

    public void setQuitChief() {
        mHandler.sendEmptyMessage(STATE_QUIT_CHIEF);

    }


    /**
     * 未准备状态
     */
    public void unReady() {
        mHandler.sendEmptyMessage(STATE_UNREADY);
    }

    /**
     * 离开状态
     */
    public void noBody() {
        mHandler.sendEmptyMessage(STATE_NOBODY);
    }

    /**
     * 锁定状态
     */
    public void lock() {
        mHandler.sendEmptyMessage(STATE_LOCK);
    }

    /**
     * 语音状态
     */
    public void speak() {
        mHandler.sendEmptyMessage(STATE_SPEAKING);
    }

    public void unSpeak(){
        mHandler.sendEmptyMessage(STATE_SPEAKING_END);

    }

    /**
     * 死亡状态
     */
    public void die() {
        mHandler.sendEmptyMessage(STATE_DIE);
    }

    /**
     * 位置是否存在玩家
     */
    public boolean hasRole() {
        return mRoleData != null;
    }

}
