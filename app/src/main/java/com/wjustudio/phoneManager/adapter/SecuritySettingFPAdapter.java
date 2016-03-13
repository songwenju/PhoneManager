package com.wjustudio.phoneManager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wjustudio.phoneManager.base.BaseFragment;

import java.util.ArrayList;

/**
 * 作者：songwenju on 2016/3/13 10:29
 * 邮箱：songwenju01@163.com
 */
public class SecuritySettingFPAdapter extends FragmentPagerAdapter{
    ArrayList<BaseFragment> mFragments;
    public SecuritySettingFPAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
