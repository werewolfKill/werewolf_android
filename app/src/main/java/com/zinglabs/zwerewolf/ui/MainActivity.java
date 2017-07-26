package com.zinglabs.zwerewolf.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zinglabs.zwerewolf.R;
import com.zinglabs.zwerewolf.constant.GlobalData;
import com.zinglabs.zwerewolf.entity.User;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private int[] arr_tab = new int[]{R.mipmap.icon_tab_game, R.mipmap.icon_tab_message};
    private int[] arr_tab_s = new int[]{R.mipmap.icon_tab_game_s, R.mipmap.icon_tab_message_s};

    private LinearLayout tag_v;
    private ViewPager vp;

    private ScaleAnimation scaleAnimation;
    private Context context;
    //    public native String stringFromJNI();
    static {
//        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        List<String> permissionNeeded = new ArrayList<>();
        if (!hasPermission(context, Manifest.permission.RECORD_AUDIO)) {
            permissionNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if(!permissionNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this, permissionNeeded.toArray(new String[permissionNeeded.size()]), 80);
        }

        init();
        initTagbar();
        // Example of a call to a native method
//    TextView tv = (TextView) findViewById(R.id.sample_text);
//    tv.setText(stringFromJNI());
    }

    private void init() {
        tag_v = (LinearLayout) findViewById(R.id.main_tag_v);
        vp = (ViewPager) findViewById(R.id.main_vp);

        GlobalData globalData = (GlobalData)getApplication();
        User user = globalData.getUser();
        Fragment homeFg = new HomeFragment();
        Fragment msgFg = new MessageFragment();
        Bundle userBl = new Bundle();
        userBl.putInt("userId",user.getId());
        homeFg.setArguments(userBl);

        final Fragment[] arr_fg = new Fragment[]{homeFg,msgFg};
        vp.setOffscreenPageLimit(arr_fg.length);

        vp.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return arr_fg[position];
            }

            @Override
            public int getCount() {
                return arr_fg.length;
            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < arr_fg.length; i++) {
                    TextView textView = (TextView) tag_v.getChildAt(i);
                    if (i == position) {
                        textView.setTextColor(getResources().getColor(R.color.text_red));
                        Drawable drawable = getResources().getDrawable(arr_tab_s[i]);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        textView.setCompoundDrawables(null, drawable, null, null);
                    } else {
                        textView.setTextColor(getResources().getColor(R.color.text_gray));
                        Drawable drawable = getResources().getDrawable(arr_tab[i]);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        textView.setCompoundDrawables(null, drawable, null, null);
                    }
                }
                startAnimation(tag_v.getChildAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    public boolean hasPermission(Context context, String permName) {
        int perm = context.checkCallingOrSelfPermission(permName);
        return perm == PackageManager.PERMISSION_GRANTED;
    }
    private void initTagbar() {
        for (int i = 0; i < arr_tab.length; i++) {
            final int j = i;
            tag_v.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vp.setCurrentItem(j);
                }
            });
        }
    }

    private void startAnimation(View view) {
        if (scaleAnimation == null) {
            scaleAnimation = new ScaleAnimation(1.2F, 1F, 1.2F, 1F, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        }
        scaleAnimation.setInterpolator(new BounceInterpolator());
        scaleAnimation.setDuration(350);
        view.startAnimation(scaleAnimation);
    }

}
