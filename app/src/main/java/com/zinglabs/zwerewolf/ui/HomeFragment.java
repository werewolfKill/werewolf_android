package com.zinglabs.zwerewolf.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.zinglabs.zwerewolf.R;
import com.zinglabs.zwerewolf.config.Constants;
import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.entity.Room;
import com.zinglabs.zwerewolf.event.HomeFragmentEvent;
import com.zinglabs.zwerewolf.manager.DialogManager;
import com.zinglabs.zwerewolf.utils.IMClientUtil;
import com.zinglabs.zwerewolf.utils.MathUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017/3/7.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private AppCompatActivity activity;
    private View root;

    private View rl;

    private EditText select_room_et;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (AppCompatActivity) getActivity();
        root = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.fragment_home);

        toolbar.findViewById(R.id.menu_home_setting).setOnClickListener(this);

        ImageView head_iv = (ImageView) root.findViewById(R.id.home_head_iv);

//        select_room_et = new EditText(true);
        // GlideUtil.into(activity, R.mipmap.my_head, head_iv, GlideUtil.CIRCLE);

        root.findViewById(R.id.home_easy_iv).setOnClickListener(this);

        root.findViewById(R.id.home_help_iv).setOnClickListener(this);
        root.findViewById(R.id.home_search_iv).setOnClickListener(this);
        root.findViewById(R.id.home_standard_iv).setOnClickListener(this);
        root.findViewById(R.id.home_create_iv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_easy_iv:  //简单模式
                easy();
                break;
            case R.id.home_standard_iv: //标准模式
                DialogManager.showToast(getActivity(),"暂未开发,敬请期待...");
                break;
            case R.id.menu_home_setting:  //设置
                DialogManager.showToast(getActivity(),"暂未开发...");
                break;
            case R.id.home_help_iv:   //帮助
                DialogManager.showToast(getActivity(),"暂未开发...");
                break;
            case R.id.home_search_iv:  //搜索房间
                searchRoom();
                break;
            case R.id.home_create_iv:  //创建房间
                createRoom();
                break;

        }
    }

    private void easy() {
        //加入房间之前的参数
        String channel = "wzc25151";
        //  App.mAudioSettings.mChannelName = channel;
        Intent i = new Intent(activity, GameActivity.class);
        i.putExtra("ecHANEL", channel);
        startActivity(i);
        startActivity(new Intent(activity, GameActivity.class));
    }

    private void createRoom() {
        Bundle bundle = getArguments();
        int userId = bundle.getInt("userId", 0);
        Map<String, Integer> param = new HashMap<>();
        param.put("fromId", userId);
        showModalChoice(param);

    }

    private void searchRoom() {
        Bundle bundle = getArguments();
        int userId = bundle.getInt("userId", 0);
        Map<String, Integer> param = new HashMap<>();
        param.put("fromId", userId);
        showSearchRoomDialog(param);
    }


    @Subscribe
    public void onEvent(HomeFragmentEvent event) {
        int userId = event.getUserId();
        int roomId = event.getRoomId();
        int modelId = event.getModelId();
        int code = event.getCode();
        Room room = event.getRoom();
        Intent roomIntent;
        Bundle bundle;
        switch (code) {
            case HomeFragmentEvent.CREATE_ROOM_SUC:
                roomIntent = new Intent(activity, GameActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("room", room);
                roomIntent.putExtras(bundle);
                startActivity(roomIntent);
                break;
            case HomeFragmentEvent.SEARCH_ROOM_SUC:
                bundle = new Bundle();
                roomIntent = new Intent(activity, GameActivity.class);
                bundle.putSerializable("room", room);
                roomIntent.putExtras(bundle);
                startActivity(roomIntent);
                break;

            case HomeFragmentEvent.CREATE_ROOM_FAIL:
                DialogManager.showToast(getActivity(),"房间创建失败");
                break;
            case HomeFragmentEvent.SEARCH_ROOM_NOT_EXIST:
                DialogManager.showToast(getActivity(),"房间不存在，请输入正确的房间号");
                break;
            case HomeFragmentEvent.SEARCH_ROOM_ALREADY_FULL:
                DialogManager.showToast(getActivity(),"该房间已满");
                break;
            case HomeFragmentEvent.SEARCH_ROOM_FAIL:
                DialogManager.showToast(getActivity(),"搜索失败");
                break;
        }

    }

    private void showModalChoice(Map<String, Integer> param) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("请选择游戏模式");
        //    指定下拉列表的显示数据
        final String[] modals = {"9人局", "10人局", "12人-预女猎守", "12人-预女猎白"};
        builder.setItems(modals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int modal = Constants.MODEL_12_YNLS;
                if (which == 0) {
                    modal = Constants.MODEL_9;
                } else if (which == 1) {
                    modal = Constants.MODEL_10;
                } else if (which == 3) {
                    modal = Constants.MODEL_12_YNLB;
                }
                param.put("content", modal);
                IMClientUtil.sendMsg(ProtocolConstant.SID_BNS, ProtocolConstant.CID_BNS_CRE_ROOM_REQ, param);
            }
        });
        //    设置一个下拉的列表选择项
        builder.setNegativeButton("取消", null);

        builder.show();
    }

    private void showSearchRoomDialog(Map<String, Integer> param) {

        final EditText et = new EditText(getActivity());

        new AlertDialog.Builder(getActivity()).setTitle("请输入房间号")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String roomNum = et.getText().toString();
                        if (roomNum.trim().length() == 0) {
                            new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("请输入房间号")
                                    .setPositiveButton("确定", null).show();
                            return;
                        }
                        if (!MathUtil.isNumeric(roomNum)) {
                            new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("请输入正确房间号")
                                    .setPositiveButton("确定", null).show();
                            return;
                        }
                        param.put("content", Integer.parseInt(roomNum));
                        IMClientUtil.sendMsg(ProtocolConstant.SID_BNS, ProtocolConstant.CID_BNS_FIND_ROOM_REQ, param);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
