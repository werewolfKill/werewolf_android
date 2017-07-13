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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_easy_iv:
                easy();
                break;
            case R.id.menu_home_setting:
                //showToast("setting");
                break;
            case R.id.home_help_iv:
                //showToast("help");
                break;
        }
    }

    public void easy() {
     //加入房间之前的参数
      String channel = "wzc25151";
     //  App.mAudioSettings.mChannelName = channel;
       Intent i = new Intent(activity, GameActivity.class);
       i.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, channel);
       startActivity(i);
        startActivity(new Intent(activity, GameActivity.class));
    }
}
