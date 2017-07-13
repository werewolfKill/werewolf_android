package com.zinglabs.zwerewolf.manager;

import android.content.Context;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zinglabs.zwerewolf.utils.LogUtil;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by wangxiangbo on 2016/7/5.
 */
public class HttpManager {
    private static final long MAX_CONNECT_TIMEOUT = 10000L;
    private static final long MAX_READ_TIMEOUT = 10000L;
    private static Context context;
    private static boolean isInitialized;

    /**
     * 使用默认的HttpClient
     */
    public static void initializeDefaultHttpClient(Context con) {
        if (!isInitialized) {
            context = con;
            initialize(false, null, null, null);
        }
    }

    /**
     * 使用默认的HttpsClient
     */
    public static void initializeDefaultHttpsClient(Context con) {
        if (!isInitialized) {
            context = con;
            initialize(true, null, null, null);
        }
    }

    /**
     * @param isHttps  是否使用Https
     * @param online   证书
     * @param loc      本地证书
     * @param password 本地证书密码
     */
    public static void initialize(Context con, boolean isHttps, InputStream[] online, InputStream loc, String password) {
        if (!isInitialized) {
            context = con;
            initialize(isHttps, online, loc, password);
        }
    }

    private static void initialize(boolean isHttps, InputStream[] online, InputStream loc, String password) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(context));
        HttpsUtils.SSLParams sslParams = null;
        if (isHttps) {
            sslParams = HttpsUtils.getSslSocketFactory(online, loc, password);
        }
        if (sslParams != null) {
            httpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        }
        httpClientBuilder.connectTimeout(MAX_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(MAX_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        OkHttpUtils.initClient(httpClientBuilder.build());
        isInitialized = true;
        LogUtil.e("OkHttpClient初始化:支持Https = " + isHttps);
    }
}
