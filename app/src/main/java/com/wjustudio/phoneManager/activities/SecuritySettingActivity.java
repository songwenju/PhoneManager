package com.wjustudio.phoneManager.activities;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.adapter.SecuritySettingFPAdapter;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.base.BaseFragment;
import com.wjustudio.phoneManager.fragment.SetUpOneFragment;
import com.wjustudio.phoneManager.fragment.SetUpTwoFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 手机防盗的相关设置界面
 */
public class SecuritySettingActivity extends BaseActivity {
    @Bind(R.id.vp_security_setting)
    ViewPager mVpSecuritySetting;
    private ArrayList<BaseFragment> mFragmentList;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_security_setting;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onInitData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new SetUpOneFragment());
        mFragmentList.add(new SetUpTwoFragment());
    }

    @Override
    protected void onSetViewData() {
        //给ViewPager设置适配器
        mVpSecuritySetting.setAdapter(new SecuritySettingFPAdapter(
                getSupportFragmentManager(),mFragmentList));

    }

    @Override
    protected void onInitListener() {

    }

    @Override
    protected void processClick(View v) {

    }

}
