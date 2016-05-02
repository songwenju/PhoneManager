package com.example.rxjavademo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * 作者：songwenju on 2016/1/13 21:48
 * 邮箱：songwenju01@163.com
 */

public class BaseApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Handler getAppHandler() {
        Looper.prepare();
        return new Handler();
    }
    public static  Context getContext(){
        return  mContext;
    }
}
