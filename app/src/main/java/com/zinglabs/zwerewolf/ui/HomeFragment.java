package com.zinglabs.zwerewolf.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.zinglabs.zwerewolf.R;


/**
 * Created by Administrator on 2017/3/7.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private AppCompatActivity activity;
    private View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (AppCompatActivity) getActivity();
        root = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return root;
    }

    private void init() {
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.fragment_home);

        toolbar.findViewById(R.id.menu_home_setting).setOnClickListener(this);

        ImageView head_iv = (ImageView) root.findViewById(R.id.home_head_iv);
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
                showToast("暂未开发,敬请期待...");
                break;
            case R.id.menu_home_setting:  //设置
                showToast("暂未开发...");
                break;
            case R.id.home_help_iv:   //帮助
                showToast("暂未开发...");
                break;
            case R.id.home_search_iv:  //搜索房间
                showToast("暂未开发...");
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

    private void createRoom(){
        showToast("暂未开发...");
        System.out.println("创建房间");

    }

    private void showToast(String str){

        Toast.makeText(activity,str,Toast.LENGTH_SHORT).show();

    }
}
