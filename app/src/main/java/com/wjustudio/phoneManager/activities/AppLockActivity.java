package com.wjustudio.phoneManager.activities;

import android.content.Intent;
import android.view.View;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * songwenju on 16-5-5 : 15 : 45.
 * 邮箱：songwenju@outlook.com
 */
public class AppLockActivity extends BaseActivity{
    private String packageName;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_applock;
    }

    @Override
    protected void onInitView() {
        Intent intent = getIntent();
        packageName = intent.getStringExtra("packageName");
        ButterKnife.bind(this);

    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected void onSetViewData() {

    }

    @Override
    protected void onInitListener() {

    }

    @Override
    protected void processClick(View v) {

    }
}
