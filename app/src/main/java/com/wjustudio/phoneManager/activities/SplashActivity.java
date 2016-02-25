package com.wjustudio.phoneManager.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.presenter.SplashPresenter;
import com.wjustudio.phoneManager.utils.SpUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    @Bind(R.id.tv_splash_activity)
    TextView mTvSplashActivity;
    //包管理器
    private PackageManager mPackageManager;
    SplashPresenter mSplashPresenter;

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
            mSplashPresenter = new SplashPresenter(this);
            mSplashPresenter.getUpdate(packageInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSplashPresenter.enterHomeActivity();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
