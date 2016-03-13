package com.wjustudio.phoneManager.activities;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.utils.SpUtil;

import butterknife.Bind;

/**
 * 手机防盗的activity
 */
public class SecurityActivity extends BaseActivity {
    @Bind(R.id.rv_security)
    RecyclerView mRvSecurity;

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
        boolean securitySetting = TextUtils.isEmpty(SpUtil.getString("safeNum", null));
        if (securitySetting) {
            //进入设置界面
            Intent intent = new Intent(this, SecuritySettingActivity.class);
            startActivity(intent);
        } else {
            //进入主界面

        }
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
