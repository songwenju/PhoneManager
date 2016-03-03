package com.wjustudio.phoneManager.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.presenter.SplashPresenter;
import com.wjustudio.phoneManager.utils.SpUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {
    @Bind(R.id.tv_splash_activity)
    TextView mTvSplashActivity;
    SplashPresenter mSplashPresenter;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onInitData() {
        //判断应用是否是第一次开启应用
        boolean isFirstOpen = SpUtil.getBoolean(AppConstants.FIRST_OPEN, true);
        if (isFirstOpen) {
            Intent intent = new Intent(this,WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onSetViewData() {
        //获得包管理器
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.wjustudio.phoneManager", 0);
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

    @Override
    protected void processClick(View v) {

    }

    @Override
    protected void onIntListener() {

    }

}
