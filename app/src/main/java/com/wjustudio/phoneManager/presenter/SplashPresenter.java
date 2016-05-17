package com.wjustudio.phoneManager.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.os.Handler;

import com.lidroid.xutils.util.IOUtils;
import com.wjustudio.phoneManager.biz.CheckVersionBizImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：songwenju on 2015/12/20 11:08
 * 邮箱：hgdswj@163.com
 * presenter,MVP设计模式,在这里面开启子线程请求网络.
 */
public class SplashPresenter {
    private CheckVersionBizImpl mCheckVersionBiz;
    private Context mContext;

    public SplashPresenter(Context context) {
        mContext = context;
        mCheckVersionBiz = new CheckVersionBizImpl(mContext);
    }

    /**
     * 请求网络更新
     *
     * @param packageInfo
     */
    public void getUpdate(PackageInfo packageInfo) {
        new Handler().postDelayed(new Runnable(){

            public void run() {
               mCheckVersionBiz.enterHomeActivity();
            }

        }, 2000);
//        final int versionCode = packageInfo.versionCode;
////
////        //请求网络在子线程
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                mCheckVersionBiz.checkVersion(String.valueOf(versionCode));
////            }
////        }).start();

    }

    /**
     * 进入主界面
     */
    public void enterHomeActivity() {
        mCheckVersionBiz.enterHomeActivity();
    }

    /**
     * 拷贝数据库到手机中
     *
     * @param dbName
     */
    public void copyDbFromAssets(String dbName) {
        AssetManager assets = mContext.getAssets();
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = assets.open(dbName);
            outputStream = new FileOutputStream(
                    new File(mContext.getFilesDir(), dbName));
            int len;
            byte[] b = new byte[1024];
            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
                outputStream.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }
}
