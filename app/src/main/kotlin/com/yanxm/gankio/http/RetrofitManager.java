package com.yanxm.gankio.http;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.yanxm.gankio.BuildConfig;
import com.yanxm.gankio.MyApplication;
import com.yanxm.gankio.utils.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dell on 2017/5/5.
 * <p>
 * Retrofit 工具类
 */

public class RetrofitManager {

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constant.RETROFIT_HTTP_BASE_URL)
            .client(getNewClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ApiService createService() {
        return retrofit.create(ApiService.class);
    }

    private static OkHttpClient getNewClient() {
        /**
         * HttpLoggingInterceptor提供了4中控制打印信息类型的等级，分别是NONE，BASIC，HEADERS，BODY，接下来分别来说一下相应的打印信息类型。
         *
         * NONE  没有任何日志信息
         * Basic  打印请求类型，URL，请求体大小，返回值状态以及返回值的大小
         * Headers 打印返回请求和返回值的头部信息，请求类型，URL以及返回值状态码
         * Body  打印请求和返回值的头部和body信息
         *
         */
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(MyApplication.getApplication().getCacheDir(), cacheSize);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        return builder.build();
    }
}
