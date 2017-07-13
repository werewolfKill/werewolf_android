package com.zinglabs.zwerewolf.config;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.zinglabs.zwerewolf.utils.AppUtil;
import com.zinglabs.zwerewolf.utils.LogUtil;

import java.io.InputStream;

/**
 * Created by wangxiangbo on 2016/7/16.
 */
public class MyGlideModuleConfig implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        if (AppUtil.hasSDCard()) {
            builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "GlideCache", 500 * 1024 * 1024));
            LogUtil.e("Glide使用SD卡缓存");
        } else {
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "GlideCache", 250 * 1024 * 1024));
            LogUtil.e("Glide使用内部缓存");
        }
        builder.setMemoryCache(new LruResourceCache(100 * 1024 * 1024));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //glide.clearDiskCache();
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
        glide.setMemoryCategory(MemoryCategory.NORMAL);
    }
}