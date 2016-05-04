package com.wjustudio.phoneManager.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.adapter.SoftMgrTabFpAdapter;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.base.BaseFragment;
import com.wjustudio.phoneManager.fragment.AppMgrFragment;
import com.wjustudio.phoneManager.fragment.InstallerMgrFragment;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * songwenju on 16-4-14 : 12 : 17.
 * 邮箱：songwenju@outlook.com
 */
public class SoftMgrActivity extends BaseActivity {
    @Bind(R.id.ctl_common_title)
    CommonTitleLayout mCommonTitleLayout;
    @Bind(R.id.tab_title)
    TabLayout mTabTitle;
    @Bind(R.id.vp_pager)
    ViewPager mVpPager;
    private List<BaseFragment> mFragmentList;
    private List<String> mTitleList;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_soft_mgr;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
        mCommonTitleLayout.setTitle("软件管家");
        AppMgrFragment appMgrFragment = new AppMgrFragment();
        InstallerMgrFragment installerMgrFragment = new InstallerMgrFragment();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(appMgrFragment);
        //mFragmentList.add(installerMgrFragment);
    }

    @Override
    protected void onInitData() {
        mTitleList = new ArrayList<>();
        mTitleList.add("软件管理");
        mTitleList.add("安装包管理");
    }

    @Override
    protected void onSetViewData() {
        //设置TabLayout的模式
        mTabTitle.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < mTitleList.size(); i++) {
            mTabTitle.addTab(mTabTitle.newTab().setText(mTitleList.get(i)));
        }

        SoftMgrTabFpAdapter softMgrTabFpAdapter = new SoftMgrTabFpAdapter(
                getSupportFragmentManager(), mFragmentList, mTitleList);

        //viewpager加载adapter
        mVpPager.setAdapter(softMgrTabFpAdapter);
        //TabLayout加载viewpager
        mTabTitle.setupWithViewPager(mVpPager);
    }

    @Override
    protected void onInitListener() {
        mCommonTitleLayout.setOnSettingImgClickListener(new CommonTitleLayout.OnSettingImgClickListener() {
            @Override
            public void onSettingImgClick() {
                Intent intent = new Intent(mContext,SoftMgrSettingActivity.class);
                startActivity(intent);
                toast("设置");
            }
        });
    }

    @Override
    protected void processClick(View v) {

    }

}
