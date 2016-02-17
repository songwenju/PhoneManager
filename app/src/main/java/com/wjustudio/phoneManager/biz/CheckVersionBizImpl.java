package com.wjustudio.phoneManager.biz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wjustudio.phoneManager.activities.HomeActivity;
import com.wjustudio.phoneManager.activities.SplashActivity;
import com.wjustudio.phoneManager.commonInterface.Url;
import com.wjustudio.phoneManager.javaBean.VersionInfo;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.utils.OkHttpUtil;
import com.wjustudio.phoneManager.utils.ToastUtil;

import java.io.File;
import java.io.IOException;

/**
 * 作者：songwenju on 2015/12/20 18:21
 * 邮箱：songwenju01@163.com
 * 在这里面请求网络去校验版本是否有更新.
 */
public class CheckVersionBizImpl implements ICheckVersionBiz {
    public Context mContext;

    public CheckVersionBizImpl(Context context) {
        mContext = context;
    }

    @Override
    public void checkVersion(String versionCode) {
        try {
            String jsonString = OkHttpUtil.getStringFromServer(Url.checkVersion);
            final VersionInfo versionInfo = JSON.parseObject(jsonString, VersionInfo.class);
            LogUtil.e(this, versionInfo.desc);
            LogUtil.e(this, versionInfo.version);
            LogUtil.e(this, jsonString);
            if (!versionCode.equals(versionInfo.version)) {
                //存在新版本,弹出对话框,下载后弹出安装界面.
                LogUtil.e(this, "存在新版本");
                CommonUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        CommonUtil.showInfoDialog(mContext, versionInfo.desc, "发现新版本"
                                , "确定", "取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //下载app
                                        ToastUtil.showToast("开始下载");
                                        download(versionInfo.downloadUrl);

                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        enterHomeActivity();
                                    }
                                }
                        );
                    }
                });
            } else {
                //不存在新版本,进入主界面
                LogUtil.e(this, "不存在新版本");
                CommonUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enterHomeActivity();
                    }
                },2000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多线程下载器
     * @param downloadUrl
     */
    private void download(String downloadUrl){

        HttpUtils httpUtils = new HttpUtils();
        final String fileUrl = Environment.getExternalStorageDirectory().getAbsolutePath();
        httpUtils.download(downloadUrl, fileUrl+"/temp.apk", new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                //下载完毕
                ToastUtil.showToast("下载完毕:"+fileUrl);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                //下载中
                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtil.showToast("下载失败");
                LogUtil.e("CheckVersionBizImpl",e.toString());
                enterHomeActivity();
            }
        });
    }
    @Override
    public void enterHomeActivity() {
        Intent intent = new Intent(mContext, HomeActivity.class);
        mContext.startActivity(intent);
        if (mContext instanceof SplashActivity){
            ((SplashActivity)mContext).finish();
        }
    }
}
