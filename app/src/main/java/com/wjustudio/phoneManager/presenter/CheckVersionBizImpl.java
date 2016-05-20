package com.wjustudio.phoneManager.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wjustudio.phoneManager.activities.HomeActivity;
import com.wjustudio.phoneManager.activities.SplashActivity;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.utils.ToastUtil;

import java.io.File;

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
//        try {
//            String jsonString = OkHttpUtil.getStringFromServer(AppConstants.VERSION_URL);
//            if (TextUtils.isEmpty(jsonString)) {
//                enterHomeActivity();
//            } else {
//                final VersionInfo versionInfo = JSON.parseObject(jsonString, VersionInfo.class);
//                LogUtil.i(this, versionInfo.desc);
//                LogUtil.i(this, versionInfo.version);
//                LogUtil.i(this, jsonString);
//                if (!versionCode.equals(versionInfo.version)) {
//                    //存在新版本,弹出对话框,下载后弹出安装界面.
//                    LogUtil.e(this, "存在新版本");
//                    CommonUtil.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            CommonUtil.showInfoDialog(mContext, versionInfo.desc, "发现新版本"
//                                    , "确定", "取消", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            //下载app
//                                            download(versionInfo.downloadUrl);
//                                        }
//                                    },
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            enterHomeActivity();
//                                        }
//                                    },
//                                    new DialogInterface.OnCancelListener() {
//                                        @Override
//                                        public void onCancel(DialogInterface dialog) {
//                                            enterHomeActivity();
//                                        }
//                                    }
//                            );
//                        }
//                    });
//                } else {
//                    //不存在新版本,进入主界面
//                    LogUtil.e(this, "不存在新版本");
//                    CommonUtil.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            enterHomeActivity();
//                        }
//                    }, 1000);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        CommonUtil.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                enterHomeActivity();
//            }
//        }, 3000);
    }

    /**
     * 多线程下载器
     *
     * @param downloadUrl
     */
    private void download(String downloadUrl) {

        HttpUtils httpUtils = new HttpUtils();
        final String fileUrl = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/phoneManager.apk";
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("下载进度");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        httpUtils.download(downloadUrl, fileUrl, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                //下载完毕
                progressDialog.dismiss();
                //安装
                install();
            }

            /**
             * 安装
             */
            private void install() {
              /*
                <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:scheme="content" />
                <data android:scheme="file" />
                <data android:mimeType="application/vnd.android.package-archive" />
                </intent-filter>*/
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setDataAndType(Uri.fromFile(new File(fileUrl)), "application/vnd.android.package-archive");
                if (mContext instanceof SplashActivity) {
                    ((SplashActivity) mContext).startActivityForResult(intent, 0);
                }
            }


            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                //下载中，弹出对话框
                super.onLoading(total, current, isUploading);
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) current);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtil.showToast("下载失败");
                LogUtil.e("CheckVersionBizImpl", e.toString());
                enterHomeActivity();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void enterHomeActivity() {
        Intent intent = new Intent(mContext, HomeActivity.class);
        mContext.startActivity(intent);
        if (mContext instanceof SplashActivity) {
            ((SplashActivity) mContext).finish();
        }
    }
}
