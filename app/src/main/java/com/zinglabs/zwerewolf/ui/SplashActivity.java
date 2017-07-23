package com.zinglabs.zwerewolf.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.zinglabs.zwerewolf.R;
import com.zinglabs.zwerewolf.constant.GlobalData;
import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.entity.User;
import com.zinglabs.zwerewolf.event.UserLoginEvent;
import com.zinglabs.zwerewolf.im.IMClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;


public class SplashActivity extends AppCompatActivity {
    private static Handler mHandle = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        EventBus.getDefault().register(this);
        init();
    }
    Map userMap = new HashMap();
    private void init() {
        userMap.put("name","张三"+((int)(Math.random()*100)));
        userMap.put("password","11111");
        IMClient.getInstance().send(ProtocolConstant.SID_USER + "/" + ProtocolConstant.CID_USER_LOGIN_REQ ,userMap);
    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        User user = new User(event.getUserId(),(String) userMap.get("name"));
        GlobalData globalData = (GlobalData)getApplication();
        globalData.setUser(user);
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
