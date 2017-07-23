package com.zinglabs.zwerewolf.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;


/**
 * Created by wangxiangbo on 2016/7/8.
 */
public class GlideUtil {

//            Glide.with((Activity) obj).load(url).placeholder(R.mipmap.place_holder_img).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
//            Glide.with((Activity) obj).load(url).placeholder(R.mipmap.place_holder_img).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(iv);
//            Glide.with((Activity) obj).load(url).placeholder(R.mipmap.place_holder_mine_header).diskCacheStrategy(DiskCacheStrategy.SOURCE).transform(new GlideCircleTransform((Activity) obj)).into(iv);
//            Glide.with((Activity) obj).load(url).placeholder(place_holder_img).diskCacheStrategy(DiskCacheStrategy.SOURCE).transform(new GlideRoundTransform((Activity) obj)).into(iv);

    public static void into(Activity activity, Object resPath, ImageView iv) {
        into(activity, resPath, iv, null);
    }

    public static void into(Activity activity, Object resPath, ImageView iv, BitmapTransformation bt) {
        if (!AppUtil.isSafe(activity)) {
            return;
        }
        if (resPath == null) {
            return;
        }
        if (bt == null) {
            Glide.with(activity).load(resPath).into(iv);
        } else {
            Glide.with(activity).load(resPath).transform(bt).into(iv);
        }
    }


   // public final static GlideCircleTransform CIRCLE = new GlideCircleTransform(App.app);
    //public final static GlideRoundTransform ROUND = new GlideRoundTransform(App.app);

    /**
     * 转圆形
     */
    public static class GlideCircleTransform extends BitmapTransformation {
        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    /**
     * 转圆角
     */
    public static class GlideRoundTransform extends BitmapTransformation {
        private final static int DEFAULT_SIZE = 5;
        private static float radius = 0f;

        public GlideRoundTransform(Context context) {
            this(context, DEFAULT_SIZE);
        }

        public GlideRoundTransform(Context context, int dp) {
            super(context);
            radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }
}
