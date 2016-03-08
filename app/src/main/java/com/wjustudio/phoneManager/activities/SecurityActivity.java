package com.wjustudio.phoneManager.activities;

import android.content.Intent;
import android.view.View;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.utils.SpUtil;

/**
 *
 */
public class SecurityActivity extends BaseActivity{
    @Override
    protected int getLayoutID() {
        return R.layout.activity_security;
    }

    @Override
    protected void onInitView() {

    }

    @Override
    protected void onInitData() {
        //判断应用是否是设置了相关数据
        boolean securitySetting = SpUtil.getBoolean("SecuritySetting", false);
        if (!securitySetting){
            //进入设置界面
            Intent intent = new Intent(this,SecuritySettingActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onSetViewData() {

    }

    @Override
    protected void onIntListener() {

    }

    @Override
    protected void processClick(View v) {

    }
}
