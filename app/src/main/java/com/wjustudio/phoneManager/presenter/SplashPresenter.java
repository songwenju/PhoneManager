package com.wjustudio.phoneManager.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.wjustudio.phoneManager.biz.CheckVersionBizImpl;

/**
 * 作者：songwenju on 2015/12/20 11:08
 * 邮箱：hgdswj@163.com
 * presenter,MVP设计模式,在这里面开启子线程请求网络.
 */
public class SplashPresenter {
    private CheckVersionBizImpl mCheckVersionBiz;

    public SplashPresenter (Context context) {
        mCheckVersionBiz = new CheckVersionBizImpl(context);
    }

    /**
     * 请求网络更新
     * @param packageInfo
     */
    public void getUpdate(PackageInfo packageInfo) {
        final int versionCode = packageInfo.versionCode;

        //请求网络在子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCheckVersionBiz.checkVersion(String.valueOf(versionCode));
            }
        }).start();
    }

    /**
     * 进入主界面
     */
    public void enterHomeActivity() {
        mCheckVersionBiz.enterHomeActivity();
    }
}
