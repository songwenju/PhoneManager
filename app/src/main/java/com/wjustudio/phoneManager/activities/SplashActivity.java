package com.wjustudio.phoneManager.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.biz.CheckVersionBizImpl;
import com.wjustudio.phoneManager.utils.SpUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    @Bind(R.id.tv_splash_activity)
    TextView mTvSplashActivity;
    @Bind(R.id.pb_splash_activity)
    ProgressBar mPbSplashActivity;
    //包管理器
    private PackageManager mPackageManager;
    private CheckVersionBizImpl mCheckVersionBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //判断应用是否是第一次开启应用
        boolean isFirstOpen = SpUtil.getBoolean(AppConstants.FIRST_OPEN, true);
        if (isFirstOpen) {
            Intent intent = new Intent(this,WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
        }
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mPackageManager = getPackageManager();
        try {
            PackageInfo packageInfo = mPackageManager.getPackageInfo("com.wjustudio.phoneManager", 0);
            String versionName = packageInfo.versionName;
            mTvSplashActivity.setText(versionName);
            final int versionCode = packageInfo.versionCode;
            mCheckVersionBiz = new CheckVersionBizImpl((Context) SplashActivity.this);
            //请求网络在子线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mCheckVersionBiz.checkVersion(String.valueOf(versionCode));
                }
            }).start();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCheckVersionBiz.enterHomeActivity();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
