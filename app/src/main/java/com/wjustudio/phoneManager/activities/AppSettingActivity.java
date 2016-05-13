package com.wjustudio.phoneManager.activities;

import android.view.View;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * songwenju on 16-5-13 : 20 : 04.
 * 邮箱：songwenju@outlook.com
 */
public class AppSettingActivity extends BaseActivity{
    @Bind(R.id.ctl_common_title)
    CommonTitleLayout mCommonTitleLayout;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_app_setting;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected void onSetViewData() {
        mCommonTitleLayout.setTitle("设置");
        mCommonTitleLayout.setImgSettingVisible(false);
    }

    @Override
    protected void onInitListener() {

    }

    @Override
    protected void processClick(View v) {

    }
}
