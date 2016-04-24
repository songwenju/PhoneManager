package com.wjustudio.phoneManager.Common;

import android.os.Handler;
import android.os.Looper;

import org.litepal.LitePalApplication;

/**
 * 作者：songwenju on 2016/1/13 21:48
 * 邮箱：songwenju01@163.com
 */

public class BaseApplication extends LitePalApplication {

    public static Handler getAppHandler() {
        Looper.prepare();
        return new Handler();
    }
}
