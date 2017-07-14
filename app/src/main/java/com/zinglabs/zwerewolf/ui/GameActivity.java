package com.zinglabs.zwerewolf.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zinglabs.zwerewolf.R;
import com.zinglabs.zwerewolf.controller.SimpleController;
import com.zinglabs.zwerewolf.data.GameChatData;
import com.zinglabs.zwerewolf.data.RoleBuild;
import com.zinglabs.zwerewolf.data.RoleData;
import com.zinglabs.zwerewolf.data.UserData;
import com.zinglabs.zwerewolf.event.GameStateMessage;
import com.zinglabs.zwerewolf.event.MsgEvent;
import com.zinglabs.zwerewolf.manager.DialogManager;
import com.zinglabs.zwerewolf.role.Role;
import com.zinglabs.zwerewolf.utils.AppUtil;
import com.zinglabs.zwerewolf.utils.DateUtil;
import com.zinglabs.zwerewolf.widget.RoleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;


/**
 * Created by Administrator on 2017/3/7.
 */

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private final int lvSize = 120;//定义可显示的最多聊天数量
    private int playerCount = 10;//模拟玩家人数
    private int curPlayerNumber = -1;//当前玩家持有的编号，默认是房主
    private TextView title_tv;
    private LinearLayout bar_left;
    private LinearLayout bar_right;
    private List<RoleView> roleViewList = new ArrayList<RoleView>();
    private View myRoleBg_v;
    private View myRole_v;
    private TextView myRole_tv;
    private ListView lv;
    private ImageButton keyIb;
    private View voice_v;
    private View key_v;
    private EditText et;
    private ImageButton startIB;
    // 面板View
    private KPSwitchPanelLinearLayout mPanelLayout;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!AppUtil.isSafe(GameActivity.this)) {
//                mHandler = null;
                return;
            }
            GameStateMessage gameStateMessage = null;
            if (msg.obj instanceof GameStateMessage) {
                gameStateMessage = (GameStateMessage) msg.obj;
            }
            GameChatData gameChatData = null;


            switch (msg.what) {
                //角色部署完成
                case GameStateMessage.ROLE_DEPLOY:
                    Map<Integer, Role> deployMap = (Map<Integer, Role>) msg.obj;
                    Role role = deployMap.get(curPlayerNumber);

                    myRole_v.setVisibility(View.VISIBLE);
                    myRoleBg_v.setVisibility(View.VISIBLE);
                    myRole_tv.setText("我的身份：" + role.getName());
                    break;

                //天亮天黑变化，修改房间标题
                case GameStateMessage.ZHOUYE:
                    String zhouYe = gameStateMessage.getText();
                    title_tv.setText("简单25151房" + gameStateMessage.getText());

                    if (simpleController.isAlive(curPlayerNumber)) {
                        DialogManager.showGameDay(GameActivity.this, myRole_v, zhouYe, null, null);
                    } else {
                        DialogManager.showGameDay(GameActivity.this, myRole_v, "死", null, null);
                    }
                    break;

                //倒计时内容
                case GameStateMessage.COUNTDOWNTIMER:
                    DialogManager.showGameDay(GameActivity.this, myRole_v, null, gameStateMessage.getText(), null);
                    doTimer(gameStateMessage.getTime());
                    break;

                //接收到聊天信息
                case GameStateMessage.CHAT:
                    gameChatData = new GameChatData(GameChatData.CHAT, DateUtil.nowLongStr(), GameChatData.SYSTEM_CHAT, "", gameStateMessage.getText());
                    chatAdapter.update(gameChatData);
                    break;

                //游戏开始界面变化
                case GameStateMessage.GAME_START:
                    gameChatData = new GameChatData(GameChatData.CHAT, DateUtil.nowLongStr(), GameChatData.SYSTEM_CHAT, "", gameStateMessage.getText());
                        chatAdapter.update(gameChatData);

                        for (RoleView roleView : roleViewList) {
                            if (roleView.hasRole()) {
                                roleView.unReady();
                            }
                    }
                    break;

                //游戏结束界面变化
                case GameStateMessage.GAME_OVER:
                    DialogManager.dismissDialog();

                    gameChatData = new GameChatData(GameChatData.CHAT, DateUtil.nowLongStr(), GameChatData.SYSTEM_CHAT, "", gameStateMessage.getText());
                    chatAdapter.update(gameChatData);
                    for (RoleView roleView : roleViewList) {
                        if (roleView.hasRole()) {
                            roleView.unReady();
                        }
                    }

                    startIB.setVisibility(View.VISIBLE);
                    myRole_v.setVisibility(View.GONE);
                    myRoleBg_v.setVisibility(View.GONE);
                    break;

                //游戏内语音界面变化
                case GameStateMessage.GAME_SPEAK:
                    String curStage = simpleController.getCurStage();
                    if (curStage.equals(SimpleController.TAOLUN) || curStage.equals(SimpleController.TOUPIAO)) {
                        List<Integer> alive_List = simpleController.getAliveList();
                        int i = new Random().nextInt(alive_List.size());
                        roleViewList.get(alive_List.get(i)-1).chat();
                        autoChat();
                    }
                    break;

                case GameStateMessage.STAGE_CHANGE:
                    List<Integer> aliveList = simpleController.getAliveList();
                    for (int i = 0; i < roleViewList.size(); i++) {
                        RoleView roleView = roleViewList.get(i);
                        if (!roleView.hasRole()) continue;
                        Role role1 = simpleController.getRole(i+1);
                        if (!aliveList.contains(roleView.getmRoleData().getNumber())) {
                            roleView.die();
                        } else if (role1 != null && role1.getState().equals(Role.STATE_TALK)) {
                            roleView.speak();
                        } else {
                            roleView.unReady();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        EventBus.getDefault().register(this);
        init();
        // initUIandEvent();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.icon_nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框，确定退出游戏界面
                startActivity(new Intent(GameActivity.this, MainActivity.class));
                finish();
            }
        });

        title_tv = (TextView) findViewById(R.id.game_title_tv);

        bar_left = (LinearLayout) findViewById(R.id.room_bar_left);
        bar_right = (LinearLayout) findViewById(R.id.room_bar_right);

        lv = (ListView) findViewById(R.id.room_chat_lv);
        lv.setAdapter(chatAdapter);

        myRoleBg_v = findViewById(R.id.room_myrolebg_v);
        myRole_v = findViewById(R.id.room_myrole_v);
        myRole_tv = (TextView) findViewById(R.id.room_myrole_tv);

        keyIb = (ImageButton) findViewById(R.id.room_key_switch_ib);
        keyIb.setOnClickListener(this);

        voice_v = findViewById(R.id.room_voice_v);
        key_v = findViewById(R.id.room_key_v);
        et = (EditText) findViewById(R.id.room_et);

        findViewById(R.id.room_send_tv).setOnClickListener(this);

        startIB = (ImageButton) findViewById(R.id.room_start_ib);
        startIB.setOnClickListener(this);

        voice_v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

               // RtcEngine rtcEngine = rtcEngine();
                // TODO
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
//                        EventBus.getDefault().post(new MsgEvent(MsgEvent.ROOM_OVER));
                        //开始发言，反之取消发言
                       // rtcEngine.muteLocalAudioStream(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        //rtcEngine.muteLocalAudioStream(true);
                        break;
                }
                return false;
            }
        });

        simulate();

        simpleController = new SimpleController(mHandler);
    }

    private SimpleController simpleController;

    private void autoChat() {
        int time = new Random().nextInt(7) * 1000 + 3000;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(GameStateMessage.GAME_SPEAK);
                }
        }, time);
    }

    // 邀请和帮助样式
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }
    // 邀请和帮助触发方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_game_invite:
               // showToast("invite");
                break;
            case R.id.menu_game_help:
               // showToast("help");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.room_key_switch_ib: // 聊天模式切换
                AppUtil.closeBoard(GameActivity.this);
                if (voice_v.isShown()) {
                    keyIb.setBackgroundResource(R.drawable.room_im_voice_selected);
                    voice_v.setVisibility(View.GONE);
                    key_v.setVisibility(View.VISIBLE);
                } else {
                    keyIb.setBackgroundResource(R.drawable.room_im_keyborad_selected);
                    voice_v.setVisibility(View.VISIBLE);
                    key_v.setVisibility(View.GONE);
                }
                break;
            case R.id.room_send_tv: // 发送文字
                String msg = et.getText().toString().trim();
                if (msg == null || msg.length() < 1) {
                    return;
                }
                et.setText("");
                GameChatData gameChatData = new GameChatData(GameChatData.CHAT, new Date().getTime() + "", UserData.user().getNickName(), "", msg);
                MsgEvent msgEvent = new MsgEvent(MsgEvent.ROOM_CHAT, null, gameChatData);
                EventBus.getDefault().post(msgEvent);
                break;
            //开局
            case R.id.room_start_ib:
                startIB.setVisibility(View.GONE);
                simpleController.startGame(playerCount);
                break;
        }
    }

    @Subscribe
    public void onEvent(MsgEvent event) {
        switch (event.getMsgType()) {
            case MsgEvent.ROOM_CHAT:
                chatAdapter.update(event.getObj());
                break;
            case MsgEvent.ROOM_START:
                //所有玩家进入分配角色阶段
//                if (gameController.isGameing()) {
//                    return;
//                }
//                gameController.gameStart(playerCount);
//                for (RoleView roleView : roleViewList) {
//                    if (roleView.hasRole()) {
//                        roleView.unReady();
//                    }
//        }
                break;
            case MsgEvent.ROOM_OVER:
//                if (!gameController.isGameing()) {
//                    return;
//                }
//                gameController.gameOver();
//                for (RoleView roleView : roleViewList) {
//                    if (roleView.hasRole()) {
//                        roleView.ready();
//                    }
//                }
                break;
            case MsgEvent.ROOM_ROLE:
//                if (curPlayerNumber < 0) {
//                    return;
//                }
//                //法官发布开局角色组成
//                String msgStr = event.getMsgStr();
//                if (msgStr != null && msgStr.length() > 0) {
//                    GameChatData gameChatData = new GameChatData(GameChatData.CHAT, new Date().getTime() + "", GameChatData.SYSTEM_CHAT, "", msgStr);
//                    chatAdapter.update(gameChatData);
//                    return;
//                }
//                //显示我的身份
//                int[] arrRoleDeploy = gameController.getArrRoleDeploy();
//                String roleName = GameAlloter.getRoleName(arrRoleDeploy[curPlayerNumber - 1]);
//                myRole_tv.setText("我的身份是：" + roleName);
                break;
            case MsgEvent.ROOM_ROLE_STATE_CHANGE:
//                List<Role> roleList = gameController.getRoleList();
//                for (int i = 0; i < roleList.size(); i++) {
//                    if (roleList.get(i).isAlive()) {
//                        roleViewList.get(i).unReady();
//                    } else {
//                        roleViewList.get(i).die();
//                    }
//                }
                break;
        }
    }

    private CountDownTimer timer;

    private void doTimer(long time) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                day_v.setVisibility(View.VISIBLE);
//                day_timer_tv.setText(millisUntilFinished / 1000 + " s");
                DialogManager.showGameDay(GameActivity.this, myRole_v, null, null, millisUntilFinished / 1000 + " s");
            }

            @Override
            public void onFinish() {
//                day_v.setVisibility(View.GONE);
                DialogManager.dismissDialog();
            }
        }.start();
    }

    private void simulate() {
        //模拟角色，随机房主，号码大于1
        int random = new Random().nextInt(10) + 1;
        for (int i = 1; i <= 16; i++) {
            RoleView roleView = new RoleView(GameActivity.this);
            roleViewList.add(roleView);
            if (i <= 8) {
                bar_left.addView(roleView);
            } else {
                bar_right.addView(roleView);
            }
            if (i <= playerCount) {
                RoleData roleData = RoleBuild.build(i);
                if (i == random) {
                    //模拟用户为房主
                    curPlayerNumber = i;
                    roleData = RoleBuild.build(i, UserData.user());
                    roleData.setOwner(true);
                }
                roleView.setup(roleData);
            }
        }
    }

    private void clear() {
        if (simpleController != null && simpleController.isGameing()) {
            simpleController.stopGame();
            simpleController = null;
        }
        if (roleViewList != null) {
            roleViewList.clear();
            roleViewList = null;
        }
        if (chatAdapter != null) {
            chatAdapter.clear();
            chatAdapter = null;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        clear();
       // deInitUIandEvent();
        super.onDestroy();
    }

    private GameChatAdapter chatAdapter = new GameChatAdapter();

    private class GameChatAdapter extends BaseAdapter {
        private List<GameChatData> datas = new ArrayList<GameChatData>();

        public GameChatAdapter() {
            GameChatData timeData = new GameChatData(GameChatData.DATE, new Date().getTime() + "", UserData.user().getNickName(), "", null);
            datas.add(timeData);
        }

        public void clear() {
            datas.clear();
        }

        public void update(Object obj) {
            if (obj == null) {
                return;
            }
            if (obj instanceof List) {
                datas.addAll((List) obj);
                update((List) obj);
            } else if (obj instanceof GameChatData) {
                datas.add((GameChatData) obj);
            }
//减少聊天数量，减小内存损耗
            if (datas.size() > lvSize) {
                datas.remove(datas.subList(0, 30));
            }
            notifyDataSetChanged();
            if (key_v.isShown()) {
                lv.setSelection(lv.getBottom());
                return;
            }
//            if (datas.size() - lv.getLastVisiblePosition() > 2) {
//                showToast("新消息");
//            } else {
            lv.setSelection(lv.getBottom());
//            }
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        private void gone(ViewHolder viewHolder) {
            viewHolder.time_tv.setVisibility(View.GONE);
            viewHolder.entry_tv.setVisibility(View.GONE);
            viewHolder.other_tv.setVisibility(View.GONE);
            viewHolder.other_msg_tv.setVisibility(View.GONE);
            viewHolder.mine_tv.setVisibility(View.GONE);
            viewHolder.mine_msg_tv.setVisibility(View.GONE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GameChatData data = datas.get(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(GameActivity.this).inflate(R.layout.item_chat_game, null);

                viewHolder.time_tv = (TextView) convertView.findViewById(R.id.item_chat_time_tv);
                viewHolder.entry_tv = (TextView) convertView.findViewById(R.id.item_chat_entry_tv);
                viewHolder.other_tv = (TextView) convertView.findViewById(R.id.item_chat_other_tv);
                viewHolder.other_msg_tv = (TextView) convertView.findViewById(R.id.item_chat_other_msg_tv);
                viewHolder.mine_tv = (TextView) convertView.findViewById(R.id.item_chat_mine_tv);
                viewHolder.mine_msg_tv = (TextView) convertView.findViewById(R.id.item_chat_mine_msg_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            gone(viewHolder);
            switch (data.getType()) {
                case GameChatData.DATE:
                    viewHolder.time_tv.setText(DateUtil.dateFormat(data.getDate()));
                    viewHolder.time_tv.setVisibility(View.VISIBLE);
                    break;
                case GameChatData.ENTRY:
                    viewHolder.entry_tv.setText(data.getFrom() + " 进入房间");
                    viewHolder.entry_tv.setVisibility(View.VISIBLE);
                    break;
                case GameChatData.CHAT:
                    //判断消息是否从本角色发出
                    String nickName = UserData.user().getNickName();
                    String from = data.getFrom();
                    if (nickName != null && nickName.equals(from)) {
                        //本玩家聊天
                        viewHolder.mine_tv.setVisibility(View.VISIBLE);
                        viewHolder.mine_msg_tv.setVisibility(View.VISIBLE);
                        viewHolder.mine_tv.setText(data.getFrom());
                        viewHolder.mine_msg_tv.setText(data.getText());
                    } else {
                        //判断是否系统聊天
                        viewHolder.other_tv.setVisibility(View.VISIBLE);
                        viewHolder.other_msg_tv.setVisibility(View.VISIBLE);
                        viewHolder.other_msg_tv.setText(data.getText());
                        if (GameChatData.SYSTEM_CHAT.equals(from)) {
                            Drawable drawable = getResources().getDrawable(R.mipmap.icon_room_system);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            viewHolder.other_tv.setCompoundDrawables(drawable, null, null, null);
                            viewHolder.other_tv.setText(GameChatData.SYSTEM_NAME);
                        } else {
                            viewHolder.other_tv.setCompoundDrawables(null, null, null, null);
                            viewHolder.other_tv.setText(data.getFrom());
                        }
                    }
                    break;
            }

            return convertView;
        }
    }

    private class ViewHolder {
        TextView time_tv;
        TextView entry_tv;
        TextView other_tv;
        TextView other_msg_tv;
        TextView mine_tv;
        TextView mine_msg_tv;
    }

}
