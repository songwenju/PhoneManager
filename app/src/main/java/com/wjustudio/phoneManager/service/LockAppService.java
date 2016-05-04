package com.wjustudio.phoneManager.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.wjustudio.phoneManager.utils.LogUtil;

public class LockAppService extends Service {
    private Context mContext;

    public LockAppService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        LogUtil.d(this, "开启服务");
        super.onCreate();
        mContext = this;
    }
    @Override
    public void onDestroy() {
        LogUtil.d(this, "停止服务");
        super.onDestroy();
    }
}
