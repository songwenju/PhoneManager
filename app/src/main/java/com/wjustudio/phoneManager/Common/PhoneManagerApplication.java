package com.wjustudio.phoneManager.Common;

import android.content.Context;
import android.os.Handler;

import org.litepal.LitePalApplication;

/**
 * 作者：songwenju on 2016/1/13 21:48
 * 邮箱：songwenju01@163.com
 */
public class PhoneManagerApplication extends LitePalApplication{
    public static Context mAppContext;
    public static Handler mAppHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
        mAppHandler = new Handler();
    }
}
