package com.yanxm.gankio;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by dell on 2017/8/23.
 */

public class MyApplication extends Application {

    private static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Stetho.initializeWithDefaults(this);
    }

    public static Application getApplication(){
        return application;
    }
}
