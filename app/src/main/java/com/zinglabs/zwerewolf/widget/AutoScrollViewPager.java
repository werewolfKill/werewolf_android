package com.zinglabs.zwerewolf.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zinglabs.zwerewolf.R;
import com.zinglabs.zwerewolf.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxiangbo on 2016/7/13.
 */
public class AutoScrollViewPager extends ViewPager {
    private Context context;
    private RelativeLayout indicator;
    private TextView indicator_tv;
    private ArrayList<ImageView> ringList;
    private List<ImageView> imgList;

    //    间隔时间，默认间隔4s
    private long INTERVAL_TIME = 4000L;
    private int pageCount;
    private boolean isAuto;
    private boolean isDragging;
    private Message mMsg;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (!isDragging) {
                        autoScroll();
                    }
                    if (isAuto) {
                        mMsg = new Message();
                        mMsg.what = 0;
                        sendMessageDelayed(mMsg, INTERVAL_TIME);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(myPageChangeListener);
    }

    public void setupData(List<ImageView> imgList) {
        this.context = getContext();
        this.imgList = imgList;
        this.pageCount = imgList.size();
        setAdapter(new AutoScrollViewPagerAdapter(imgList));
        if (pageCount > 1) {
            setCurrentItem(400, false);
            setAutoScroll(true);
        }
    }

    public void setupIndicator(RelativeLayout indicator) {
        this.indicator = indicator;
        if (indicator == null) {
            return;
        }
        if (indicator.getChildCount() > 0) {
            indicator.removeAllViews();
        }
        ringList = new ArrayList<ImageView>();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(lp);
        indicator.addView(ll);
        LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(DisplayUtil.dp2px(7), DisplayUtil.dp2px(7));
        for (int i = 0; i < pageCount; i++) {
            ImageView iv = new ImageView(context);
            iv.setLayoutParams(lpp);
            if (pageCount == 1) {
                iv.setBackgroundResource(R.drawable.ring_imageview_select);
            }
            if (i > 0) {
                lpp.leftMargin = DisplayUtil.dp2px(3);
            }
            ringList.add(iv);
            ll.addView(iv);
        }
    }


    public void setAutoScroll(boolean isAuto) {
        if (this.isAuto == isAuto) {
            return;
        } else {
            this.isAuto = isAuto;
            if (isAuto) {
                mMsg = new Message();
                mMsg.what = 0;
                mHandler.sendMessageDelayed(mMsg, INTERVAL_TIME);
            }
        }
    }

    private void autoScroll() {
        int count = getAdapter().getCount();
        int cur = getCurrentItem();
        if (count <= 1) {
            isAuto = false;
            return;
        }
        if (cur < count - 1) {
            setCurrentItem(++cur);
            return;
        }
        if (cur == count - 1) {
            setCurrentItem(getAdapter().getCount() / 2);
        }
    }

    private OnPageChangeListener myPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            int cur = position % pageCount;
            if (ringList == null || ringList.size() == 0) {
                return;
            }
            for (ImageView iv : ringList) {
                if (iv.equals(ringList.get(cur))) {
                    iv.setBackgroundResource(R.drawable.ring_imageview_select);
                } else {
                    iv.setBackgroundResource(R.drawable.ring_imageview_default);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            isDragging = state != ViewPager.SCROLL_STATE_IDLE;
        }
    };

    private class AutoScrollViewPagerAdapter extends PagerAdapter {
        private List<ImageView> imgList;

        public AutoScrollViewPagerAdapter(List<ImageView> imgList) {
            this.imgList = imgList;
        }

        @Override
        public int getCount() {
            if (imgList.size() == 1) {
                return 1;
            }
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            position = position % bannerMap.size() == 0 ? bannerMap.size() - 1 : position % bannerMap.size() - 1;
            position %= imgList.size();
            ImageView iv = imgList.get(position);
            ViewParent vp = iv.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(iv);
            }
            container.addView(iv);
            return iv;
        }
    }

    private class MyOnClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
        }
    }
}

