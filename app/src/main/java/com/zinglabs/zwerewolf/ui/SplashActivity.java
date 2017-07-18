package com.zinglabs.zwerewolf.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.zinglabs.zwerewolf.R;
import com.zinglabs.zwerewolf.event.MsgEvent;
import com.zinglabs.zwerewolf.im.IMLoginClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class SplashActivity extends AppCompatActivity {
    private static Handler mHandle = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        IMLoginClient.instance().connect("192.168.0.151",8090);
        mHandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 1500);
    }

    @Subscribe
    public void onEvent(MsgEvent event) {
        switch (event.getMsgType()) {

        }
    }

}
